package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._

/** Sideband Types */
object SBMsgType extends ChiselEnum {
  val OutOfReset, Done = Value
}

class SBMsgExchange extends Bundle {
  val exchangeMsg = SBMsgType()
}

object SBMsgExchangeStatus extends ChiselEnum {
  val SUCCESS, ERR = Value
}

object SBMsgReqRespStatus extends ChiselEnum {
  val SUCCESS, ERR = Value
}

class SBMsgReqResp extends Bundle {
  val reqMsg = SBMsgType()
  val respMsg = SBMsgType()
}

/** Param Enums */

object ClockModeParam extends ChiselEnum {
  val strobe = Value(0.U)
  val continuous = Value(1.U)
}

private object TransmitPattern extends ChiselEnum {
  val CLOCK_64_LOW_32 = Value(0.U)
}

val transmitPatternBits = Seq(
  TransmitPattern.CLOCK_64_LOW_32 -> BitPat(""),
)
