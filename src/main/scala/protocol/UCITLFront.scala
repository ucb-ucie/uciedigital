package ucie.tlfront

import chisel3._
import chisel3.util._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import org.chipsalliance.cde.config.Parameters
import freechips.rocketchip.util._
import freechips.rocketchip.prci._
import freechips.rocketchip.subsystem._

class UCITLFront (val params: ProtocolLayerParams)(implicit p: Parameters) extends LazyModule {

    lazy val module = UCITLFrontImp(this)
}

class UCITLFrontImp (outer: UCITLFront) extends LazyModuleImp(outer) {

}
