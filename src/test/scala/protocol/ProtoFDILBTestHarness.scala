package edu.berkeley.cs.ucie.digital
package protocol

import chisel3._
import chisel3.util._
import chisel3.util.random.LFSR

import freechips.rocketchip.diplomacy._
import org.chipsalliance.cde.config.{Field, Parameters, Config}
import freechips.rocketchip.tilelink._
import freechips.rocketchip.util._
import scala.collection.immutable.ListMap
import e2e._
import tilelink._
import interfaces._
import sideband.{SidebandParams}
import logphy.{LinkTrainingParams}

trait HasSuccessIO { this: Module =>
  val io = IO(new Bundle {
    val success = Output(Bool())
  })
}

case class ProtoLBTesterParams(
  protoParams: ProtocolLayerParams, 
  tlParams: TileLinkParams,
  fdiParams: FdiParams,
  rdiParams: RdiParams,
  sbParams: SidebandParams, 
  myId: BigInt,
  linkTrainingParams: LinkTrainingParams,
  afeParams: AfeParams,
  laneAsyncQueueParams: AsyncQueueParams,
  delay: Double = 0.0,
  txns: Int = 100
)

case object ProtoLBTesterKey extends Field[ProtoLBTesterParams]

class ProtoFDILBTester(implicit p: Parameters) extends LazyModule {
  val tParams = p(ProtoLBTesterKey)
  val txns = tParams.txns
  
  val tlUcieDie1 = LazyModule(new UCITLFront(tlParams = tParams.tlParams,
                          protoParams = tParams.protoParams, 
                          fdiParams = tParams.fdiParams,
                          rdiParams = tParams.rdiParams,
                          sbParams = tParams.sbParams,
                          myId = tParams.myId,
                          linkTrainingParams = tParams.linkTrainingParams,
                          afeParams = tParams.afeParams,
                          laneAsyncQueueParams = tParams.laneAsyncQueueParams))

  val fdiLoopback = Module(new ProtoFDILoopback(fdiParams = tParams.fdiParams, latency = 8))

  val tlUcieDie2 = LazyModule(new UCITLFront(tlParams = tParams.tlParams, 
                          protoParams = tParams.protoParams, 
                          fdiParams = tParams.fdiParams,
                          rdiParams = tParams.rdiParams,
                          sbParams = tParams.sbParams,
                          myId = tParams.myId,
                          linkTrainingParams = tParams.linkTrainingParams,
                          afeParams = tParams.afeParams,
                          laneAsyncQueueParams = tParams.laneAsyncQueueParams))

  //tlUcieDie1.node := TLDelayer(tParams.delay) := fuzzm.node
  // Connect Die 1's FDI lp signals to the Loopback module's FDI lp signals
  fdiLoopback.io.fdi.lpRetimerCrd     := tlUcieDie1.module.ucietop.io.fdi.lpRetimerCrd
  fdiLoopback.io.fdi.lpCorruptCrc     := tlUcieDie1.module.ucietop.io.fdi.lpCorruptCrc
  fdiLoopback.io.fdi.lpDllp           := tlUcieDie1.module.ucietop.io.fdi.lpDllp
  fdiLoopback.io.fdi.lpDllpOfc        := tlUcieDie1.module.ucietop.io.fdi.lpDllpOfc
  fdiLoopback.io.fdi.lpStream         := tlUcieDie1.module.ucietop.io.fdi.lpStream
  fdiLoopback.io.fdi.lpStateReq       := tlUcieDie1.module.ucietop.io.fdi.lpStateReq
  fdiLoopback.io.fdi.lpLinkError      := tlUcieDie1.module.ucietop.io.fdi.lpLinkError
  fdiLoopback.io.fdi.lpRxActiveStatus := tlUcieDie1.module.ucietop.io.fdi.lpRxActiveStatus
  fdiLoopback.io.fdi.lpStallAck       := tlUcieDie1.module.ucietop.io.fdi.lpStallAck
  fdiLoopback.io.fdi.lpClkAck         := tlUcieDie1.module.ucietop.io.fdi.lpClkAck
  fdiLoopback.io.fdi.lpWakeReq        := tlUcieDie1.module.ucietop.io.fdi.lpWakeReq
  fdiLoopback.io.fdi.lpConfig         := tlUcieDie1.module.ucietop.io.fdi.lpConfig
  fdiLoopback.io.fdi.lpConfigCredit   := tlUcieDie1.module.ucietop.io.fdi.lpConfigCredit

  // Connect Die 2's FDI pl signals to the Loopback module's FDI lp signals
  tlUcieDie2.module.ucietop.io.fdi.plRetimerCrd         := fdiLoopback.io.fdi.plRetimerCrd
  tlUcieDie2.module.ucietop.io.fdi.plDllp.valid         := fdiLoopback.io.fdi.plDllp.valid
  tlUcieDie2.module.ucietop.io.fdi.plDllp.bits          := fdiLoopback.io.fdi.plDllp.bits
  tlUcieDie2.module.ucietop.io.fdi.plDllpOfc            := fdiLoopback.io.fdi.plDllpOfc
  tlUcieDie2.module.ucietop.io.fdi.plStream.protoType   := fdiLoopback.io.fdi.plStream.protoType
  tlUcieDie2.module.ucietop.io.fdi.plFlitCancel         := fdiLoopback.io.fdi.plFlitCancel
  tlUcieDie2.module.ucietop.io.fdi.plStateStatus        := fdiLoopback.io.fdi.plStateStatus
  tlUcieDie2.module.ucietop.io.fdi.plInbandPres         := fdiLoopback.io.fdi.plInbandPres
  tlUcieDie2.module.ucietop.io.fdi.plError              := fdiLoopback.io.fdi.plError
  tlUcieDie2.module.ucietop.io.fdi.plCerror             := fdiLoopback.io.fdi.plCerror
  tlUcieDie2.module.ucietop.io.fdi.plNfError            := fdiLoopback.io.fdi.plNfError
  tlUcieDie2.module.ucietop.io.fdi.plTrainError         := fdiLoopback.io.fdi.plTrainError
  tlUcieDie2.module.ucietop.io.fdi.plRxActiveReq        := fdiLoopback.io.fdi.plRxActiveReq 
  tlUcieDie2.module.ucietop.io.fdi.plProtocol           := fdiLoopback.io.fdi.plProtocol
  tlUcieDie2.module.ucietop.io.fdi.plProtocolFlitFormat := fdiLoopback.io.fdi.plProtocolFlitFormat
  tlUcieDie2.module.ucietop.io.fdi.plProtocolValid      := fdiLoopback.io.fdi.plProtocolValid
  tlUcieDie2.module.ucietop.io.fdi.plStallReq           := fdiLoopback.io.fdi.plStallReq 
  tlUcieDie2.module.ucietop.io.fdi.plPhyInRecenter      := fdiLoopback.io.fdi.plPhyInRecenter
  tlUcieDie2.module.ucietop.io.fdi.plPhyInL1            := fdiLoopback.io.fdi.plPhyInL1
  tlUcieDie2.module.ucietop.io.fdi.plPhyInL2            := fdiLoopback.io.fdi.plPhyInL2
  tlUcieDie2.module.ucietop.io.fdi.plSpeedMode          := fdiLoopback.io.fdi.plSpeedMode
  tlUcieDie2.module.ucietop.io.fdi.plLinkWidth          := fdiLoopback.io.fdi.plLinkWidth 
  tlUcieDie2.module.ucietop.io.fdi.plClkReq             := fdiLoopback.io.fdi.plClkReq 
  tlUcieDie2.module.ucietop.io.fdi.plWakeAck            := fdiLoopback.io.fdi.plWakeAck
  tlUcieDie2.module.ucietop.io.fdi.plConfig.valid       := fdiLoopback.io.fdi.plConfig.valid
  tlUcieDie2.module.ucietop.io.fdi.plConfig.bits        := fdiLoopback.io.fdi.plConfig.bits
  tlUcieDie2.module.ucietop.io.fdi.plConfigCredit       := fdiLoopback.io.fdi.plConfigCredit

  lazy val module = new Impl
  class Impl extends LazyModuleImp(this) {
    val io = IO(new Bundle {
      val finished = Output(Bool())
    })
    io.finished := false.B
  }
}

class ProtoFDILBTestHarness(implicit val p: Parameters) extends Module with HasSuccessIO {
  val tester = Module(LazyModule(new ProtoFDILBTester).module)
  io.success := tester.io.finished

  // Dummy plusarg to avoid breaking verilator builds with emulator.cc
  val useless_plusarg = PlusArg("useless_plusarg", width=1)
  dontTouch(useless_plusarg)
  ElaborationArtefacts.add("plusArgs", PlusArgArtefacts.serialize_cHeader)
}