package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._
import sideband.{SBMessage_factory, SBM}
import interfaces._

class RdiBringupIO extends Bundle {
  // Tie to 1 if clock gating not supported.
  val plClkReq = Output(Bool())
  val lpClkAck = Input(Bool())

  // Tie to 1 if clock gating not supported.
  val lpWakeReq = Input(Bool())
  val plWakeAck = Output(Bool())

  val lpStateReq = Input(PhyStateReq())
}

class RdiBringup extends Module {

  val io = IO(new Bundle {
    val rdiIO = new RdiBringupIO
    val sbTrainIO = Flipped(new SBMsgWrapperTrainIO)
    val transition = Output(Bool())
    val error = Output(Bool())
  })

  io.rdiIO.plClkReq := true.B
  io.rdiIO.plWakeAck := true.B

  private object State extends ChiselEnum {
    val CLK_HANDSHAKE, LP_WAKE_HANDSHAKE, WAIT_LP_STATE_REQ, REQ_ACTIVE_SEND,
        REQ_ACTIVE_WAIT, RESP_ACTIVE_SEND, RESP_ACTIVE_WAIT, IDLE, ERR = Value
  }

  private val state = RegInit(State.WAIT_LP_STATE_REQ)
  private val nextState = WireInit(state)
  io.transition := nextState === State.IDLE || nextState === State.ERR
  io.error := state === State.ERR
  state := nextState
  io.sbTrainIO.msgReq.noenq()
  io.sbTrainIO.msgReqStatus.nodeq()

  switch(state) {
    is(State.WAIT_LP_STATE_REQ) {
      when(io.rdiIO.lpStateReq === PhyStateReq.active) {
        nextState := State.REQ_ACTIVE_SEND
      }
    }
    is(State.REQ_ACTIVE_SEND) {
      io.sbTrainIO.msgReq.valid := true.B
      io.sbTrainIO.msgReq.bits.msg := SBMessage_factory(
        base = SBM.LINK_MGMT_RDI_REQ_ACTIVE,
        src = "PHY",
        dst = "PHY",
      )

      /** TODO: how many timeout cycles here? */
      io.sbTrainIO.msgReq.bits.timeoutCycles := 1_000_000.U
      when(io.sbTrainIO.msgReq.fire) {
        nextState := State.REQ_ACTIVE_WAIT
      }
    }
    is(State.REQ_ACTIVE_WAIT) {
      io.sbTrainIO.msgReqStatus.ready := true.B
      when(io.sbTrainIO.msgReqStatus.fire) {
        nextState := State.RESP_ACTIVE_SEND
      }
    }
    is(State.RESP_ACTIVE_SEND) {
      io.sbTrainIO.msgReq.valid := true.B
      io.sbTrainIO.msgReq.bits.msg := SBMessage_factory(
        base = SBM.LINK_MGMT_RDI_RSP_ACTIVE,
        src = "PHY",
        dst = "PHY",
      )

      /** TODO: how many timeout cycles here? */
      io.sbTrainIO.msgReq.bits.timeoutCycles := 1_000_000.U
      when(io.sbTrainIO.msgReqStatus.fire) {
        nextState := State.RESP_ACTIVE_WAIT
      }
    }
    is(State.RESP_ACTIVE_WAIT) {
      io.sbTrainIO.msgReqStatus.ready := true.B
      when(io.sbTrainIO.msgReqStatus.fire) {
        nextState := State.IDLE
      }
    }
    is(State.IDLE) {}
    is(State.ERR) {}

  }

}
