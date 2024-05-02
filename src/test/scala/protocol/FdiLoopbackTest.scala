package edu.berkeley.cs.ucie.digital
package protocol

import chisel3._
import chisel3.util._
import chiseltest._
import org.chipsalliance.cde.config.{Field, Parameters, Config}
import freechips.rocketchip.tilelink._
import freechips.rocketchip.util._
import freechips.rocketchip.prci._
import freechips.rocketchip.diplomacy._
// import freechips.rocketchip.unittest._
import edu.berkeley.cs.ucie.digital.interfaces.FdiParams
import org.scalatest.flatspec.AnyFlatSpec
import tilelink._
//import protocol._

class FdiLoopbackTester (implicit p: Parameters) extends LazyModule {
    val fdiParams = FdiParams(width=64, dllpWidth=64, sbWidth=32)
    val protoParams = ProtocolLayerParams()
    val tlParams = TileLinkParams(address=0x0, addressRange=0xffff, configAddress=0x40000, inwardQueueDepth=8, outwardQueueDepth=8)
    val delay = 0.0
    val txns = 10

    // Create clock source
    val clockSourceNode = ClockSourceNode(Seq(ClockSourceParameters()))

    val csrfuzz = LazyModule(new TLFuzzer(txns))
    val fuzz = LazyModule(new TLFuzzer(txns))
    val tlUcieDie1 = LazyModule(new UCITLFront(tlParams=tlParams,
                                protoParams=protoParams, fdiParams=fdiParams))
    val ram  = LazyModule(new TLRAM(AddressSet(tlParams.ADDRESS, tlParams.addressRange), beatBytes=tlParams.BEAT_BYTES))
    // val fdiLoopback = LazyModule(new FdiLoopback(fdiParams))
    // val tlUcieDie2 = LazyModule()

    tlUcieDie1.clockNode := clockSourceNode
    // CSR node
    tlUcieDie1.regNode.node := csrfuzz.node
    // connect data nodes
    tlUcieDie1.managerNode := TLSourceShrinker(tlParams.sourceIDWidth) := fuzz.node
    ram.node := tlUcieDie1.clientNode
    // fdiLoopback.ram.node := tlUcieDie1.clientNode
    lazy val module = new Impl
    class Impl extends LazyModuleImp(this) {
        val io = IO(new Bundle {
            val uci_clock = Input(new ClockBundle(ClockBundleParameters()))
            val finished = Output(Bool())
        })
        // connect IOs
        io.finished := fuzz.module.io.finished
        val fdiLoopback = Module(new FdiLoopback(fdiParams))
        io.uci_clock <> clockSourceNode.out(0)._1
        // inputs to tlUcieDie1
        tlUcieDie1.module.io.fdi.lpData.ready        := fdiLoopback.io.fdi1.lpData.ready
        tlUcieDie1.module.io.fdi.plData              := fdiLoopback.io.fdi1.plData
        tlUcieDie1.module.io.fdi.plRetimerCrd        := fdiLoopback.io.fdi1.plRetimerCrd
        // why split valid and bits for dllp
        tlUcieDie1.module.io.fdi.plDllp              := fdiLoopback.io.fdi1.plDllp
        tlUcieDie1.module.io.fdi.plDllpOfc           := fdiLoopback.io.fdi1.plDllpOfc
        // plStream.protoType here
        tlUcieDie1.module.io.fdi.plStream            := fdiLoopback.io.fdi1.plStream
        tlUcieDie1.module.io.fdi.plFlitCancel        := fdiLoopback.io.fdi1.plFlitCancel
        tlUcieDie1.module.io.fdi.plStateStatus       := fdiLoopback.io.fdi1.plStateStatus
        tlUcieDie1.module.io.fdi.plInbandPres        := fdiLoopback.io.fdi1.plInbandPres
        tlUcieDie1.module.io.fdi.plError             := fdiLoopback.io.fdi1.plError
        tlUcieDie1.module.io.fdi.plCerror            := fdiLoopback.io.fdi1.plCerror
        tlUcieDie1.module.io.fdi.plNfError           := fdiLoopback.io.fdi1.plNfError
        tlUcieDie1.module.io.fdi.plTrainError        := fdiLoopback.io.fdi1.plTrainError
        tlUcieDie1.module.io.fdi.plRxActiveReq       := fdiLoopback.io.fdi1.plRxActiveReq
        tlUcieDie1.module.io.fdi.plProtocol          := fdiLoopback.io.fdi1.plProtocol
        tlUcieDie1.module.io.fdi.plProtocolFlitFormat:= fdiLoopback.io.fdi1.plProtocolFlitFormat
        tlUcieDie1.module.io.fdi.plProtocolValid     := fdiLoopback.io.fdi1.plProtocolValid
        tlUcieDie1.module.io.fdi.plStallReq          := fdiLoopback.io.fdi1.plStallReq
        tlUcieDie1.module.io.fdi.plPhyInRecenter     := fdiLoopback.io.fdi1.plPhyInRecenter
        tlUcieDie1.module.io.fdi.plPhyInL1           := fdiLoopback.io.fdi1.plPhyInL1
        tlUcieDie1.module.io.fdi.plPhyInL2           := fdiLoopback.io.fdi1.plPhyInL2
        tlUcieDie1.module.io.fdi.plSpeedMode         := fdiLoopback.io.fdi1.plSpeedMode
        tlUcieDie1.module.io.fdi.plLinkWidth         := fdiLoopback.io.fdi1.plLinkWidth
        tlUcieDie1.module.io.fdi.plClkReq            := fdiLoopback.io.fdi1.plClkReq
        tlUcieDie1.module.io.fdi.plWakeAck           := fdiLoopback.io.fdi1.plWakeAck
        // split valids and bits again
        tlUcieDie1.module.io.fdi.plConfig            := fdiLoopback.io.fdi1.plConfig
        fdiLoopback.io.fdi1.plConfigCredit           := tlUcieDie1.module.io.fdi.plConfigCredit

        fdiLoopback.io.fdi1.lpData.valid    := tlUcieDie1.module.io.fdi.lpData.valid
        fdiLoopback.io.fdi1.lpData.bits     := tlUcieDie1.module.io.fdi.lpData.bits
        fdiLoopback.io.fdi1.lpData.irdy     := tlUcieDie1.module.io.fdi.lpData.irdy //true.B // TODO: check a better way of doing this
        fdiLoopback.io.fdi1.lpRetimerCrd    := tlUcieDie1.module.io.fdi.lpRetimerCrd
        fdiLoopback.io.fdi1.lpCorruptCrc    := tlUcieDie1.module.io.fdi.lpCorruptCrc
        fdiLoopback.io.fdi1.lpDllp          := tlUcieDie1.module.io.fdi.lpDllp
        fdiLoopback.io.fdi1.lpDllpOfc       := tlUcieDie1.module.io.fdi.lpDllpOfc
        fdiLoopback.io.fdi1.lpStream        := tlUcieDie1.module.io.fdi.lpStream
        fdiLoopback.io.fdi1.lpStateReq      := tlUcieDie1.module.io.fdi.lpStateReq
        fdiLoopback.io.fdi1.lpLinkError     := tlUcieDie1.module.io.fdi.lpLinkError
        fdiLoopback.io.fdi1.lpRxActiveStatus:= tlUcieDie1.module.io.fdi.lpRxActiveStatus
        fdiLoopback.io.fdi1.lpStallAck      := tlUcieDie1.module.io.fdi.lpStallAck
        fdiLoopback.io.fdi1.lpClkAck        := tlUcieDie1.module.io.fdi.lpClkAck
        fdiLoopback.io.fdi1.lpWakeReq       := tlUcieDie1.module.io.fdi.lpWakeReq
        fdiLoopback.io.fdi1.lpConfig        := tlUcieDie1.module.io.fdi.lpConfig
        tlUcieDie1.module.io.fdi.lpConfigCredit := fdiLoopback.io.fdi1.lpConfigCredit  
    }
}

class FDITLTestHarness(implicit val p: Parameters) extends Module with HasSuccessIO {
  val tester = Module(LazyModule(new FdiLoopbackTester).module)
  tester.io.uci_clock.clock := clock
  tester.io.uci_clock.reset := reset
  io.success := tester.io.finished

  // Dummy plusarg to avoid breaking verilator builds with emulator.cc
  val useless_plusarg = PlusArg("useless_plusarg", width=1)
  dontTouch(useless_plusarg)
  ElaborationArtefacts.add("plusArgs", PlusArgArtefacts.serialize_cHeader)
}

class FdiLoopbackTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "FdiLoopback"
    val txns = 2
    val timeout = 1000
    implicit val p: Parameters = Parameters.empty
    it should "finish request and response before timeout" in {
        test(new FDITLTestHarness()).withAnnotations(Seq(VcsBackendAnnotation, WriteVcdAnnotation)) {c => //.withAnnotations(Seq(VcsBackendAnnotation, WriteVcdAnnotation))
            println("start Fdi Loopback Test")
            c.clock.setTimeout(timeout+10)
            c.clock.step(timeout)
            c.io.success.expect(true.B)
            println("Fdi Loopback Test finished? " + c.io.success.peek())
        }
    }
}
