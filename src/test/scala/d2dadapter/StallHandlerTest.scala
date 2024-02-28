package ucie.d2dadapter

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class StallHandlerTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "RDIStallHandler"
    it should "complete stall handshake" in {
        test(new RDIStallHandler(new D2DAdapterParams())) { c => 
            //init
            for( a <- 0 until 5){
                c.io.rdi_pl_stallreq.poke(false.B)
                c.io.mainband_stallcpt.poke(false.B)
                c.clock.step(1)
                c.clock.step(1)
                c.io.rdi_lp_stallack.expect(false.B)
                // Rising edge of stall
                c.io.rdi_pl_stallreq.poke(true.B)
                c.clock.step(1)
                // should tell the mainband to stall
                while(c.io.mainband_stallreq.peek().litValue != true.B.litValue){
                    c.io.rdi_pl_stallreq.poke(true.B)
                    c.clock.step(1)
                }
                // mainband processing
                for(i <- 0 until 10){
                    c.io.mainband_stallreq.expect(true.B)
                    c.io.rdi_lp_stallack.expect(false.B)
                    c.clock.step(1)
                }
                // mainband reply
                c.io.mainband_stallcpt.poke(true.B)
                c.clock.step(1)
                // should create rising edge on stallack
                while(c.io.rdi_lp_stallack.peek().litValue != true.B.litValue){
                    c.io.rdi_pl_stallreq.poke(true.B)
                    c.clock.step(1)
                }
                // stallack should keep high, as the handshake needs to wait for falling edge of req
                // the mainband should tell that it has stalled the transmission
                c.io.rdi_pl_stallreq.poke(true.B)
                for(i <- 0 until 10){
                    c.io.rdi_lp_stallack.expect(true.B)
                    c.io.mainband_stallcpt.expect(true.B)
                    c.clock.step(1)
                }
                // falling edge of req
                c.io.rdi_pl_stallreq.poke(false.B)         
                c.clock.step(1)   
                // waiting for falling edge of 
                while(c.io.rdi_lp_stallack.peek().litValue != false.B.litValue){
                    c.io.rdi_pl_stallreq.poke(false.B)
                    c.clock.step(1)
                }
                // stallack should keep low, as the handshake has completed
                for(i <- 0 until 10){
                    c.io.rdi_lp_stallack.expect(false.B)
                    c.clock.step(1)
                }
            }
        }
    }

    behavior of "FDIStallHandler"
    it should "complete stall handshake" in {
        test(new FDIStallHandler(new D2DAdapterParams())) { c => 
            for( a <- 0 until 5){
                //init
                c.io.linkmngt_stallreq.poke(false.B)
                c.clock.step(1)
                // check if the initial signals are all false
                for(i <- 0 until 10){
                    c.io.linkmngt_stallcpt.expect(false.B)
                    c.io.fdi_pl_stallreq.expect(false.B)
                    c.clock.step(1)
                }
                // link management require handshake
                c.io.linkmngt_stallreq.poke(true.B)
                c.clock.step(1)

                while(c.io.fdi_pl_stallreq.peek().litValue != true.B.litValue){
                    c.io.linkmngt_stallcpt.expect(false.B)
                    c.clock.step(1)
                }
                // start handshaking, waiting for ack
                for(i <- 0 until 10){
                    c.io.linkmngt_stallcpt.expect(false.B)
                    c.clock.step(1)
                }

                c.io.fdi_lp_stallack.poke(true.B)
                c.clock.step(1)
                //fdi ack, req should fall
                while(c.io.fdi_pl_stallreq.peek().litValue != false.B.litValue){
                    c.io.linkmngt_stallcpt.expect(false.B)
                    c.clock.step(1)
                }            
                // req fall, the ack should fall
                for(i <- 0 until 10){
                    c.io.linkmngt_stallcpt.expect(false.B)
                    c.clock.step(1)
                }

                // ack fall
                c.io.fdi_lp_stallack.poke(false.B)
                c.clock.step(1)
                // tell the link management that the handshake has completed
                while(c.io.linkmngt_stallcpt.peek().litValue != true.B.litValue){
                    c.io.fdi_pl_stallreq.expect(false.B)
                    c.clock.step(1)
                }
                
            }
        }
    }
}
