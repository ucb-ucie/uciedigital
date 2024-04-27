package edu.berkeley.cs.ucie.digital
package protocol

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import scala.collection.immutable._
import scala.math._

import interfaces._

class ProtocolLayerTest extends AnyFlatSpec with ChiselScalatestTester {
  val protoParams = new ProtocolLayerParams
  val fdiParams = new FdiParams(width = 8, dllpWidth = 8, sbWidth = 32)
  behavior of "Protocol layer"
  it should "test hamming encode and decode" in {
    test(new HammingWrapper(protoParams)){ c => 
        println("Instantiating pattern buffer")
        val tuvok = "hBADDCAFE".U
        val neelix = "hDEADBEEF".U

        println("Test matching transport")
        c.io.tx_data.poke(tuvok)
        c.io.rx_data.poke(tuvok)
        c.io.checksum.poke("hFFFFFFFF".U)
        c.io.matches.expect(true.B)
        c.clock.step()
        
        c.io.tx_data.poke(neelix)
        c.io.rx_data.poke(neelix)
        c.io.checksum.poke("hFFFFFFFF".U)
        c.io.matches.expect(true.B)
        c.clock.step()

        println("Test checksum error")
        c.io.tx_data.poke(tuvok)
        c.io.rx_data.poke(tuvok)
        c.io.checksum.poke("h1234ABCD".U)
        c.io.matches.expect(false.B)
        c.clock.step()
        
        c.io.tx_data.poke(neelix)
        c.io.rx_data.poke(neelix)
        c.io.checksum.poke("h1234ABCD".U)
        c.io.matches.expect(false.B)
        c.clock.step()

        println("Test mismatch transport")
        c.io.tx_data.poke(tuvok)
        c.io.rx_data.poke(neelix)
        c.io.checksum.poke("hFFFFFFFF".U)
        c.io.matches.expect(false.B)
        c.clock.step()

        c.io.tx_data.poke(neelix)
        c.io.rx_data.poke(tuvok)
        c.io.checksum.poke("hFFFFFFFF".U)
        c.io.matches.expect(false.B)
        c.clock.step()
    }
  }

  it should "test ProtocolLayer fdi.plRxActiveReq and lp_rx_active_sts" in {
    test(new ProtocolLayer(fdiParams)){ c => 
      c.io.TLready_to_rcv.poke(true.B)
      c.io.fdi.plRxActiveReq.poke(true.B)
      c.io.fdi.plStateStatus.poke(PhyState.active)
      c.clock.step()

      c.io.TLready_to_rcv.poke(false.B)
      c.io.fdi.plRxActiveReq.poke(true.B)
      c.io.fdi.plStateStatus.poke(PhyState.active)
      c.clock.step()

      c.io.TLready_to_rcv.poke(true.B)
      c.io.fdi.plRxActiveReq.poke(false.B)
      c.io.fdi.plStateStatus.poke(PhyState.active)
      c.clock.step()

      c.io.TLready_to_rcv.poke(true.B)
      c.io.fdi.plRxActiveReq.poke(true.B)
      c.io.fdi.plStateStatus.poke(PhyState.reset)
      c.clock.step()

      c.io.TLready_to_rcv.poke(true.B)
      c.io.fdi.plRxActiveReq.poke(true.B)
      c.io.fdi.plStateStatus.poke(PhyState.linkReset)
      c.clock.step()
    }
  }

  it should "test ProtocolLayer stall req/ack" in {
    test(new ProtocolLayer(fdiParams)){ c => 
      c.io.fdi.plStallReq.poke(false.B)
      c.clock.step()
      c.io.fdi.lpStallAck.expect(false.B)
      c.io.fdi.plStallReq.poke(true.B)
      c.io.fdi.lpStallAck.expect(false.B)
      c.clock.step()
      c.io.fdi.lpStallAck.expect(true.B)
    }
  }

  it should "test ProtocolLayer link_error" in {
    test(new ProtocolLayer(fdiParams)){ c => 
      c.io.fault.poke(true.B)
      c.io.fdi.lpLinkError.expect(true.B)

      c.io.fault.poke(false.B)
      c.io.fdi.lpLinkError.expect(false.B)
    }
  }
}

class HammingWrapper(val protoParams: ProtocolLayerParams) extends Module {
  val io = IO(new Bundle{
    val tx_data = Input(UInt(protoParams.ucieNonEccWidth.W))
    val rx_data = Input(UInt(protoParams.ucieNonEccWidth.W))
    val checksum = Input(UInt(protoParams.ucieEccWidth.W))
    val matches = Output(Bool())
  })
  val encoder = Module(new HammingEncode(protoParams))
  val decoder = Module(new HammingDecode(protoParams))
  
  encoder.io.data := io.tx_data
  decoder.io.data := io.rx_data
  decoder.io.checksum := encoder.io.checksum & io.checksum
  io.matches := decoder.io.matches
}
