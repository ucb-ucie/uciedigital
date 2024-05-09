package edu.berkeley.cs.ucie.digital
package tilelink

import chisel3._
import freechips.rocketchip.util._
import chisel3.util._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import org.chipsalliance.cde.config.{Field, Config, Parameters}
import freechips.rocketchip.subsystem._
import testchipip.soc.{OBUS}
//import freechips.rocketchip.subsystem.{BaseSubsystem, CacheBlockBytes}
import freechips.rocketchip.regmapper.{HasRegMap, RegField}
import interfaces._
import protocol._
import sideband.{SidebandParams}
import logphy.{LinkTrainingParams}

case class UCITLParams (
  val protoParams: ProtocolLayerParams, 
  val tlParams: TileLinkParams,
  val fdiParams: FdiParams,
  val rdiParams: RdiParams,
  val sbParams: SidebandParams, 
  val linkTrainingParams: LinkTrainingParams,
  val afeParams: AfeParams,
  val laneAsyncQueueParams: AsyncQueueParams
)

case object UCITLKey extends Field[Option[UCITLParams]](None)

trait CanHaveTLUCIAdapter { this: BaseSubsystem =>
  val uciTL = p(UCITLKey) match {
    case Some(params) => {
      val bus = locateTLBusWrapper(SBUS) //TODO: make parameterizable?
      //val ctrlbus = locateTLBusWrapper(SBUS)
      val uciTL = LazyModule(new UCITLFront(
        tlParams    = params.tlParams,
        protoParams = params.protoParams,
        fdiParams   = params.fdiParams,
        rdiParams   = params.rdiParams,
        sbParams    = params.sbParams,
        //myId        = params.myId,
        linkTrainingParams = params.linkTrainingParams,
        afeParams   = params.afeParams,
        laneAsyncQueueParams = params.laneAsyncQueueParams
      ))
      uciTL.clockNode := bus.fixedClockNode
      bus.coupleTo(s"ucie_tl_man_port") { 
          uciTL.managerNode := TLWidthWidget(bus.beatBytes) := TLBuffer() := TLSourceShrinker(params.tlParams.sourceIDWidth) := TLFragmenter(bus.beatBytes, p(CacheBlockBytes)) := _ 
      } //manager node because SBUS is making request?
      bus.coupleFrom(s"ucie_tl_cl_port") { _ := TLWidthWidget(bus.beatBytes) := TLBuffer() := uciTL.clientNode }
      bus.coupleTo(s"ucie_tl_ctrl_port") { uciTL.regNode.node := TLWidthWidget(bus.beatBytes) := TLFragmenter(bus.beatBytes, bus.blockBytes) := _ }
      Some(uciTL)
      }
    case None => None
  }
}

class WithUCITLAdapter(params: UCITLParams) extends Config((site, here, up) => {
  case UCITLKey => Some(params)
})
