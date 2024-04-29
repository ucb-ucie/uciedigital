package edu.berkeley.cs.ucie.digital
package e2e

import chisel3._
import chisel3.util._
import chiseltest._
import org.chipsalliance.cde.config.{Field, Parameters, Config}
import freechips.rocketchip.tilelink._
import freechips.rocketchip.util._
import freechips.rocketchip.diplomacy._
import chisel3.experimental.BundleLiterals._

// import freechips.rocketchip.unittest._
import org.scalatest.flatspec.AnyFlatSpec
import tilelink._
import sideband.{SidebandParams}
import logphy.{LinkTrainingParams}
import interfaces._
import protocol.{ProtocolLayerParams}

class AfeLoopbackTester (implicit p: Parameters) extends LazyModule {
    val protoParams = ProtocolLayerParams()
    val tlParams = TileLinkParams(address=0x0, addressRange=0xffff, configAddress=0x40000, inwardQueueDepth=8, outwardQueueDepth=8)
    val fdiParams = FdiParams(width=64, dllpWidth=64, sbWidth=32)
    val rdiParams = RdiParams(width=64, sbWidth=32)
    val sbParams = SidebandParams()
    val myId = 1
    val linkTrainingParams = LinkTrainingParams()
    val afeParams = AfeParams()
    val laneAsyncQueueParams = AsyncQueueParams()
    val delay = 0.0
    val txns = 10

    val csrfuzz = LazyModule(new TLFuzzer(txns))
    val fuzz = LazyModule(new TLFuzzer(txns))
    val tlUcieDie1 = LazyModule(new UCITLFront(tlParams=tlParams,
                                protoParams=protoParams, fdiParams=fdiParams,
                                rdiParams = rdiParams,
                                sbParams = sbParams,
                                myId = myId,
                                linkTrainingParams = linkTrainingParams,
                                afeParams = afeParams,
                                laneAsyncQueueParams = laneAsyncQueueParams))
    val ram  = LazyModule(new TLRAM(AddressSet(tlParams.ADDRESS, tlParams.addressRange), beatBytes=tlParams.BEAT_BYTES))

    // CSR node
    tlUcieDie1.regNode.node := csrfuzz.node
    // connect data nodes
    tlUcieDie1.managerNode := fuzz.node
    ram.node := tlUcieDie1.clientNode
    lazy val module = new Impl
    class Impl extends LazyModuleImp(this) {
        val io = IO(new Bundle {
            val finished = Output(Bool())
        })
        // connect IOs
        io.finished := fuzz.module.io.finished
        val AfeLoopback = Module(new AfeLoopback(afeParams))
        // inputs to tlUcieDie1
        tlUcieDie1.module.io.mbAfe <> AfeLoopback.io.mbAfe
        tlUcieDie1.module.io.sbAfe <> AfeLoopback.io.sbAfe
    }
}
class AfeLoopbackTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "AfeLoopback"
    val txns = 2
    val timeout = 1000
    implicit val p: Parameters = Parameters.empty
    it should "finish request and response before timeout" in {
        test(LazyModule(new AfeLoopbackTester()).module).withAnnotations(Seq(VcsBackendAnnotation, WriteVcdAnnotation)) {c => //.withAnnotations(Seq(VcsBackendAnnotation, WriteVcdAnnotation))
            println("start Afe Loopback Test")
            c.clock.setTimeout(timeout+10)
            c.clock.step(timeout)
            c.io.finished.expect(true.B)
            println("Afe Loopback Test finished? " + c.io.finished.peek())
        }
    }
}
