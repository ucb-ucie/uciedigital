package edu.berkeley.cs.ucie.digital

import chisel3._
import chisel3.util._
import chiseltest._
import org.chipsalliance.cde.config.{Field, Parameters, Config}
import freechips.rocketchip.tilelink._
import freechips.rocketchip.util._
import freechips.rocketchip.diplomacy._
// import freechips.rocketchip.unittest._
import edu.berkeley.cs.ucie.digital.interfaces.FdiParams
import org.scalatest.flatspec.AnyFlatSpec
import tilelink._
import protocol._

class FdiLoopbackTester (implicit p: Parameters) extends LazyModule {
    val fdiParams = FdiParams(width=128, dllpWidth=128, sbWidth=64)
    val protoParams = ProtocolLayerParams()
    val tlParams = TileLinkParams(address=0x0, addressRange=0xffffff, configAddress=0x0, inwardQueueDepth=8, outwardQueueDepth=8)
    val delay = 0.0
    val txns = 100

    val fuzz = LazyModule(new TLFuzzer(txns))
    val tlUcieDie1 = LazyModule(new UCITLFront(tlParams=tlParams,
                                protoParams=protoParams, fdiParams=fdiParams))
    val fdiLoopback = LazyModule(new FdiLoopback(fdiParams))
    // val tlUcieDie2 = LazyModule()
    // val ram = LazyModule(new TLRAM(AddressSet(0x800, 0x7ff)))

    // connect nodes
    tlUcieDie1.managerNode := fuzz.node
    fdiLoopback.ram.node := tlUcieDie1.clientNode
    lazy val module = new Impl
    class Impl extends LazyModuleImp(this) {
        val io = IO(new Bundle {
            val finished = Output(Bool())
        })
        // connect IOs
        io.finished := fuzz.module.io.finished

        // inputs to tlUcieDie1
        tlUcieDie1.module.io.fdi.plData              := fdiLoopback.module.io.fdi1.plData
        tlUcieDie1.module.io.fdi.plRetimerCrd        := fdiLoopback.module.io.fdi1.plRetimerCrd
        // why split valid and bits for dllp
        tlUcieDie1.module.io.fdi.plDllp              := fdiLoopback.module.io.fdi1.plDllp
        tlUcieDie1.module.io.fdi.plDllpOfc           := fdiLoopback.module.io.fdi1.plDllpOfc
        // plStream.protoType here
        tlUcieDie1.module.io.fdi.plStream            := fdiLoopback.module.io.fdi1.plStream
        tlUcieDie1.module.io.fdi.plFlitCancel        := fdiLoopback.module.io.fdi1.plFlitCancel
        tlUcieDie1.module.io.fdi.plStateStatus       := fdiLoopback.module.io.fdi1.plStateStatus
        tlUcieDie1.module.io.fdi.plInbandPres        := fdiLoopback.module.io.fdi1.plInbandPres
        tlUcieDie1.module.io.fdi.plError             := fdiLoopback.module.io.fdi1.plError
        tlUcieDie1.module.io.fdi.plCerror            := fdiLoopback.module.io.fdi1.plCerror
        tlUcieDie1.module.io.fdi.plNfError           := fdiLoopback.module.io.fdi1.plNfError
        tlUcieDie1.module.io.fdi.plTrainError        := fdiLoopback.module.io.fdi1.plTrainError
        tlUcieDie1.module.io.fdi.plRxActiveReq       := fdiLoopback.module.io.fdi1.plRxActiveReq
        tlUcieDie1.module.io.fdi.plProtocol          := fdiLoopback.module.io.fdi1.plProtocol
        tlUcieDie1.module.io.fdi.plProtocolFlitFormat:= fdiLoopback.module.io.fdi1.plProtocolFlitFormat
        tlUcieDie1.module.io.fdi.plProtocolValid     := fdiLoopback.module.io.fdi1.plProtocolValid
        tlUcieDie1.module.io.fdi.plStallReq          := fdiLoopback.module.io.fdi1.plStallReq
        tlUcieDie1.module.io.fdi.plPhyInRecenter     := fdiLoopback.module.io.fdi1.plPhyInRecenter
        tlUcieDie1.module.io.fdi.plPhyInL1           := fdiLoopback.module.io.fdi1.plPhyInL1
        tlUcieDie1.module.io.fdi.plPhyInL2           := fdiLoopback.module.io.fdi1.plPhyInL2
        tlUcieDie1.module.io.fdi.plSpeedMode         := fdiLoopback.module.io.fdi1.plSpeedMode
        tlUcieDie1.module.io.fdi.plLinkWidth         := fdiLoopback.module.io.fdi1.plLinkWidth
        tlUcieDie1.module.io.fdi.plClkReq            := fdiLoopback.module.io.fdi1.plClkReq
        tlUcieDie1.module.io.fdi.plWakeAck           := fdiLoopback.module.io.fdi1.plWakeAck
        // split valids and bits again
        tlUcieDie1.module.io.fdi.plConfig            := fdiLoopback.module.io.fdi1.plConfig
        tlUcieDie1.module.io.fdi.plConfigCredit      := fdiLoopback.module.io.fdi1.plConfigCredit

        fdiLoopback.module.io.fdi1.lpData          := tlUcieDie1.module.io.fdi.lpData
        fdiLoopback.module.io.fdi1.lpRetimerCrd    := tlUcieDie1.module.io.fdi.lpRetimerCrd
        fdiLoopback.module.io.fdi1.lpCorruptCrc    := tlUcieDie1.module.io.fdi.lpCorruptCrc
        fdiLoopback.module.io.fdi1.lpDllp          := tlUcieDie1.module.io.fdi.lpDllp
        fdiLoopback.module.io.fdi1.lpDllpOfc       := tlUcieDie1.module.io.fdi.lpDllpOfc
        fdiLoopback.module.io.fdi1.lpStream        := tlUcieDie1.module.io.fdi.lpStream
        fdiLoopback.module.io.fdi1.lpStateReq      := tlUcieDie1.module.io.fdi.lpStateReq
        fdiLoopback.module.io.fdi1.lpLinkError     := tlUcieDie1.module.io.fdi.lpLinkError
        fdiLoopback.module.io.fdi1.lpRxActiveStatus:= tlUcieDie1.module.io.fdi.lpRxActiveStatus
        fdiLoopback.module.io.fdi1.lpStallAck      := tlUcieDie1.module.io.fdi.lpStallAck
        fdiLoopback.module.io.fdi1.lpClkAck        := tlUcieDie1.module.io.fdi.lpClkAck
        fdiLoopback.module.io.fdi1.lpWakeReq       := tlUcieDie1.module.io.fdi.lpWakeReq
        fdiLoopback.module.io.fdi1.lpConfig        := tlUcieDie1.module.io.fdi.lpConfig
        fdiLoopback.module.io.fdi1.lpConfigCredit  := tlUcieDie1.module.io.fdi.lpConfigCredit
    }
}
class FdiLoopbackTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "FdiLoopback"
    val txns = 5
    val timeout = 2000
    implicit val p: Parameters = Parameters.empty
    it should "finish request and response before timeout" in {
        test(LazyModule(new FdiLoopbackTester()).module) {c =>
            println("start Fdi Loopback Test")
            c.clock.setTimeout(timeout+10)
            c.clock.step(timeout)
            c.io.finished.expect(true.B)
            println("Fdi Loopback Test finished? " + c.io.finished.peek())
        }
    }
}
