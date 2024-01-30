package edu.berkeley.cs.ucie.digital
package logphy

import interfaces._

import chisel3._
import chisel3.util._

case class LinkTrainingParams(
    /** The amount of cycles to wait after driving the PLL frequency */
    pllWaitTime: Int = 100,
    maxSBMessageSize: Int = 128,
    voltageSwing: Int = 0,
    maximumDataRate: Int = 0,
    clockMode: ClockModeParam.Type = ClockModeParam.strobe,
)

class LinkTrainingFSM(
    linkTrainingParams: LinkTrainingParams,
    afeParams: AfeParams,
    sidebandParams: LogPHYSBParams,
    rdiParams: RdiParams,
) extends Module {

  val io = IO(new Bundle {
    val mbAfe = new MainbandAfeIo(afeParams)
    val sbAfe = new SidebandAfeIo(afeParams)
    val sbIO = new LogPHYSBTrainIO(sidebandParams)
    val rdi = new Rdi(rdiParams)
  })

  /** Initialize params */
  private val voltageSwing = RegInit(linkTrainingParams.voltageSwing.U(5.W))

  private object State extends ChiselEnum {
    val reset, sbInit, mbInit, linkInit, active, linkError = Value
  }

  private val currentState = RegInit(State.reset)
  private val nextState = Wire(currentState)
  private val resetCounter = Counter(
    Range(1, 10000), // TODO: value
    true.B,
    (nextState === State.reset && currentState =/= State.reset), // TODO: does this also reset on implicit reset
  )
  io.mbAfe.txZpd := VecInit.fill(afeParams.mbLanes)(0.U)
  io.mbAfe.txZpu := VecInit.fill(afeParams.mbLanes)(0.U)
  io.mbAfe.rxZ := VecInit.fill(afeParams.mbLanes)(0.U)

  private object ResetSubState extends ChiselEnum {
    val INIT, FREQ_SEL_CYC_WAIT, FREQ_SEL_LOCK_WAIT = Value
  }
  private val resetSubState = RegInit(ResetSubState.INIT)
  when(nextState === State.reset) {
    resetSubState := ResetSubState.INIT
  }

  private object SBInitSubState extends ChiselEnum {
    val SEND_CLOCK, SEND_LOW, WAIT_CLOCK, SB_OUT_OF_RESET_EXCH,
        SB_OUT_OF_RESET_WAIT, SB_DONE_EX, SB_DONE_WAIT = Value
  }
  private val sbInitSubState = RegInit(SBInitSubState.SEND_CLOCK)
  when(nextState === State.sbInit) {
    sbInitSubState := SBInitSubState.SEND_CLOCK
  }

  private object MBInitSubState extends ChiselEnum {
    val PARAM, REPAIR_CLK, REPAIR_VAL = Value
  }
  private val mbInitSubState = RegInit(MBInitSubState.PARAM)
  when(nextState === State.mbInit) {
    mbInitSubState := MBInitSubState.PARAM
  }

  // TODO: incorporate lpstatereq
  currentState := nextState

  switch(currentState) {
    is(State.reset) {
      io.mbAfe.rxEn := false.B
      io.sbAfe.rxEn := true.B
      val resetFreqCtrValue = false.B
      // TODO: is this the correct way to set a vec to be all 0's
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
            io.mbAfe.pllLock && io.sbAfe.pllLock && (io.rdi.lpStateReq =/= PhyStateReq.reset),
          ) {
            nextState := State.sbInit
          }
        }

      }
    }
    is(State.sbInit) {

      /** UCIe Module mainband (MB) transmitters remain tri-stated, SB
        * Transmitters continue to be held Low, SB Receivers continue to be
        * enabled
        */

      switch(sbInitSubState) {
        is(SBInitSubState.SEND_CLOCK) {
          io.sbIO.transmitPattern.bits := TransmitPattern.CLOCK_64_LOW_32
          io.sbIO.transmitPattern.valid := true.B
          when(io.sbIO.transmitPattern.fire) {
            // sbInitSubState := SBInitSubState.SEND_LOW
            sbInitSubState := SBInitSubState.WAIT_CLOCK
          }
        }
        // is(SBInitSubState.SEND_LOW) {
        //   io.sbIO.transmitPattern.bits.data := VecInit(
        //     Seq.fill(32)(0.U),
        //   ) // TODO: low pattern
        //   // TODO: fix length
        //   io.sbIO.transmitPattern.bits.length := 128.U
        //   io.sbIO.transmitPattern.valid := true.B
        //   when(io.sbIO.transmitPattern.fire) {
        //     sbInitSubState := SBInitSubState.WAIT_CLOCK
        //   }
        // }
        is(SBInitSubState.WAIT_CLOCK) {
          io.sbIO.transmitPattern.ready := true.B
          when(io.sbIO.transmitPatternStatus.fire) {
            switch(io.sbIO.transmitPatternStatus.bits) {
              is(SBMsgExchangeStatus.SUCCESS) {
                sbInitSubState := SBInitSubState.SB_OUT_OF_RESET_EXCH
              }
              is(SBMsgExchangeStatus.ERR) {
                nextState := State.linkError
              }
            }
          }
        }
        is(SBInitSubState.SB_OUT_OF_RESET_EXCH) {
          io.sbIO.exchangeMsg.bits.exchangeMsg := SBMsgType.OUT_OF_RESET
          io.sbIO.exchangeMsg.valid := true.B
          when(io.sbIO.exchangeMsg.fire) {
            sbInitSubState := SBInitSubState.SB_OUT_OF_RESET_WAIT
          }
        }
        is(SBInitSubState.SB_OUT_OF_RESET_WAIT) {
          io.sbIO.exchangeMsgStatus.ready := true.B
          when(io.sbIO.exchangeMsgStatus.fire) {
            switch(io.sbIO.exchangeMsgStatus.bits) {
              is(SBMsgExchangeStatus.SUCCESS) {
                sbInitSubState := SBInitSubState.SB_DONE_EX
              }
              is(SBMsgExchangeStatus.ERR) {
                nextState := State.linkError
              }
            }
          }
        }
        is(SBInitSubState.SB_DONE_EX) {
          io.sbIO.exchangeMsg.bits.exchangeMsg := SBMsgType.DONE
          io.sbIO.exchangeMsg.valid := true.B
          when(io.sbIO.exchangeMsg.fire) {
            sbInitSubState := SBInitSubState.SB_DONE_WAIT
          }
        }
        is(SBInitSubState.SB_DONE_WAIT) {
          io.sbIO.exchangeMsgStatus.ready := true.B
          when(io.sbIO.exchangeMsgStatus.fire) {
            switch(io.sbIO.exchangeMsgStatus.bits) {
              is(SBMsgExchangeStatus.SUCCESS) {
                nextState := State.mbInit
              }
              is(SBMsgExchangeStatus.ERR) {
                nextState := State.linkError
              }
            }
          }
        }
      }

    }
    is(State.mbInit) {
      switch(mbInitSubState) {
        is(MBInitSubState.PARAM) {}
        is(MBInitSubState.REPAIR_CLK) {}
        is(MBInitSubState.REPAIR_VAL) {}
      }
    }
    is(State.linkInit) {}
    is(State.active) {

      /** Active state = do nothing, not currently in training.
        */
    }
    is(State.linkError) {
      // TODO: What to do when I receive an error?
    }
  }

}
