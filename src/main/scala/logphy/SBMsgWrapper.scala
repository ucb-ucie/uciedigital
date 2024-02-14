package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._
import interfaces._

case class LogPHYSBParams(
    width: Int = 64,
)

class SBMsgWrapperTrainIO(
) extends Bundle {

  val msgRequest = Flipped(Decoupled(
    UInt()
  ))

  val exchangeMsg = Flipped(Decoupled(new SBExchangeMsg))
  val exchangeMsgStatus = Decoupled(SBMsgExchangeStatus())
  val reqMsg = Flipped(Decoupled(new SBReqMsg))
  val respMsg = Flipped(Decoupled(new SBReqMsg))
  val reqMsgStatus = Decoupled(new SBMsgReqRespStatus)
  val rspMsgStatus = Decoupled(new SBMsgReqRespStatus)
}

/** TODO: implementation */
class SBMsgWrapper(
    afeParams: AfeParams,
) extends Module {
  val io = IO(new Bundle {
    val trainIO = new SBMsgWrapperTrainIO
    val laneIO = new SidebandLaneIO(afeParams)
  })

  private object State extends ChiselEnum {
    val IDLE, EXCHANGE_MSG_SEND, EXCHANGE_MSG_WAIT, SEND_REQ, WAIT_RESP = Value
  }

  /** checks for a match in the pattern sent */
  private val currentState = RegInit(State.IDLE)
  private val nextState = Wire(State.IDLE)

  switch(currentState) {
    is(State.IDLE) {
    }
  }

}
