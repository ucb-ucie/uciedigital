package edu.berkeley.cs.ucie.digital
package sideband

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import interfaces._

class NodeTester extends AnyFlatSpec with ChiselScalatestTester {
  val fdiParams = new FdiParams(width = 8, dllpWidth = 8, sbWidth = 32)
  behavior of "sidebandNode"
  it should "simple sidebandNode tx sanity" in {
    test(new SidebandNode(new SidebandParams(), fdiParams)) { c =>
        // prepare random data generator
        println("Test started")
        val seed: Int = 0
        val rand = new scala.util.Random(seed)

        //init
        c.io.inner.layer_to_node.valid.poke(false.B)
        c.io.outer.tx.credit.poke(false.B)
        c.clock.step()

        //Layer send data to sidebandNode
        println("Send data")
        val data = BigInt(c.sbParams.sbNodeMsgWidth, rand).U
        c.io.inner.layer_to_node.valid.poke(true.B)
        c.io.inner.layer_to_node.bits.poke(data)
        c.io.outer.tx.valid.expect(false.B)
        c.clock.step()

        //Check tx data from sidebandNode
        //c.io.inner.layer_to_node.valid.poke(false.B)
        for(i <- 0 until (c.sbParams.sbNodeMsgWidth / c.fdiParams.sbWidth)){
          val serialized_data = data((i+1)*c.fdiParams.sbWidth-1, i*c.fdiParams.sbWidth)
          c.io.inner.layer_to_node.ready.expect(false.B)
          c.io.outer.tx.valid.expect(true.B)
          c.io.outer.tx.bits.expect(serialized_data)
          c.clock.step()
        }

        //make sure nothing is there
        c.io.inner.layer_to_node.ready.expect(true.B)
        c.io.outer.tx.valid.expect(false.B)
    }
  }

  it should "simple sidebandNode rx sanity" in {
    test(new SidebandNode(new SidebandParams(), fdiParams)) { c => 
      // prepare random data generator
      println("Test started")
      val seed: Int = 0
      val rand = new scala.util.Random(seed)

      //init
      c.io.outer.rx.valid.poke(false.B)
      c.io.inner.node_to_layer.ready.poke(false.B)
      c.clock.step()

      //Interface send data to sidebandNode
      println("Send data")
      val data = BigInt(c.sbParams.sbNodeMsgWidth, rand).U
      for(i <- 0 until (c.sbParams.sbNodeMsgWidth / c.fdiParams.sbWidth)){
        c.io.outer.rx.valid.poke(true.B)
        c.io.outer.rx.bits.poke(data((i+1)*c.fdiParams.sbWidth-1, i*c.fdiParams.sbWidth))
        c.io.inner.node_to_layer.valid.expect(false.B)
        c.clock.step()
      }

      //Check data from node to layer, and credit return
      c.io.outer.rx.valid.poke(false.B)
      c.io.inner.node_to_layer.ready.poke(true.B)
      c.clock.step()
      c.io.inner.node_to_layer.valid.expect(true.B)
      c.io.inner.node_to_layer.bits.expect(data)
      c.io.outer.rx.credit.expect(true.B)
      c.clock.step()

      //make sure nothing is there
      c.io.inner.node_to_layer.valid.expect(false.B)
    }
  }

  it should "stress sidebandNode tx sanity" in {
    test(new SidebandNode(new SidebandParams(), fdiParams)) { c =>
        // prepare random data generator
        println("Test started")
        val seed: Int = 0
        val rand = new scala.util.Random(seed)

        //init
        c.io.inner.layer_to_node.valid.poke(false.B)
        c.io.outer.tx.credit.poke(false.B)
        c.clock.step()

        //Transfer data 32 times until no credit left
        for(i <- 0 until c.sbParams.maxCrd){
          //Send non-completion packet to serializer
          println("Send data")
          val data = 1.U
          c.io.inner.layer_to_node.valid.poke(true.B)
          c.io.inner.layer_to_node.bits.poke(data)
          c.io.outer.tx.valid.expect(false.B)
          c.clock.step()

          //Check serialized data
          c.io.inner.layer_to_node.valid.poke(false.B)
          for(j <- 0 until (c.sbParams.sbNodeMsgWidth / c.fdiParams.sbWidth)){
            val serialized_data = data((j+1)*c.fdiParams.sbWidth-1, j*c.fdiParams.sbWidth)
            c.io.inner.layer_to_node.ready.expect(false.B)
            c.io.outer.tx.valid.expect(true.B)
            c.io.outer.tx.bits.expect(serialized_data)
            c.clock.step()
          }
        }

        //Send non-completion packet to serializer when no credit
        println("Send data")
        var data = 1.U
        c.io.inner.layer_to_node.valid.poke(true.B)
        c.io.inner.layer_to_node.bits.poke(data)
        c.io.outer.tx.valid.expect(false.B)
        c.io.inner.layer_to_node.ready.expect(false.B)
        c.clock.step()

        //Check not send out msg when no credit left
        c.io.inner.layer_to_node.valid.poke(false.B)
        c.io.outer.tx.valid.expect(false.B)
        c.io.inner.layer_to_node.ready.expect(false.B)
        c.clock.step()

        //Send completion packet to serializer when no credit
        println("Send data")
        data = 16.U
        c.io.inner.layer_to_node.valid.poke(true.B)
        c.io.inner.layer_to_node.bits.poke(data)
        c.io.outer.tx.valid.expect(false.B)
        c.io.inner.layer_to_node.ready.expect(true.B)
        c.clock.step()

        //Check still send out completion packet when no credit left
        c.io.inner.layer_to_node.valid.poke(false.B)
        for(j <- 0 until (c.sbParams.sbNodeMsgWidth / c.fdiParams.sbWidth)){
          val serialized_data = data((j+1)*c.fdiParams.sbWidth-1, j*c.fdiParams.sbWidth)
          c.io.inner.layer_to_node.ready.expect(false.B)
          c.io.outer.tx.valid.expect(true.B)
          c.io.outer.tx.bits.expect(serialized_data)
          c.clock.step()
        }

        //Check sendout completion doesn't decrease credit,  ready = 0
        data = 1.U
        c.io.inner.layer_to_node.bits.poke(data)
        c.io.inner.layer_to_node.ready.expect(false.B)

        //Check credit increse if get credit return, ready = 1
        c.io.outer.tx.credit.poke(true.B)
        c.clock.step()
        c.io.outer.tx.credit.poke(false.B)
        c.io.inner.layer_to_node.ready.expect(true.B)

        //make sure nothing is there
        c.io.outer.tx.valid.expect(false.B)
    }
  }

  it should "stress sidebandNode rx sanity" in {
    test(new SidebandNode(new SidebandParams(), fdiParams)) { c => 
      // prepare random data generator
      println("Test started")
      val seed: Int = 0
      val rand = new scala.util.Random(seed)

      //init
      c.io.outer.rx.valid.poke(false.B)
      c.io.inner.node_to_layer.ready.poke(false.B)
      c.clock.step()

      //Interface send data to sidebandNode
      println("Send data")
      val enqData = Array(0.U, 16.U, 18.U, 17.U)
      val deqGoldenData = Array(16.U, 17.U, 18.U, 0.U)
      for(i <- 0 until 4){
        val data = enqData(i)
        for(j <- 0 until (c.sbParams.sbNodeMsgWidth / c.fdiParams.sbWidth)){
          c.io.outer.rx.valid.poke(true.B)
          c.io.outer.rx.bits.poke(data((j+1)*c.fdiParams.sbWidth-1, j*c.fdiParams.sbWidth))
          if(i == 0) c.io.inner.node_to_layer.valid.expect(false.B)
          else if(i > 1) c.io.inner.node_to_layer.valid.expect(true.B)
          c.clock.step()
        }
      }

      //Check data from node to layer should be in correct order, and credit return
      c.io.outer.rx.valid.poke(false.B)
      for(i <- 0 until 4){
        c.io.inner.node_to_layer.ready.poke(true.B)
        c.io.inner.node_to_layer.valid.expect(true.B)
        c.io.inner.node_to_layer.bits.expect(deqGoldenData(i))
        if((i==0) || (i==1)) c.io.outer.rx.credit.expect(false.B) // The first two deque packet is completion packet
        else c.io.outer.rx.credit.expect(true.B)
        c.clock.step()
      }

      //make sure nothing is there
      c.io.inner.node_to_layer.valid.expect(false.B)
    }
  }
}