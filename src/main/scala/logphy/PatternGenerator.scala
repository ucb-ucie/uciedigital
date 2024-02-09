package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._
import edu.berkeley.cs.ucie.digital.interfaces._

class PatternGeneratorIO(
    afeParams: AfeParams,
) extends Bundle {
  val transmitPattern = Flipped(
    Decoupled(TransmitPattern()),
  ) // data to transmit & receive over SB
  val transmitPatternStatus = Decoupled(SBMsgExchangeStatus())
  val mainbandTxData = Decoupled(
    Bits((afeParams.mbLanes * afeParams.mbSerializerRatio).W),
  )
  val sidebandTxData = Decoupled(Bits((afeParams.sbSerializerRatio).W))
}

class PatternGenerator(
    afeParams: AfeParams,
) extends Module {
  val io = IO(new Bundle {
    val patternGeneratorIO = new PatternGeneratorIO(afeParams)
  })
}
