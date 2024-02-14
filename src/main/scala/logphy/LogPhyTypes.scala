package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._
import interfaces._

object LinkTrainingState extends ChiselEnum {
  val reset, sbInit, mbInit, linkInit, active, linkError = Value
}

/** Sideband Types */
object SBMsgType extends ChiselEnum {
  val OUT_OF_RESET, DONE, PARAM_CONFIG_REQ, PARAM_CONFIG_RESP = Value
}

class SBExchangeMsg extends Bundle {
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

/** Param Enums */

object ClockModeParam extends ChiselEnum {
  val strobe = Value(0.U)
  val continuous = Value(1.U)
}

object TransmitPattern extends ChiselEnum {
  val CLOCK_64_LOW_32 = Value(0.U)
}

class SBIO(params: AfeParams) extends Bundle {

  val fifoParams = Input(new FifoParams())

  /** Data to transmit on the sideband.
    *
    * Output from the async FIFO.
    */
  val txData = Decoupled(Bits(params.sbSerializerRatio.W))
  val txValid = Decoupled(Bool())

  /** Data received on the sideband.
    *
    * Input to the async FIFO.
    */
  val rxData = Flipped(Decoupled(Bits(params.sbSerializerRatio.W)))
}

class MainbandIO(
    afeParams: AfeParams,
) extends Bundle {

  val fifoParams = Input(new FifoParams())

  /** Data to transmit on the mainband.
    *
    * Output from the async FIFO.
    *
    * @group data
    */
  val txData = Decoupled(
    Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W)),
  )

  /** Data received on the mainband.
    *
    * Input to the async FIFO.
    *
    * @group data
    */
  val rxData = Flipped(
    Decoupled(Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W))),
  )
}

class MainbandLaneIO(
    afeParams: AfeParams,
) extends Bundle {

  /** Data to transmit on the mainband.
    */
  val txData = Flipped(
    Decoupled(Bits((afeParams.mbLanes * afeParams.mbSerializerRatio).W)),
  )

  val rxData =
    Decoupled(Bits((afeParams.mbLanes * afeParams.mbSerializerRatio).W))
}

class SidebandLaneIO(
    afeParams: AfeParams,
) extends Bundle {

  /** Data to transmit on the mainband.
    */
  val txData = Flipped(
    Decoupled(Bits((afeParams.sbSerializerRatio).W)),
  )

  val rxData =
    Decoupled(Bits((afeParams.sbSerializerRatio).W))
}
