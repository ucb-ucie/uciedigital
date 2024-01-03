package edu.berkeley.cs.ucie.digital
package tilelink

import chisel3._
import freechips.rocketchip.util._
import chisel3.util._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import org.chipsalliance.cde.config.{Field, Config, Parameters}
import freechips.rocketchip.subsystem.{BaseSubsystem, CacheBlockBytes}
import freechips.rocketchip.regmapper.{HasRegMap, RegField}

class UCIConfigRF(val beatBytes: Int, val address: BigInt)(implicit p: Parameters)
    extends LazyModule {
  val device = new SimpleDevice(s"ucie-regs", Nil)
  val node = TLRegisterNode(
    Seq(AddressSet(address, 4096 - 1)),
    device,
    "reg/control",
    beatBytes = beatBytes,
  )

  lazy val module = new Impl
  class Impl extends LazyModuleImp(this) {}
}
