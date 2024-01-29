package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._
import interfaces._

case class LogPHYSBParams(
    width: Int = 64,
)

class SBLaneIO(params: AfeParams) extends Bundle {

  /** Data to transmit on the sideband.
    *
    * Output from the async FIFO.
    */
  val txData = Decoupled(Bits(params.sbSerializerRatio.W))
  val txValid = Decoupled(Bits(params.sbSerializerRatio.W))

  /** Data received on the sideband.
    *
    * Input to the async FIFO.
    */
  val rxData = Flipped(Decoupled(Bits(params.sbSerializerRatio.W)))
}

class LogPHYSBTrainIO(
    params: LogPHYSBParams,
    afeParams: AfeParams,
) extends Bundle {
  // may be moved to common module to share between SB and MB
  val transmitPattern = Flipped(
    Decoupled(TransmitPattern()),
  ) // data to transmit & receive over SB
  val transmitPatternStatus = Decoupled(SBMsgExchangeStatus())
  val exchangeMsg = Flipped(Decoupled(new SBMsgExchange))
  val exchangeMsgStatus = Decoupled(SBMsgExchangeStatus())
  val reqRespMsg = Flipped(Decoupled(new SBMsgReqResp))
  val reqRspMsgStatus = Decoupled(SBMsgReqRespStatus())
  val dataReceived = Decoupled()
  val dataOut = new SBLaneIO(afeParams)
}

/** TODO: implementation */
class LogPHYSB(
    params: LogPHYSBParams,
    afeParams: AfeParams,
) extends Module {
  val io = IO(new LogPHYSBTrainIO(params, afeParams))

  private object State extends ChiselEnum {
    val IDLE, TRANSMIT_AND_DETECT, EXCHANGE_MSG_SEND, EXCHANGE_MSG_WAIT,
        SEND_REQ, WAIT_RESP = Value
  }

  /** checks for a match in the pattern sent */
  // TODO: possibly fix API here
  private val patternIn = RegInit(0.U(params.width.W))
  private val patternDetect = RegInit(0.U(params.width.W))

  private val currentState = RegInit(State.IDLE)
  private val nextState = Wire(State.IDLE)

  switch(currentState) {
    is(State.IDLE) {}
  }

}
