package edu.berkeley.cs.ucie.digital
package logphy

import interfaces._

import chisel3._
import chisel3.util._

case class MBTrainingParams(
    voltageSwing: Int = 0,
    maximumDataRate: Int = 0,
    clockMode: ClockModeParam.Type = ClockModeParam.strobe,
    clockPhase: Boolean = false,
    moduleId: Int = 0,
    ucieAx32: Boolean = false,
)

class MBInitFSM(
    trainingParams: MBTrainingParams,
    afeParams: AfeParams,
) extends Module {

  val io = IO(new Bundle {
    // TODO: needs trigger?
    val sbTrainIO = new SBMsgWrapperTrainIO
    val transition = Output(Bool())
    val error = Output(Bool())
  })
  private object State extends ChiselEnum {
    val PARAM, REPAIR_CLK, REPAIR_VAL, IDLE, ERR = Value
  }

  private object ParamSubState extends ChiselEnum {
    val SEND_REQ, WAIT_REQ, SEND_RESP, WAIT_RESP = Value
  }

  private val state = RegInit(State.PARAM)
  private val nextState = Wire(state)
  private val paramSubState = RegInit(ParamSubState.SEND_REQ)
  when(nextState === State.PARAM) {
    paramSubState := ParamSubState.SEND_REQ
  }

  when(reset.asBool) {
    nextState := State.PARAM
  }
  io.transition := nextState === State.IDLE || nextState === State.ERR
  io.error := state === State.ERR
  state := nextState

  /** Initialize params */
  private val voltageSwing = RegInit(
    trainingParams.voltageSwing.U(5.W),
  )
  private val maxDataRate = RegInit(
    trainingParams.maximumDataRate.U(4.W),
  )
  private val clockMode = RegInit(
    trainingParams.clockMode,
  )
  private val clockPhase = RegInit(
    trainingParams.clockPhase.B,
  )
  // TODO: not implemented
  private val moduleId = RegInit(
    trainingParams.moduleId.U(2.W),
  )
  // TODO: not implemented
  private val ucieAx32 = RegInit(
    trainingParams.ucieAx32.B,
  )

  switch(state) {
    is(State.PARAM) {
      def formParamsReqMsg(
          req: Bool,
          voltageSwing: UInt,
          maxDataRate: UInt,
          clockMode: ClockModeParam.Type,
          clockPhase: Bool,
          moduleId: UInt,
          UCIeAx32: Bool,
      ): SBReqMsg = {
        val sbReq = new SBReqMsg
        sbReq.msg := Mux(
          req,
          SBMsgType.PARAM_CONFIG_REQ,
          SBMsgType.PARAM_CONFIG_RESP,
        )
        sbReq.data := Cat(
          0.U(50.W),
          UCIeAx32,
          moduleId(1, 0),
          clockPhase,
          clockMode.asUInt,
          voltageSwing(4, 0),
          maxDataRate(3, 0),
        )
        sbReq.msgInfo := 0.U(16.W)
        sbReq
      }

      val reqData = RegInit(0.U(64.W))

      switch(paramSubState) {
        is(ParamSubState.SEND_REQ) {
          io.sbTrainIO.reqMsg.valid := true.B
          io.sbTrainIO.reqMsg.bits := formParamsReqMsg(
            true.B,
            voltageSwing,
            maxDataRate,
            clockMode,
            clockPhase,
            moduleId,
            ucieAx32,
          )
          when(io.sbTrainIO.reqMsg.fire) {
            paramSubState := ParamSubState.WAIT_REQ
          }
        }
        is(ParamSubState.WAIT_REQ) {
          io.sbTrainIO.reqMsgStatus.ready := true.B
          when(io.sbTrainIO.reqMsgStatus.fire) {
            reqData := io.sbTrainIO.reqMsgStatus.bits.data
            when(
              io.sbTrainIO.reqMsgStatus.bits.status === SBMsgReqRespStatusType.ERR,
            ) {
              nextState := State.ERR
            }.otherwise {
              paramSubState := ParamSubState.SEND_RESP
            }
          }
        }
        is(ParamSubState.SEND_RESP) {
          io.sbTrainIO.respMsg.valid := true.B
          val exchangedMaxDataRate = UInt(4.W)
          exchangedMaxDataRate := Mux(
            maxDataRate >= reqData(3, 0),
            reqData(3, 0),
            maxDataRate,
          )

          io.sbTrainIO.respMsg.bits := formParamsReqMsg(
            false.B,
            0.U,
            exchangedMaxDataRate,
            ClockModeParam(reqData(9, 9)),
            reqData(10, 10).asBool,
            0.U,
            0.B,
          )
        }
        is(ParamSubState.WAIT_RESP) {}
      }

    }
    is(State.REPAIR_CLK) {}
    is(State.REPAIR_VAL) {}

  }

}
