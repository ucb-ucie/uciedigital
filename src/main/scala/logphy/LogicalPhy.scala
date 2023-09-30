package edu.berkeley.cs.ucie.digital.logphy

import chisel3._
import edu.berkeley.cs.ucie.digital.interfaces._

class LogicalPhy(lanes: Int = 16, serializerRatio: Int = 16, width: Int, sbWidth: Int) extends Module {
  val io = IO(new Bundle {
    val rdi = Flipped(new Rdi(width, sbWidth))
    val mbAfe = new MainbandAfeIo(lanes, serializerRatio)
    val sbAfe = new SidebandAfeIo(serializerRatio)
  })
}
