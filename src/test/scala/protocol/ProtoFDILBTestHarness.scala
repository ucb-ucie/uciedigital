package edu.berkeley.cs.ucie.digital
package protocol

import chisel3._
import chisel3.util._
import chisel3.util.random.LFSR

import freechips.rocketchip.diplomacy._
import org.chipsalliance.cde.config.{Field, Parameters, Config}
import freechips.rocketchip.tilelink._
import freechips.rocketchip.util._
import freechips.rocketchip.prci._
import scala.collection.immutable.ListMap
import tilelink._
import interfaces._
import protocol._

trait HasSuccessIO { this: Module =>
  val io = IO(new Bundle {
    val success = Output(Bool())
  })
}

case class ProtoLBTesterParams(
  fdi: FdiParams,
  proto: ProtocolLayerParams, 
  tl: TileLinkParams,
  delay: Double = 0.0,
  txns: Int = 100
)

case object ProtoLBTesterKey extends Field[ProtoLBTesterParams]

class ProtoFDILBTester(implicit p: Parameters) extends LazyModule {
  val tParams = p(ProtoLBTesterKey)
  val txns = tParams.txns

  // Create clock source
  val clockSourceNode1 = ClockSourceNode(Seq(ClockSourceParameters()))
  val clockSourceNode2 = ClockSourceNode(Seq(ClockSourceParameters()))
  
  val tlUcieDie1 = LazyModule(new UCITLFront(tlParams = tParams.tl, 
                          protoParams = tParams.proto, 
                          fdiParams = tParams.fdi))

  val fdiLoopback = Module(new ProtoFDILoopback(fdiParams = tParams.fdi, latency = 8))

  val tlUcieDie2 = LazyModule(new UCITLFront(tlParams = tParams.tl, 
                          protoParams = tParams.proto, 
                          fdiParams = tParams.fdi))

  //tlUcieDie1.node := TLDelayer(tParams.delay) := fuzzm.node
  // Connect Die 1's FDI lp signals to the Loopback module's FDI lp signals
  fdiLoopback.io.fdi.lpRetimerCrd     := tlUcieDie1.module.io.fdi.lpRetimerCrd
  fdiLoopback.io.fdi.lpCorruptCrc     := tlUcieDie1.module.io.fdi.lpCorruptCrc
  fdiLoopback.io.fdi.lpDllp           := tlUcieDie1.module.io.fdi.lpDllp
  fdiLoopback.io.fdi.lpDllpOfc        := tlUcieDie1.module.io.fdi.lpDllpOfc
  fdiLoopback.io.fdi.lpStream         := tlUcieDie1.module.io.fdi.lpStream
  fdiLoopback.io.fdi.lpStateReq       := tlUcieDie1.module.io.fdi.lpStateReq
  fdiLoopback.io.fdi.lpLinkError      := tlUcieDie1.module.io.fdi.lpLinkError
  fdiLoopback.io.fdi.lpRxActiveStatus := tlUcieDie1.module.io.fdi.lpRxActiveStatus
  fdiLoopback.io.fdi.lpStallAck       := tlUcieDie1.module.io.fdi.lpStallAck
  fdiLoopback.io.fdi.lpClkAck         := tlUcieDie1.module.io.fdi.lpClkAck
  fdiLoopback.io.fdi.lpWakeReq        := tlUcieDie1.module.io.fdi.lpWakeReq
  fdiLoopback.io.fdi.lpConfig         := tlUcieDie1.module.io.fdi.lpConfig
  fdiLoopback.io.fdi.lpConfigCredit   := tlUcieDie1.module.io.fdi.lpConfigCredit
  tlUcieDie1.clockNode := clockSourceNode1

  // Connect Die 2's FDI pl signals to the Loopback module's FDI lp signals
  tlUcieDie2.module.io.fdi.plRetimerCrd         := fdiLoopback.io.fdi.plRetimerCrd
  tlUcieDie2.module.io.fdi.plDllp.valid         := fdiLoopback.io.fdi.plDllp.valid
  tlUcieDie2.module.io.fdi.plDllp.bits          := fdiLoopback.io.fdi.plDllp.bits
  tlUcieDie2.module.io.fdi.plDllpOfc            := fdiLoopback.io.fdi.plDllpOfc
  tlUcieDie2.module.io.fdi.plStream.protoType   := fdiLoopback.io.fdi.plStream.protoType
  tlUcieDie2.module.io.fdi.plFlitCancel         := fdiLoopback.io.fdi.plFlitCancel
  tlUcieDie2.module.io.fdi.plStateStatus        := fdiLoopback.io.fdi.plStateStatus
  tlUcieDie2.module.io.fdi.plInbandPres         := fdiLoopback.io.fdi.plInbandPres
  tlUcieDie2.module.io.fdi.plError              := fdiLoopback.io.fdi.plError
  tlUcieDie2.module.io.fdi.plCerror             := fdiLoopback.io.fdi.plCerror
  tlUcieDie2.module.io.fdi.plNfError            := fdiLoopback.io.fdi.plNfError
  tlUcieDie2.module.io.fdi.plTrainError         := fdiLoopback.io.fdi.plTrainError
  tlUcieDie2.module.io.fdi.plRxActiveReq        := fdiLoopback.io.fdi.plRxActiveReq 
  tlUcieDie2.module.io.fdi.plProtocol           := fdiLoopback.io.fdi.plProtocol
  tlUcieDie2.module.io.fdi.plProtocolFlitFormat := fdiLoopback.io.fdi.plProtocolFlitFormat
  tlUcieDie2.module.io.fdi.plProtocolValid      := fdiLoopback.io.fdi.plProtocolValid
  tlUcieDie2.module.io.fdi.plStallReq           := fdiLoopback.io.fdi.plStallReq 
  tlUcieDie2.module.io.fdi.plPhyInRecenter      := fdiLoopback.io.fdi.plPhyInRecenter
  tlUcieDie2.module.io.fdi.plPhyInL1            := fdiLoopback.io.fdi.plPhyInL1
  tlUcieDie2.module.io.fdi.plPhyInL2            := fdiLoopback.io.fdi.plPhyInL2
  tlUcieDie2.module.io.fdi.plSpeedMode          := fdiLoopback.io.fdi.plSpeedMode
  tlUcieDie2.module.io.fdi.plLinkWidth          := fdiLoopback.io.fdi.plLinkWidth 
  tlUcieDie2.module.io.fdi.plClkReq             := fdiLoopback.io.fdi.plClkReq 
  tlUcieDie2.module.io.fdi.plWakeAck            := fdiLoopback.io.fdi.plWakeAck
  tlUcieDie2.module.io.fdi.plConfig.valid       := fdiLoopback.io.fdi.plConfig.valid
  tlUcieDie2.module.io.fdi.plConfig.bits        := fdiLoopback.io.fdi.plConfig.bits
  tlUcieDie2.module.io.fdi.plConfigCredit       := fdiLoopback.io.fdi.plConfigCredit
  tlUcieDie2.clockNode := clockSourceNode2

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
