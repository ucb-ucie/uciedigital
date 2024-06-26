package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._
import sideband.SidebandParams
import interfaces._

object LinkTrainingState extends ChiselEnum {
  val reset, sbInit, mbInit, linkInit, active, linkError, retrain = Value
}

object MsgSource extends ChiselEnum {
  val PATTERN_GENERATOR, SB_MSG_WRAPPER = Value
}

/** Sideband Types */

class SBExchangeMsg extends Bundle {
  val exchangeMsg = UInt(128.W)
}

object MessageRequestStatusType extends ChiselEnum {
  val SUCCESS, ERR = Value
}

class SBReqMsg extends Bundle {
  val msg = UInt(128.W)
}

class MessageRequest extends Bundle {
  val msg = UInt(128.W)
  val timeoutCycles = UInt(64.W)
  // val msgTypeHasData = Bool()
}

class MessageRequestStatus extends Bundle {
  val status = MessageRequestStatusType()
  val data = UInt(64.W)
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
    Valid(Bits((afeParams.mbLanes * afeParams.mbSerializerRatio).W))
}

class SidebandLaneIO(
    sbParams: SidebandParams,
) extends Bundle {

  /** Data to transmit on the mainband.
    */
  val txData = Flipped(
    Decoupled(Bits(sbParams.sbNodeMsgWidth.W)),
  )

  val rxData =
    Decoupled(Bits(sbParams.sbNodeMsgWidth.W))
}
