package edu.berkeley.cs.ucie.digital
package sideband

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import interfaces._

class ChannelTest extends AnyFlatSpec with ChiselScalatestTester {
  val fdiParams = new FdiParams(width = 8, dllpWidth = 8, sbWidth = 32)
  behavior of "sideband channel"
  it should "instantiate d2d channel" in {
    test(new D2DSidebandChannel(BigInt(1), new SidebandParams(), fdiParams)){ c => 
        c.clock.step()
        println("welp")
    }
  }

  it should "instantiate phy channel" in {
    test(new PHYSidebandChannel(BigInt(2), new SidebandParams(), fdiParams)){ c => 
        c.clock.step()
        println("welp")
    }
  }

  it should "instantiate test" in {
    test(new channel_wrapper()){ c => 
        c.clock.step()
        println("welp")
    }
  }

  it should "send something for you" in {
    test (new channel_wrapper()) { c => 
        //init
        c.io.channel.to_upper_layer.rx.valid.poke(false.B)
        c.io.channel.to_upper_layer.tx.credit.poke(false.B)

        c.io.channel.to_lower_layer.rx.valid.poke(false.B)
        c.io.channel.to_lower_layer.tx.credit.poke(false.B)

        c.io.channel.inner.node_to_layer_above.ready.poke(false.B)

        c.clock.step()
        // send something for you
        c.io.channel.to_upper_layer.rx.valid.poke(true.B)
        val packet = c.io.dummy_foryou.peek()

        //send this in MSG_Width/NC_width cycles
        for(i <- 0 until (c.sbParams.sbNodeMsgWidth / c.fdiParams.sbWidth)){
            c.io.channel.to_upper_layer.rx.bits.poke(packet((i+1)*c.fdiParams.sbWidth-1, i*c.fdiParams.sbWidth))
            println("subpacket: " + packet((i+1)*c.fdiParams.sbWidth-1, i*c.fdiParams.sbWidth).litValue)
            c.clock.step()
        }

        // wait for inner.node_to_layer_above.valid to be true
        while(!c.io.channel.inner.node_to_layer_above.valid.peek().litToBoolean){
            c.clock.step()
        }

        // check that the packet has arrived
        c.io.channel.inner.node_to_layer_above.bits.expect(packet)
        c.io.channel.inner.node_to_layer_above.valid.expect(true.B)
        // assert the ready signal
        c.io.channel.inner.node_to_layer_above.ready.poke(true.B)
        println("waiting for credit")
        // wait for a credit return
        while(!c.io.channel.to_upper_layer.rx.credit.peek().litToBoolean){
            c.clock.step()
        }
    }
  }

  it should "send something not for you" in {
    test (new channel_wrapper()) { c => 
        // init
                c.io.channel.to_upper_layer.rx.valid.poke(false.B)
        c.io.channel.to_upper_layer.tx.credit.poke(false.B)

        c.io.channel.to_lower_layer.rx.valid.poke(false.B)
        c.io.channel.to_lower_layer.tx.credit.poke(false.B)

        c.io.channel.inner.node_to_layer_above.ready.poke(false.B)

        c.clock.step()
        // send something for you
        c.io.channel.to_upper_layer.rx.valid.poke(true.B)
        val packet = c.io.dummy_notforyou.peek()

        //send this in MSG_Width/NC_width cycles
        for(i <- 0 until (c.sbParams.sbNodeMsgWidth / c.fdiParams.sbWidth)){
            c.io.channel.to_upper_layer.rx.bits.poke(packet((i+1)*c.fdiParams.sbWidth-1, i*c.fdiParams.sbWidth))
            println("subpacket: " + packet((i+1)*c.fdiParams.sbWidth-1, i*c.fdiParams.sbWidth).litValue)
            c.clock.step()
        }

        // wait for the otherside tx valid to be high
        while(!c.io.channel.to_lower_layer.tx.valid.peek().litToBoolean){
            c.clock.step()
        }

        // check that the packet has arrived, MSG_Width/NC_width cycles 
        for(i <- 0 until (c.sbParams.sbNodeMsgWidth / c.fdiParams.sbWidth)){
            c.io.channel.to_lower_layer.tx.bits.expect(packet((i+1)*c.fdiParams.sbWidth-1, i*c.fdiParams.sbWidth))
            println("subpacket: " + packet((i+1)*c.fdiParams.sbWidth-1, i*c.fdiParams.sbWidth).litValue)
            c.clock.step()
        }

        // check the credit
        c.io.channel.to_upper_layer.rx.credit.expect(true.B)
    }
  }

  it should "send channel pair" in {
    test(new channel_pair_wrapper()){ c => 
        c.clock.step()
        println("welp")

        // initialize
        c.reset.poke(true.B)
        c.clock.step()
        c.reset.poke(false.B)
        c.clock.step()
        c.io.to_D2D.layer_to_node_below.valid.poke(true.B)
        val dtop_packet = c.io.dtop.peek()
        c.io.to_D2D.layer_to_node_below.bits.poke(dtop_packet)


        c.io.to_PHY.layer_to_node_above.valid.poke(true.B)
        val ptod_packet = c.io.ptod.peek()
        c.io.to_PHY.layer_to_node_above.bits.poke(ptod_packet)

        c.clock.step()
        c.io.to_D2D.layer_to_node_below.valid.poke(false.B)
        c.io.to_PHY.layer_to_node_above.valid.poke(false.B)

        // wait for the packet to arrive
        while(!c.io.to_PHY.node_to_layer_above.valid.peek().litToBoolean){
            c.clock.step()
        }

        c.io.to_PHY.node_to_layer_above.bits.expect(dtop_packet)

        //wait for the other packet to arrive
        while(!c.io.to_D2D.node_to_layer_below.valid.peek().litToBoolean){
            c.clock.step()
        }

        c.io.to_D2D.node_to_layer_below.bits.expect(ptod_packet)

        // verify credit is zero
        c.io.to_D2D.layer_to_node_below.ready.expect(false.B)
        c.io.to_PHY.layer_to_node_above.ready.expect(false.B)

        // send complete over
        c.io.to_D2D.layer_to_node_below.valid.poke(true.B)
        val dtop_complete = c.io.dtop_cmp.peek()
        c.io.to_D2D.layer_to_node_below.bits.poke(dtop_complete)

        c.io.to_D2D.layer_to_node_below.ready.expect(true.B)

        c.clock.step()

        // change the input to something that is not complete, and shut valid
        c.io.to_D2D.layer_to_node_below.valid.poke(false.B)
        c.io.to_D2D.layer_to_node_below.bits.poke(dtop_packet)


        // wait for arbitrary time: 20 cycles
        for(i <- 0 until 20){
            c.clock.step()
        }

        // verify the other side now sees the complete packet
        c.io.to_PHY.node_to_layer_above.valid.expect(true.B)
        c.io.to_PHY.node_to_layer_above.bits.expect(dtop_complete)

        // dequeue the complete packet
        c.io.to_PHY.node_to_layer_above.ready.poke(true.B)

        c.clock.step()

        c.io.to_PHY.node_to_layer_above.ready.poke(false.B)
        // see the dtop packet there
        c.io.to_PHY.node_to_layer_above.bits.expect(dtop_packet)
        // see the ready is still false
        for(i <- 0 until 10){
            c.clock.step()
        }

        c.io.to_D2D.layer_to_node_below.ready.expect(false.B)

        // now dequeue the other packet
        c.io.to_PHY.node_to_layer_above.ready.poke(true.B)
        for(i <- 0 until 10){
            c.clock.step()
        }
        // see credit return
        c.io.to_D2D.layer_to_node_below.ready.expect(true.B)
    }
  }
}

class channel_wrapper extends Module{
    val sbParams = new SidebandParams()
    val fdiParams = new FdiParams(width = 8, dllpWidth = 8, sbWidth = 32)
    val io = IO( new Bundle{
        val channel = new D2DSidebandChannelIO(sbParams, fdiParams)
        val dummy_foryou = Output(UInt(128.W))
        val dummy_notforyou = Output(UInt(128.W))
    })
    val s = Module(new D2DSidebandChannel(BigInt(1),sbParams, fdiParams))
    val d = Module(new dummyfactory())

    s.io <> io.channel

    io.dummy_foryou := d.io.output_foryou
    io.dummy_notforyou := d.io.output_notforyou
}

class channel_pair_wrapper extends Module{
    val sbParams = new SidebandParams(maxCrd = 1)
    val fdiParams = new FdiParams(width = 8, dllpWidth = 8, sbWidth = 32)
    val io = IO( new Bundle{
        val to_D2D = Flipped(new SidebandSwitcherbundle(sbParams))
        val to_PHY = Flipped(new SidebandSwitcherbundle(sbParams))
        val to_upper = new SidebandNodeOuterIO(sbParams, fdiParams)
        val to_lower = new SidebandLinkNodeOuterIO(sbParams, fdiParams)
        val dtop = Output(UInt(128.W))
        val ptod = Output(UInt(128.W))
        val dtop_cmp = Output(UInt(128.W))
    })
    val dietodie_channel = Module(new D2DSidebandChannel(BigInt(1),sbParams,fdiParams))
    val phy_channel = Module(new PHYSidebandChannel(BigInt(2),sbParams, fdiParams))
    val d = Module(new channelmsgfactory())

    io.to_D2D <> dietodie_channel.io.inner
    io.to_PHY <> phy_channel.io.inner

    io.to_upper <> dietodie_channel.io.to_upper_layer
    io.to_lower <> phy_channel.io.to_lower_layer

    dietodie_channel.io.to_lower_layer.tx <> phy_channel.io.to_upper_layer.rx
    dietodie_channel.io.to_lower_layer.rx <> phy_channel.io.to_upper_layer.tx

    io.dtop := d.io.dtop
    io.ptod := d.io.ptod
    io.dtop_cmp := d.io.dtop_cmp
}

class channelmsgfactory extends Module {
  val io = IO(new Bundle {
    val dtop = Output(UInt(128.W))
    val ptod = Output(UInt(128.W))
    val dtop_cmp = Output(UInt(128.W))
  })
  io.dtop := SBMessage_factory(SBM.LINK_MGMT_ADAPTER0_REQ_DISABLE, src="D2D", remote=false, dst="PHY")
  io.ptod := SBMessage_factory(SBM.MBINIT_REVERSALMB_CLEAR_ERROR_REQ, src="PHY", remote=false, dst="D2D")
  io.dtop_cmp := SBMessage_factory(SBM.COMP_0, src = "D2D", remote = false, dst = "PHY")
}