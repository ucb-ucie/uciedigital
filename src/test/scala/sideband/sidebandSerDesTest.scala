package edu.berkeley.cs.ucie.digital
package sideband

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import interfaces._

class SerDesTester extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "SerDes"
  it should "simple serializer sanity" in {
    test(new SidebandSerializer(new SidebandParams(), new FdiParams(width = 8, dllpWidth = 8, sbWidth = 32))) { c =>
        // prepare random data generator
        println("Test started")
        val seed: Int = 0
        val rand = new scala.util.Random(seed)

        //init
        c.io.in.valid.poke(false.B)
        c.io.out.credit.poke(false.B)
        c.clock.step()

        //Send data to serializer
        println("Send data")
        val data = BigInt(c.msg_w, rand).U
        c.io.in.valid.poke(true.B)
        c.io.in.bits.poke(data)
        c.io.out.valid.expect(false.B)
        c.clock.step()

        //Check serialized data
        c.io.in.valid.poke(false.B)
        for(i <- 0 until (c.msg_w / c.sb_w)){
          val serialized_data = data((i+1)*c.sb_w-1, i*c.sb_w)
          c.io.in.ready.expect(false.B)
          c.io.out.valid.expect(true.B)
          c.io.out.bits.expect(serialized_data)
          c.clock.step()
        }

        //make sure nothing is there
        c.io.in.ready.expect(true.B)
        c.io.out.valid.expect(false.B)
    }
  }

  it should "simple deserializer sanity" in {
    test(new SidebandDeserializer(new SidebandParams(), new FdiParams(width = 8, dllpWidth = 8, sbWidth = 32))) { c => 
      // prepare random data generator
      println("Test started")
      val seed: Int = 0
      val rand = new scala.util.Random(seed)

      //init
      c.io.in.valid.poke(false.B)
      c.io.out.ready.poke(false.B)
      c.clock.step()

      //Send data to deserializer
      println("Send data")
      val data = BigInt(c.msg_w, rand).U
      for(i <- 0 until (c.msg_w / c.sb_w)){
        c.io.in.valid.poke(true.B)
        c.io.in.bits.poke(data((i+1)*c.sb_w-1, i*c.sb_w))
        c.io.out.valid.expect(false.B)
        c.clock.step()
      }

      //Check deserialized data and credit return
      c.io.in.valid.poke(false.B)
      c.io.out.ready.poke(true.B)
      c.io.out.valid.expect(true.B)
      c.io.out.bits.expect(data)
      //c.io.in.credit.expect(true.B)
      c.clock.step()

      //make sure nothing is there
      c.io.out.valid.expect(false.B)
    }
  }

  it should "stress serializer sanity" in {
    test(new SidebandSerializer(new SidebandParams(), new FdiParams(width = 8, dllpWidth = 8, sbWidth = 32))) { c =>
        // prepare random data generator
        println("Test started")
        val seed: Int = 0
        val rand = new scala.util.Random(seed)

        //init
        c.io.in.valid.poke(false.B)
        c.io.out.credit.poke(false.B)
        c.clock.step()

        //Transfer data 32 times until no credit left
        for(i <- 0 until c.cdt_max){
          //Send data to serializer
          println("Send data")
          val data = 1.U
          c.io.in.valid.poke(true.B)
          c.io.in.bits.poke(data)
          c.io.out.valid.expect(false.B)
          c.clock.step()

          //Check serialized data
          c.io.in.valid.poke(false.B)
          for(j <- 0 until (c.msg_w / c.sb_w)){
            val serialized_data = data((j+1)*c.sb_w-1, j*c.sb_w)
            c.io.in.ready.expect(false.B)
            c.io.out.valid.expect(true.B)
            c.io.out.bits.expect(serialized_data)
            c.clock.step()
          }
        }

        //Send data to serializer when no credit
        println("Send data")
        val data = 1.U
        c.io.in.valid.poke(true.B)
        c.io.in.bits.poke(data)
        c.io.out.valid.expect(false.B)
        c.clock.step()

        //Check not send out msg when no credit left
        c.io.in.valid.poke(false.B)
        c.io.out.valid.expect(false.B)
        c.io.in.ready.expect(false.B)
        c.clock.step()
    }
  }
}