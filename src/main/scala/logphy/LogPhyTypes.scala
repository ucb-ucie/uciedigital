package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._

object LinkTrainingState extends ChiselEnum {
  val reset, sbInit, mbInit, linkInit, active, linkError = Value
}

/** Sideband Types */
object SBMsgType extends ChiselEnum {
  val OUT_OF_RESET, DONE, PARAM_CONFIG_REQ, PARAM_CONFIG_RESP = Value
}

class SBMsgExchange extends Bundle {
  val exchangeMsg = SBMsgType()
}

object SBMsgExchangeStatus extends ChiselEnum {
  val SUCCESS, ERR = Value
}

object SBMsgReqRespStatusType extends ChiselEnum {
  val SUCCESS, ERR = Value
}

class SBMsgReqRespStatus extends Bundle {
  val status = SBMsgReqRespStatusType()
  val data = UInt(64.W)
}

class SBReqMsg extends Bundle {
  val msg = SBMsgType()
  val data = UInt(64.W)
  val msgInfo = UInt(16.W)
}

// class SBMsgReqResp extends Bundle {
//   val reqMsg = new SBReqMsg()
//   val respMsg = new SBReqMsg()
// }

/** Param Enums */

object ClockModeParam extends ChiselEnum {
  val strobe = Value(0.U)
  val continuous = Value(1.U)
}

object TransmitPattern extends ChiselEnum {
  val CLOCK_64_LOW_32 = Value(0.U)
}

object TransmitPatternConsts {

  val transmitPatternBits = Seq(
    TransmitPattern.CLOCK_64_LOW_32 -> BitPat(""),
  )
}
