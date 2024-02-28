package ucie.d2dadapter

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

    // val start_negotiation = Input(Bool())
    // val negotiation_complete = Output(Bool())

    // val sideband_rcv = Input(UInt(D2DAdapterSignalSize.SIDEBAND_MESSAGE_OP_WIDTH)) // sideband requested signals
    // val parity_sideband_snt = Output(UInt(D2DAdapterSignalSize.SIDEBAND_MESSAGE_OP_WIDTH)) // tell sideband module to send request of state change
    // val parity_sideband_rdy = Input(Bool())// sideband can consume the op in sideband_snt. 

    // val parity_tx_sw_en = Input(Bool())// if parity tx is enabled by software
    // val parity_rx_sw_en = Input(Bool())// if parity rx is enabled by software
    // val parity_rx_enable = Output(Bool())// tell parity if the parity should be used for receiving
    // val parity_tx_enable = Output(Bool())// tell parity if the parity should be used for sending

    // val cycles_2us = Input(UInt(32.W))

class ParityNegotiationSubmoduleTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "ParityNegotiationSubmoduleTest"
    it should "complete negotiation (both false and then tx open)" in {
        val params = new D2DAdapterParams()
        test(new ParityNegotiationSubmodule(params)) { c => 
            // init
            c.io.start_negotiation.poke(false.B)
            c.io.cycles_2us.poke(100.U(32.W))
            c.io.parity_tx_sw_en.poke(false.B)
            c.io.parity_rx_sw_en.poke(false.B)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.io.parity_sideband_rdy.poke(false.B)

            c.clock.step(1)
            // start both are false
            c.io.start_negotiation.poke(true.B)
            c.clock.step(1)
            //should timeout and complete
            while(c.io.negotiation_complete.peek().litValue == false.B.litValue){
                c.io.parity_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)   
            }
            c.io.parity_rx_enable.expect(false.B)
            c.io.parity_tx_enable.expect(false.B)
            c.clock.step(20)
            c.io.start_negotiation.poke(false.B)
            c.io.parity_rx_enable.expect(false.B)
            c.io.parity_tx_enable.expect(false.B)
            c.clock.step(20)
            c.io.parity_rx_enable.expect(false.B)
            c.io.parity_tx_enable.expect(false.B)
            // tx ok
            c.io.start_negotiation.poke(true.B)
            c.io.parity_tx_sw_en.poke(true.B)
            c.io.parity_rx_sw_en.poke(false.B)
            c.clock.step(1)          
            while(c.io.parity_sideband_snt.peek().litValue != SideBandMessageOP.PARITY_FEATURE_REQ.litValue){
                c.io.parity_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)   
            }
            // send req
            c.io.parity_sideband_rdy.poke(true.B)
            c.clock.step(1)
            c.io.parity_sideband_rdy.poke(false.B)
            c.clock.step(1)            
            // give req
            c.io.sideband_rcv.poke(SideBandMessageOP.PARITY_FEATURE_REQ)
            c.clock.step(1)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.clock.step(1)
            while(c.io.parity_sideband_snt.peek().litValue != SideBandMessageOP.PARITY_FEATURE_NAK.litValue){
                c.io.parity_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)   
            }
            // send nack
            c.io.parity_sideband_rdy.poke(true.B)
            c.clock.step(1)
            c.io.parity_sideband_rdy.poke(false.B)
            c.clock.step(1)      
            // give ack
            c.io.sideband_rcv.poke(SideBandMessageOP.PARITY_FEATURE_ACK)
            c.clock.step(1)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.clock.step(1)    

            while(c.io.negotiation_complete.peek().litValue == false.B.litValue){
                c.io.parity_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)   
            }
            c.io.parity_rx_enable.expect(false.B)
            c.io.parity_tx_enable.expect(true.B)
            c.clock.step(20)
            c.io.start_negotiation.poke(false.B)
            c.io.parity_rx_enable.expect(false.B)
            c.io.parity_tx_enable.expect(true.B)
            c.clock.step(20)
            c.io.parity_rx_enable.expect(false.B)
            c.io.parity_tx_enable.expect(true.B)
        }
    }

    it should "request tx but get nack" in {
        val params = new D2DAdapterParams()
        test(new ParityNegotiationSubmodule(params)) { c => 
            // init
            c.io.start_negotiation.poke(false.B)
            c.io.cycles_2us.poke(100.U(32.W))
            c.io.parity_tx_sw_en.poke(false.B)
            c.io.parity_rx_sw_en.poke(false.B)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.io.parity_sideband_rdy.poke(false.B)

            c.clock.step(1)
            // tx 
            c.io.start_negotiation.poke(true.B)
            c.io.parity_tx_sw_en.poke(true.B)
            c.io.parity_rx_sw_en.poke(false.B)
            c.clock.step(1)          
            while(c.io.parity_sideband_snt.peek().litValue != SideBandMessageOP.PARITY_FEATURE_REQ.litValue){
                c.io.parity_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)   
            }
            // send req
            c.io.parity_sideband_rdy.poke(true.B)
            c.clock.step(1)
            c.io.parity_sideband_rdy.poke(false.B)
            c.clock.step(1)            
            // give req
            c.io.sideband_rcv.poke(SideBandMessageOP.PARITY_FEATURE_REQ)
            c.clock.step(1)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.clock.step(1)
            while(c.io.parity_sideband_snt.peek().litValue != SideBandMessageOP.PARITY_FEATURE_NAK.litValue){
                c.io.parity_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)   
            }
            // send nack
            c.io.parity_sideband_rdy.poke(true.B)
            c.clock.step(1)
            c.io.parity_sideband_rdy.poke(false.B)
            c.clock.step(1)      
            // give nack
            c.io.sideband_rcv.poke(SideBandMessageOP.PARITY_FEATURE_NAK)
            c.clock.step(1)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.clock.step(1)    

            while(c.io.negotiation_complete.peek().litValue == false.B.litValue){
                c.io.parity_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)   
            }
            c.io.parity_rx_enable.expect(false.B)
            c.io.parity_tx_enable.expect(false.B)
            c.clock.step(20)
            c.io.start_negotiation.poke(false.B)
            c.io.parity_rx_enable.expect(false.B)
            c.io.parity_tx_enable.expect(false.B)
            c.clock.step(20)
            c.io.parity_rx_enable.expect(false.B)
            c.io.parity_tx_enable.expect(false.B)
        }
    }

    it should "open rx" in {
        val params = new D2DAdapterParams()
        test(new ParityNegotiationSubmodule(params)) { c => 
            // init
            c.io.start_negotiation.poke(false.B)
            c.io.cycles_2us.poke(100.U(32.W))
            c.io.parity_tx_sw_en.poke(false.B)
            c.io.parity_rx_sw_en.poke(false.B)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.io.parity_sideband_rdy.poke(false.B)

            c.clock.step(1)
            // tx 
            c.io.start_negotiation.poke(true.B)
            c.io.parity_tx_sw_en.poke(false.B)
            c.io.parity_rx_sw_en.poke(true.B)
            c.clock.step(1)             
            // give req
            c.io.sideband_rcv.poke(SideBandMessageOP.PARITY_FEATURE_REQ)
            c.clock.step(1)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.clock.step(1)
            while(c.io.parity_sideband_snt.peek().litValue != SideBandMessageOP.PARITY_FEATURE_ACK.litValue){
                c.io.parity_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)   
            }
            // send ack
            c.io.parity_sideband_rdy.poke(true.B)
            c.clock.step(1)
            c.io.parity_sideband_rdy.poke(false.B)
            c.clock.step(1)      

            while(c.io.negotiation_complete.peek().litValue == false.B.litValue){
                c.io.parity_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)   
            }
            c.io.parity_rx_enable.expect(true.B)
            c.io.parity_tx_enable.expect(false.B)
            c.clock.step(20)
            c.io.start_negotiation.poke(false.B)
            c.io.parity_rx_enable.expect(true.B)
            c.io.parity_tx_enable.expect(false.B)
            c.clock.step(20)
            c.io.parity_rx_enable.expect(true.B)
            c.io.parity_tx_enable.expect(false.B)
        }
    }

    it should "open both tx and rx" in {
        val params = new D2DAdapterParams()
        test(new ParityNegotiationSubmodule(params)) { c => 
            // init
            c.io.start_negotiation.poke(false.B)
            c.io.cycles_2us.poke(100.U(32.W))
            c.io.parity_tx_sw_en.poke(false.B)
            c.io.parity_rx_sw_en.poke(false.B)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.io.parity_sideband_rdy.poke(false.B)

            c.clock.step(1)
            // tx 
            c.io.start_negotiation.poke(true.B)
            c.io.parity_tx_sw_en.poke(true.B)
            c.io.parity_rx_sw_en.poke(true.B)
            c.clock.step(1)          
            while(c.io.parity_sideband_snt.peek().litValue != SideBandMessageOP.PARITY_FEATURE_REQ.litValue){
                c.io.parity_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)   
            }
            // send req
            c.io.parity_sideband_rdy.poke(true.B)
            c.clock.step(1)
            c.io.parity_sideband_rdy.poke(false.B)
            c.clock.step(1)            
            // give req
            c.io.sideband_rcv.poke(SideBandMessageOP.PARITY_FEATURE_REQ)
            c.clock.step(1)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.clock.step(1)
            while(c.io.parity_sideband_snt.peek().litValue != SideBandMessageOP.PARITY_FEATURE_ACK.litValue){
                c.io.parity_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)   
            }
            // send ack
            c.io.parity_sideband_rdy.poke(true.B)
            c.clock.step(1)
            c.io.parity_sideband_rdy.poke(false.B)
            c.clock.step(1)      
            // give ack
            c.io.sideband_rcv.poke(SideBandMessageOP.PARITY_FEATURE_ACK)
            c.clock.step(1)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.clock.step(1)    

            while(c.io.negotiation_complete.peek().litValue == false.B.litValue){
                c.io.parity_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)   
            }
            c.io.parity_rx_enable.expect(true.B)
            c.io.parity_tx_enable.expect(true.B)
            c.clock.step(20)
            c.io.start_negotiation.poke(false.B)
            c.io.parity_rx_enable.expect(true.B)
            c.io.parity_tx_enable.expect(true.B)
            c.clock.step(20)
            c.io.parity_rx_enable.expect(true.B)
            c.io.parity_tx_enable.expect(true.B)
        }
    }
}
