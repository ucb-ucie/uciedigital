package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._

class PatternGeneratorIO(
) extends Bundle {
  val transmitPattern = Flipped(
    Decoupled(TransmitPattern()),
  ) // data to transmit & receive over SB
  val transmitPatternStatus = Decoupled(SBMsgExchangeStatus())


}

class PatternGenerator extends Module {
  val io = IO(new Bundle {
    val patternGeneratorIO = new PatternGeneratorIO()
  })
}
