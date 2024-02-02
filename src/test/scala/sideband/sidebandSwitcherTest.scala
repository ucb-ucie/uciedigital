package edu.berkeley.cs.ucie.digital
package sideband

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class SwitcherTester extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "Switcher"
  it should "instantiate test" in {
    test(new sidebandSwitcher(0x001, new SidebandParams())){ c => 
        c.clock.step()
        println("welp")
    }
  }
  it should "simple foryou sanity" in {
    // This is the D2D layer switcher
    test(new switcher_wrapper()) { c => 
        //init
        c.io.inner.layer_to_node_above.valid.poke(false.B)
        c.io.inner.layer_to_node_above.bits.poke(0.U)
        c.io.inner.node_to_layer_above.ready.poke(false.B)

        c.io.inner.layer_to_node_below.valid.poke(false.B)
        c.io.inner.layer_to_node_below.bits.poke(0.U)
        c.io.inner.node_to_layer_below.ready.poke(false.B)

        c.io.outer.node_to_layer_above.valid.poke(false.B)
        c.io.outer.node_to_layer_above.bits.poke(0.U)
        c.io.outer.layer_to_node_above.ready.poke(false.B)

        c.io.outer.node_to_layer_below.valid.poke(false.B)
        c.io.outer.node_to_layer_below.bits.poke(0.U)
        c.io.outer.layer_to_node_below.ready.poke(false.B)

        c.clock.step()

        // send a packet from outer_above to inner_above
        c.io.outer.node_to_layer_above.valid.poke(true.B)
        val packet = c.io.dummy_foryou.peek()
        c.io.outer.node_to_layer_above.bits.poke(packet)
        c.io.inner.node_to_layer_above.ready.poke(true.B)

        // check that the packet was received
        c.io.inner.node_to_layer_above.valid.expect(true.B)
        c.io.inner.node_to_layer_above.bits.expect(packet)
        c.io.outer.node_to_layer_above.ready.expect(true.B)
        
        }
    }
  
  it should "simple notforyou sanity" in {
    // This is the D2D layer switcher
    test(new switcher_wrapper()) { c => 
        //init
        c.io.inner.layer_to_node_above.valid.poke(false.B)
        c.io.inner.layer_to_node_above.bits.poke(0.U)
        c.io.inner.node_to_layer_above.ready.poke(false.B)

        c.io.inner.layer_to_node_below.valid.poke(false.B)
        c.io.inner.layer_to_node_below.bits.poke(0.U)
        c.io.inner.node_to_layer_below.ready.poke(false.B)

        c.io.outer.node_to_layer_above.valid.poke(false.B)
        c.io.outer.node_to_layer_above.bits.poke(0.U)
        c.io.outer.layer_to_node_above.ready.poke(false.B)

        c.io.outer.node_to_layer_below.valid.poke(false.B)
        c.io.outer.node_to_layer_below.bits.poke(0.U)
        c.io.outer.layer_to_node_below.ready.poke(false.B)

        c.clock.step()

        // send a packet from outer_above to outer_below
        c.io.outer.node_to_layer_above.valid.poke(true.B)
        val packet = c.io.dummy_notforyou.peek()
        c.io.outer.node_to_layer_above.bits.poke(packet)
        c.io.outer.layer_to_node_below.ready.poke(true.B)

        // check that the packet was received
        c.io.inner.node_to_layer_below.valid.expect(false.B)
        c.io.outer.layer_to_node_below.valid.expect(true.B)
        c.io.outer.layer_to_node_below.bits.expect(packet)
        c.io.outer.node_to_layer_above.ready.expect(true.B)

      }
  }

  it should "racing sending check" in {
   test(new race_switcher_wrapper() { c => 
        //init
        c.io.inner.layer_to_node_above.valid.poke(false.B)
        c.io.inner.layer_to_node_above.bits.poke(0.U)
        c.io.inner.node_to_layer_above.ready.poke(false.B)

        c.io.inner.layer_to_node_below.valid.poke(false.B)
        c.io.inner.layer_to_node_below.bits.poke(0.U)
        c.io.inner.node_to_layer_below.ready.poke(false.B)

        c.io.outer.node_to_layer_above.valid.poke(false.B)
        c.io.outer.node_to_layer_above.bits.poke(0.U)
        c.io.outer.layer_to_node_above.ready.poke(false.B)

        c.io.outer.node_to_layer_below.valid.poke(false.B)
        c.io.outer.node_to_layer_below.bits.poke(0.U)
        c.io.outer.layer_to_node_below.ready.poke(false.B)

        c.clock.step()

        
        c.io.outer.node_to_layer_above.ready.poke(true.B)
        // send a high priority complete message from inner layer to node above
        c.io.inner.layer_to_node_above.valid.poke(true.B)
        val packet_complete = c.io.output_complete.peek()
        val packet_message = c.io.output_message.peek()
        c.io.inner.layer_to_node_above.bits.poke(packet_complete)

        // send a low priority message from outer node below to node above
        c.io.outer.node_to_layer_above.valid.poke(true.B)
        c.io.outer.node_to_layer_above.bits.poke(packet_message)

        c.clock.step()

        // check that complete is arbitrated first
        c.io.inner.node_to_layer_above.valid.expect(true.B)
        c.io.inner.node_to_layer_above.bits.expect(packet_complete)
        // check that the inner gets ready
        c.io.inner.layer_to_node_above.ready.expect(true.B)
        // check that the outer gets blocked
        c.io.outer.node_to_layer_above.ready.expect(false.B)
   })   
  }
}

class dummyfactory extends Module {
  val io = IO(new Bundle {
    val output_foryou = Output(UInt(128.W))
    val output_notforyou = Output(UInt(128.W))
  })
  io.output_foryou := SBMessage_factory(SBM.LINK_MGMT_ADAPTER0_REQ_DISABLE, src="Protocol_0", remote=false, dst="D2D")
  io.output_notforyou := SBMessage_factory(SBM.MBINIT_REVERSALMB_CLEAR_ERROR_REQ, src="Protocol_0", remote=false, dst="PHY")
}

class switcher_wrapper extends Module{
    val io = IO( new Bundle{
        val inner = Flipped(new SidebandSwitcherbundle(new SidebandParams()))
        val outer = new SidebandSwitcherbundle(new SidebandParams())
        val dummy_foryou = Output(UInt(128.W))
        val dummy_notforyou = Output(UInt(128.W))
    })
    val s = Module(new sidebandSwitcher(0x001, new SidebandParams()))
    val d = Module(new dummyfactory())

    s.io.inner <> io.inner
    s.io.outer <> io.outer

    io.dummy_foryou := d.io.output_foryou
    io.dummy_notforyou := d.io.output_notforyou
}

class racefactory extends Module {
  val io = IO(new Bundle {
    val output_complete = Output(UInt(128.W))
    val output_message = Output(UInt(128.W))
  })
  io.output_complete := SBMessage_factory(SBM.COMP_0, src="Protocol_0", remote=false, dst="D2D")
  io.output_message := SBMessage_factory(SBM.MBINIT_REVERSALMB_CLEAR_ERROR_REQ, src="Protocol_0", remote=false, dst="PHY")
}

class race_switcher_wrapper extends Module{
    val io = IO( new Bundle{
        val inner = Flipped(new SidebandSwitcherbundle(new SidebandParams()))
        val outer = new SidebandSwitcherbundle(new SidebandParams())
        val output_complete = Output(UInt(128.W))
        val output_message = Output(UInt(128.W))
    })
    val s = Module(new sidebandSwitcher(0x001, new SidebandParams()))
    val d = Module(new racefactory())

    s.io.inner <> io.inner
    s.io.outer <> io.outer

    io.output_complete := d.io.output_complete
    io.output_message := d.io.output_message
}