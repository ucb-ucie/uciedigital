package edu.berkeley.cs.ucie.digital
package sideband

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class PriorityQueueTester extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "PriorityQueue"
  it should "simple enqdeq sanity" in {
    test(new SidebandPriorityQueue(new SidebandParams())) { c => 
        //init
        c.io.enq.valid.poke(false.B)
        c.io.deq.ready.poke(false.B)
        c.clock.step()
        //first enqueue
        c.io.enq.valid.poke(true.B)
        c.io.enq.bits.poke(0.U)
        c.io.deq.valid.expect(false.B)
        c.clock.step()
        //first dequeue
        c.io.enq.valid.poke(false.B)
        c.io.deq.ready.poke(true.B)
        c.io.deq.valid.expect(true.B)
        c.io.deq.bits.expect(0.U)
        c.clock.step()
        //make sure nothing is there
        c.io.deq.valid.expect(false.B)
        }
    }
  
  it should "simple priority sanity" in {
    test(new SidebandPriorityQueue(new SidebandParams())) { c => 
        //init
        c.io.enq.valid.poke(false.B)
        c.io.deq.ready.poke(false.B)
        c.clock.step()
        //first enqueue
        c.io.enq.valid.poke(true.B)
        //priority 2
        c.io.enq.bits.poke(0.U)
        c.io.deq.valid.expect(false.B)
        c.clock.step()
        //second enqueue
        c.io.enq.valid.poke(true.B)
        //priority 0
        c.io.enq.bits.poke(16.U)
        c.io.deq.valid.expect(true.B)
        c.clock.step()
        //first dequeue
        c.io.enq.valid.poke(false.B)
        c.io.deq.ready.poke(true.B)
        c.io.deq.valid.expect(true.B)
        c.io.deq.bits.expect(16.U)
        c.clock.step()
        // second dequeue
        c.io.deq.ready.poke(true.B)
        c.io.deq.valid.expect(true.B)
        c.io.deq.bits.expect(0.U)
        c.clock.step()
        //make sure nothing is there  
        c.io.deq.valid.expect(false.B)      
        }
  }

    it should "stress priority sanity" in {
    test(new SidebandPriorityQueue(new SidebandParams())) { c => 
        //init
        c.io.enq.valid.poke(false.B)
        c.io.deq.ready.poke(false.B)
        c.clock.step()
        //first enqueue
        c.io.enq.valid.poke(true.B)
        //priority 2
        c.io.enq.bits.poke(0.U)
        c.io.deq.valid.expect(false.B)
        c.clock.step()
        //second enqueue
        c.io.enq.valid.poke(true.B)
        //priority 0
        c.io.enq.bits.poke(16.U)
        c.io.deq.valid.expect(true.B)
        c.clock.step()
        //thrid enqueue
        c.io.enq.valid.poke(true.B)
        // priority 1
        c.io.enq.bits.poke(18.U)
        c.clock.step()
        //fourth enqueue
        c.io.enq.valid.poke(true.B)
        // priority 0
        c.io.enq.bits.poke(17.U)
        c.clock.step()
        //first dequeue
        c.io.enq.valid.poke(false.B)
        c.io.deq.ready.poke(true.B)
        c.io.deq.valid.expect(true.B)
        c.io.deq.bits.expect(16.U)
        c.clock.step()
        // second dequeue
        c.io.deq.ready.poke(true.B)
        c.io.deq.valid.expect(true.B)
        c.io.deq.bits.expect(17.U)
        c.clock.step()
        // third dequeue
        c.io.deq.ready.poke(true.B)
        c.io.deq.valid.expect(true.B)
        c.io.deq.bits.expect(18.U)
        c.clock.step()
        // fourth dequeue
        c.io.deq.ready.poke(true.B)
        c.io.deq.valid.expect(true.B)
        c.io.deq.bits.expect(0.U)
        c.clock.step()
        //make sure nothing is there  
        c.io.deq.valid.expect(false.B)      
        }
  }

}