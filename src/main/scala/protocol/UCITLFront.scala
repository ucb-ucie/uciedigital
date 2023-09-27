package ucie.tlfront

import chisel3._
import chisel3.util._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import org.chipsalliance.cde.config.Parameters
import freechips.rocketchip.util._
import freechips.rocketchip.prci._
import freechips.rocketchip.subsystem._

import ucie.d2dadapter._

class UCITLFront(
    val d2dParams: D2DAdapterParams,
    val params: ProtocolLayerParams,
)(implicit p: Parameters)
    extends LazyModule {

  lazy val module = UCITLFrontImp(this)
}

class UCITLFrontImp(outer: UCITLFront) extends LazyModuleImp(outer) {
  val io = IO(new Bundle {
    val lclk = Input(Clock())
    val fdi = new Fdi(d2dParams)
  })
}
