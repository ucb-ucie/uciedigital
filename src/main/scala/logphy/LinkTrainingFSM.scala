package edu.berkeley.cs.ucie.digital
package logphy

import interfaces._
import ucie.sideband.{SBM, SBMessage_factory}

import chisel3._
import chisel3.util._

case class LinkTrainingParams(
    /** The amount of cycles to wait after driving the PLL frequency */
    pllWaitTime: Int = 100,
    maxSBMessageSize: Int = 128,
    mbTrainingParams: MBTrainingParams,
    sbClockFreqAnalog: 800_000_000,
)

class LinkTrainingRdiIO(
    rdiParams: RdiParams,
) extends Bundle {
  val lpStateReq = Input(PhyStateReq())
}

class LinkTrainingFSM(
    linkTrainingParams: LinkTrainingParams,
    afeParams: AfeParams,
    rdiParams: RdiParams,
) extends Module {

  val sbClockFreq =
    linkTrainingParams.sbClockFreqAnalog / afeParams.sbSerializerRatio

  val io = IO(new Bundle {
    val mbAfe = new MainbandAfeIo(afeParams)
    val sbAfe = new SidebandAfeIo(afeParams)

    /** packet output from training */
    val mainbandLaneIO = new MainbandLaneIO(afeParams)
    val sidebandLaneIO = new SidebandLaneIO(afeParams)
    val rdi = new LinkTrainingRdiIO(rdiParams)
    val active = Output(Bool())
  })

  val patternGenerator = new PatternGenerator(afeParams)
  val sbMsgWrapper = new SBMsgWrapper(afeParams)

  private object MsgSource extends ChiselEnum {
    val PATTERN_GENERATOR, SB_MSG_WRAPPER = Value
  }
  private val msgSource = Wire(MsgSource.PATTERN_GENERATOR)
  io.mainbandLaneIO <> patternGenerator.io.mainbandLaneIO
  when(msgSource === MsgSource.PATTERN_GENERATOR) {
    io.sidebandLaneIO <> patternGenerator.io.sidebandLaneIO
  }.otherwise {
    io.sidebandLaneIO <> sbMsgWrapper.io.laneIO
  }

  private val currentState = RegInit(LinkTrainingState.reset)
  private val nextState = Wire(currentState)
  private val resetCounter = Counter(
    Range(1, 10000), // TODO: value
    true.B,
    (nextState === LinkTrainingState.reset && currentState =/= LinkTrainingState.reset), // TODO: does this also reset on implicit reset
  )
  io.mbAfe.txZpd := VecInit.fill(afeParams.mbLanes)(0.U)
  io.mbAfe.txZpu := VecInit.fill(afeParams.mbLanes)(0.U)
  io.mbAfe.rxZ := VecInit.fill(afeParams.mbLanes)(0.U)

  private object ResetSubState extends ChiselEnum {
    val INIT, FREQ_SEL_CYC_WAIT, FREQ_SEL_LOCK_WAIT = Value
  }
  private val resetSubState = RegInit(ResetSubState.INIT)
  when(nextState === LinkTrainingState.reset) {
    resetSubState := ResetSubState.INIT
  }

  private object SBInitSubState extends ChiselEnum {
    val SEND_CLOCK, SEND_LOW, WAIT_CLOCK, SB_OUT_OF_RESET_EXCH,
        SB_OUT_OF_RESET_WAIT, SB_DONE_REQ, SB_DONE_REQ_WAIT, SB_DONE_RESP,
        SB_DONE_RESP_WAIT = Value
  }
  private val sbInitSubState = RegInit(SBInitSubState.SEND_CLOCK)
  when(
    nextState === LinkTrainingState.sbInit && currentState =/= LinkTrainingState.sbInit,
  ) {
    sbInitSubState := SBInitSubState.SEND_CLOCK
  }

  private val mbInit = Module(
    new MBInitFSM(
      linkTrainingParams,
      linkTrainingParams.mbTrainingParams,
      afeParams,
    ),
  )
  mbInit.reset := (nextState === LinkTrainingState.mbInit) && (currentState =/= LinkTrainingState.mbInit)

  // TODO: incorporate lpstatereq
  currentState := nextState
  io.active := currentState === LinkTrainingState.active

  switch(currentState) {
    is(LinkTrainingState.reset) {
      io.mbAfe.rxEn := false.B
      io.sbAfe.rxEn := true.B
      val resetFreqCtrValue = false.B
      io.mbAfe.txZpd := VecInit(Seq.fill(afeParams.mbLanes)(0.U))
      io.mbAfe.txZpu := VecInit(Seq.fill(afeParams.mbLanes)(0.U))
      val (freqSelCtrValue, freqSelCtrWrap) = Counter(
        Range(1, linkTrainingParams.pllWaitTime),
        true.B,
        resetFreqCtrValue,
      )
      switch(resetSubState) {
        is(ResetSubState.INIT) {
          when(io.mbAfe.pllLock && io.sbAfe.pllLock) {
            io.mbAfe.txFreqSel := SpeedMode.speed4
            resetSubState := ResetSubState.FREQ_SEL_CYC_WAIT
            resetFreqCtrValue := true.B
          }
        }
        is(ResetSubState.FREQ_SEL_CYC_WAIT) {
          when(freqSelCtrValue === (linkTrainingParams.pllWaitTime - 1).U) {
            resetSubState := ResetSubState.FREQ_SEL_LOCK_WAIT
          }
        }
        is(ResetSubState.FREQ_SEL_LOCK_WAIT) {
          when(
            io.mbAfe.pllLock && io.sbAfe.pllLock && (io.rdi.lpStateReq =/= PhyStateReq.linkReset
            /** TODO: what happened to reset */
            ),
          ) {
            nextState := LinkTrainingState.sbInit
          }
        }

      }
    }
    is(LinkTrainingState.sbInit) {

      /** UCIe Module mainband (MB) transmitters remain tri-stated, SB
        * Transmitters continue to be held Low, SB Receivers continue to be
        * enabled
        */

      switch(sbInitSubState) {
        is(SBInitSubState.SEND_CLOCK) {
          patternGenerator.io.patternGeneratorIO.transmitInfo.bits.pattern := TransmitPattern.CLOCK_64_LOW_32
          patternGenerator.io.patternGeneratorIO.transmitInfo.bits.sideband := true.B

          /** Timeout occurs after 8ms */
          patternGenerator.io.patternGeneratorIO.transmitInfo.bits.timeoutCycles := (
            0.008 * sbClockFreq,
          ).toInt.U

          patternGenerator.io.patternGeneratorIO.transmitInfo.valid := true.B
          msgSource := MsgSource.PATTERN_GENERATOR
          when(patternGenerator.io.patternGeneratorIO.transmitInfo.fire) {
            sbInitSubState := SBInitSubState.WAIT_CLOCK
          }
        }
        is(SBInitSubState.WAIT_CLOCK) {
          patternGenerator.io.patternGeneratorIO.transmitPatternStatus.ready := true.B
          msgSource := MsgSource.PATTERN_GENERATOR
          when(
            patternGenerator.io.patternGeneratorIO.transmitPatternStatus.fire,
          ) {
            switch(
              patternGenerator.io.patternGeneratorIO.transmitPatternStatus.bits,
            ) {
              is(MessageRequestStatusType.SUCCESS) {
                sbInitSubState := SBInitSubState.SB_OUT_OF_RESET_EXCH
              }
              is(MessageRequestStatusType.ERR) {
                nextState := LinkTrainingState.linkError
              }
            }
          }
        }
        is(SBInitSubState.SB_OUT_OF_RESET_EXCH) {
          sbMsgWrapper.io.trainIO.msgReq.bits.msg := SBMessage_factory(
            SBM.SBINIT_OUT_OF_RESET,
            "PHY",
            true,
            "PHY",
          )
          sbMsgWrapper.io.trainIO.msgReq.bits.reqType := MessageRequestType.MSG_EXCH
          sbMsgWrapper.io.trainIO.msgReq.bits.msgTypeHasData := false.B
          sbMsgWrapper.io.trainIO.msgReq.valid := true.B

          sbMsgWrapper.io.trainIO.msgReq.bits.timeoutCycles := (
            0.008 * sbClockFreq,
          ).toInt.U
          msgSource := MsgSource.SB_MSG_WRAPPER
          when(sbMsgWrapper.io.trainIO.msgReq.fire) {
            sbInitSubState := SBInitSubState.SB_OUT_OF_RESET_WAIT
          }
        }
        is(SBInitSubState.SB_OUT_OF_RESET_WAIT) {
          sbMsgWrapper.io.trainIO.msgReqStatus.ready := true.B
          msgSource := MsgSource.SB_MSG_WRAPPER
          when(sbMsgWrapper.io.trainIO.msgReqStatus.fire) {
            switch(sbMsgWrapper.io.trainIO.msgReqStatus.bits.status) {
              is(MessageRequestStatusType.SUCCESS) {
                sbInitSubState := SBInitSubState.SB_DONE_REQ
              }
              is(MessageRequestStatusType.ERR) {
                nextState := LinkTrainingState.linkError
              }
            }
          }
        }
        is(SBInitSubState.SB_DONE_REQ) {
          sbMsgWrapper.io.trainIO.msgReq.bits.msg := SBMessage_factory(
            SBM.SBINIT_DONE_REQ,
            "PHY",
            false,
            "PHY",
          )
          sbMsgWrapper.io.trainIO.msgReq.bits.reqType := MessageRequestType.MSG_REQ
          sbMsgWrapper.io.trainIO.msgReq.bits.msgTypeHasData := false.B
          sbMsgWrapper.io.trainIO.msgReq.valid := true.B
          sbMsgWrapper.io.trainIO.msgReq.bits.timeoutCycles := (
            0.008 * sbClockFreq,
          ).toInt.U
          msgSource := MsgSource.SB_MSG_WRAPPER
          when(sbMsgWrapper.io.trainIO.msgReq.fire) {
            sbInitSubState := SBInitSubState.SB_DONE_REQ_WAIT
          }
        }
        is(SBInitSubState.SB_DONE_REQ_WAIT) {
          sbMsgWrapper.io.trainIO.msgReqStatus.ready := true.B
          msgSource := MsgSource.SB_MSG_WRAPPER
          when(sbMsgWrapper.io.trainIO.msgReqStatus.fire) {
            switch(sbMsgWrapper.io.trainIO.msgReqStatus.bits.status) {
              is(MessageRequestStatusType.SUCCESS) {
                sbInitSubState := SBInitSubState.SB_DONE_RESP
              }
              is(MessageRequestStatusType.ERR) {
                nextState := LinkTrainingState.linkError
              }
            }
          }
        }
        is(SBInitSubState.SB_DONE_RESP) {
          sbMsgWrapper.io.trainIO.msgReq.bits.msg := SBMessage_factory(
            SBM.SBINIT_DONE_RESP,
            "PHY",
            false,
            "PHY",
          )
          sbMsgWrapper.io.trainIO.msgReq.bits.reqType := MessageRequestType.MSG_RESP
          sbMsgWrapper.io.trainIO.msgReq.valid := true.B
          sbMsgWrapper.io.trainIO.msgReq.bits.msgTypeHasData := false.B
          msgSource := MsgSource.SB_MSG_WRAPPER
          sbMsgWrapper.io.trainIO.msgReq.bits.timeoutCycles := (
            0.008 * sbClockFreq,
          ).toInt.U
          when(sbMsgWrapper.io.trainIO.msgReq.fire) {
            sbInitSubState := SBInitSubState.SB_DONE_RESP_WAIT
          }
        }
        is(SBInitSubState.SB_DONE_RESP_WAIT) {
          sbMsgWrapper.io.trainIO.msgReqStatus.ready := true.B
          msgSource := MsgSource.SB_MSG_WRAPPER
          when(sbMsgWrapper.io.trainIO.msgReqStatus.fire) {
            switch(sbMsgWrapper.io.trainIO.msgReqStatus.bits.status) {
              is(MessageRequestStatusType.SUCCESS) {
                nextState := LinkTrainingState.mbInit
              }
              is(MessageRequestStatusType.ERR) {
                nextState := LinkTrainingState.linkError
              }
            }
          }
        }
      }
    }
    is(LinkTrainingState.mbInit) {
      mbInit.io.sbTrainIO <> sbMsgWrapper.io.trainIO
      msgSource := MsgSource.SB_MSG_WRAPPER
      when(mbInit.io.transition.asBool) {
        nextState := Mux(
          mbInit.io.error,
          LinkTrainingState.linkError,
          LinkTrainingState.linkInit,
        )
      }
    }
    is(LinkTrainingState.linkInit) {}
    is(LinkTrainingState.active) {

      /** Active state = do nothing, not currently in training.
        */
    }
    is(LinkTrainingState.linkError) {
      // TODO: What to do when I receive an error?
    }
  }

}
