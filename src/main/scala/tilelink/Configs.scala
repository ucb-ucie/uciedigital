package edu.berkeley.cs.ucie.digital
package tilelink

import chisel3._
import freechips.rocketchip.util._
import chisel3.util._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import org.chipsalliance.cde.config.{Field, Config, Parameters}
import freechips.rocketchip.subsystem._
//import freechips.rocketchip.subsystem.{BaseSubsystem, CacheBlockBytes}
import freechips.rocketchip.regmapper.{HasRegMap, RegField}
import interfaces._
import protocol._

case class UCITLParams (
  val fdi: FdiParams,
  val proto: ProtocolLayerParams, 
  val tl: TileLinkParams
)

case object UCITLKey extends Field[Option[UCITLParams]](None)

trait CanHaveTLUCIAdapter { this: BaseSubsystem =>
  p(UCITLKey).map { params =>
    val bus = locateTLBusWrapper(SBUS) //TODO: make parameterizable?
    val uciTL = LazyModule(new UCITLFront(
      tlParams    = params.tl,
      protoParams = params.proto,
      fdiParams   = params.fdi
    ))
    uciTL.clockNode := bus.fixedClockNode
    bus.coupleTo(s"ucie_tl_man_port") { 
        uciTL.managerNode := TLWidthWidget(bus.beatBytes) := TLSourceShrinker(params.tl.sourceIDWidth) := TLFragmenter(bus.beatBytes, p(CacheBlockBytes)) := _ 
    } //manager node because SBUS is making request?
    bus.coupleFrom(s"ucie_tl_cl_port") { _ := TLWidthWidget(bus.beatBytes) := uciTL.clientNode }
    bus.coupleTo(s"ucie_tl_ctrl_port") { uciTL.regNode.node := TLWidthWidget(bus.beatBytes) := TLFragmenter(bus.beatBytes, bus.blockBytes) := _ }
  }
}

class WithUCITLAdapter(params: UCITLParams) extends Config((site, here, up) => {
  case UCITLKey => Some(params)
})