package edu.berkeley.cs.ucie.digital
package logphy

import interfaces._

import chisel3._

class LogicalPhy(
    afeParams: AfeParams,
    rdiParams: RdiParams,
) extends Module {
  val io = IO(new Bundle {
    val rdi = Flipped(new Rdi(rdiParams))
    val mbAfe = new MainbandAfeIo(afeParams)
    val sbAfe = new SidebandAfeIo(afeParams)
  })
}
