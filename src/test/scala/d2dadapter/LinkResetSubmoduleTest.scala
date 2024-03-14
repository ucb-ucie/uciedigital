package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import interfaces._
import sideband._

    // val fdi_lp_state_req = Input(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val linkreset_rdi_lp_state_req = Output(UInt(D2DAdapterSignalSize.STATE_WIDTH))

    // val linkreset_entry = Output(Bool())

    // val link_state = Input(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val linkreset_sb_rcv = Input(UInt(D2DAdapterSignalSize.SIDEBAND_MESSAGE_OP_WIDTH)) // sideband requested signals
    // val linkreset_sb_snd = Output(UInt(D2DAdapterSignalSize.SIDEBAND_MESSAGE_OP_WIDTH)) // tell sideband module to send request of state change
    // val linkreset_sb_rdy = Input(Bool())// sideband can consume the op in sideband_snt. 

class LinkResetSubmoduleTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "LinkResetSubmoduleTest"
    it should "complete linkreset flow as DP Adapter" in {
        test(new LinkResetSubmodule()) { c => 
            // init
            c.io.fdi_lp_state_req.poke(PhyStateReq.active)
            c.io.link_state.poke(PhyState.active)
            c.io.linkreset_sb_rcv.poke(SideBandMessage.NOP)
            c.io.linkreset_sb_rdy.poke(false.B)

            c.clock.step(1)
            for(i <- 0 until 5){
                // fdi request linkreset
                c.io.fdi_lp_state_req.poke(PhyStateReq.linkReset)
                c.clock.step(1)
                while(c.io.linkreset_sb_snd.peek().litValue != SideBandMessage.REQ_LINKRESET.litValue){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(false.B)
                    c.clock.step(1)   
                }
                for(i <- 0 until 10){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.REQ_LINKRESET)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }
                // send req
                c.io.linkreset_sb_rdy.poke(true.B)
                c.clock.step(1)
                c.io.linkreset_sb_rdy.poke(false.B)
                c.clock.step(1)      
                //
                for(i <- 0 until 10){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }              
                // get rsp linkreset
                c.io.linkreset_sb_rcv.poke(SideBandMessage.RSP_LINKRESET)
                c.clock.step(1)
                c.io.linkreset_sb_rcv.poke(SideBandMessage.NOP)
                c.clock.step(1)  

                while(c.io.linkreset_entry.peek().litValue == false.B.litValue){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)  
                }

                for(i <- 0 until 10){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(true.B)
                    
                    c.clock.step(1)                   
                }   
                // transition to linkreset
                c.io.link_state.poke(PhyState.linkReset)
                c.clock.step(1)
                // should request for rdi to linkreset
                for(i <- 0 until 10){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }   
                // transition to reset go again
                c.io.fdi_lp_state_req.poke(PhyStateReq.active)
                c.clock.step(100)  
                c.io.link_state.poke(PhyState.reset)
                c.clock.step(100)     
                c.io.fdi_lp_state_req.poke(PhyStateReq.nop)
                c.io.linkreset_entry.expect(false.B) 
                
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(PhyStateReq.active)
                c.io.linkreset_entry.expect(false.B) 
                
                c.clock.step(100)  
                c.io.link_state.poke(PhyState.active)
                c.io.linkreset_entry.expect(false.B) 
                
                c.clock.step(100)  
                c.io.linkreset_entry.expect(false.B) 
                
                c.clock.step(100)  
            }
      
        }
    }

    it should "complete linkreset flow as UP Adapter" in {
        test(new LinkResetSubmodule()) { c => 
            // init
            c.io.fdi_lp_state_req.poke(PhyStateReq.active)
            c.io.link_state.poke(PhyState.active)
            c.io.linkreset_sb_rcv.poke(SideBandMessage.NOP)
            c.io.linkreset_sb_rdy.poke(false.B)

            c.clock.step(1)
            for(i <- 0 until 5){
                // fdi request linkreset
                c.io.linkreset_sb_rcv.poke(SideBandMessage.REQ_LINKRESET)
                c.clock.step(1)
                c.io.linkreset_sb_rcv.poke(SideBandMessage.NOP)
                c.clock.step(1)

                while(c.io.linkreset_sb_snd.peek().litValue != SideBandMessage.RSP_LINKRESET.litValue){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)   
                }
                for(i <- 0 until 10){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.RSP_LINKRESET)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }
                // send rsp
                c.io.linkreset_sb_rdy.poke(true.B)
                c.clock.step(1)
                c.io.linkreset_sb_rdy.poke(false.B)
                c.clock.step(1)      

                while(c.io.linkreset_entry.peek().litValue == false.B.litValue){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)  
                }
                // complete linkreset
                for(i <- 0 until 10){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(true.B)
                    
                    c.clock.step(1)                   
                }   
                // transition to linkreset
                c.io.link_state.poke(PhyState.linkReset)
                c.clock.step(1)
                // no need to request for rdi
                for(i <- 0 until 10){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }   
                // transition to reset go again
                c.io.fdi_lp_state_req.poke(PhyStateReq.active)
                c.clock.step(100)  
                c.io.link_state.poke(PhyState.reset)
                c.clock.step(100)     
                c.io.fdi_lp_state_req.poke(PhyStateReq.nop)
                c.io.linkreset_entry.expect(false.B) 
                
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(PhyStateReq.active)
                c.io.linkreset_entry.expect(false.B) 
                
                c.clock.step(100)  
                c.io.link_state.poke(PhyState.active)
                c.io.linkreset_entry.expect(false.B) 
                
                c.clock.step(100)  
                c.io.linkreset_entry.expect(false.B) 
                
                c.clock.step(100)  
            }
      
        }
    }
    it should "complete linkreset flow as DP Adapter from reset" in {
        test(new LinkResetSubmodule()) { c => 
            // init
            c.io.fdi_lp_state_req.poke(PhyStateReq.active)
            c.io.fdi_lp_state_req_prev.poke(PhyStateReq.active)
            c.io.link_state.poke(PhyState.reset)
            c.io.linkreset_sb_rcv.poke(SideBandMessage.NOP)
            c.io.linkreset_sb_rdy.poke(false.B)

            c.clock.step(1)
            for(i <- 0 until 5){
                // fdi request linkreset but not from NOP
                c.io.fdi_lp_state_req.poke(PhyStateReq.linkReset)
                c.io.fdi_lp_state_req_prev.poke(PhyStateReq.active)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(PhyStateReq.linkReset)
                c.io.fdi_lp_state_req_prev.poke(PhyStateReq.linkReset)
                // should do nothing
                for(i <- 0 until 10){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }
                // give the correct transition
                c.io.fdi_lp_state_req.poke(PhyStateReq.nop)
                c.io.fdi_lp_state_req_prev.poke(PhyStateReq.linkReset)
                c.clock.step(1) 
                c.io.fdi_lp_state_req.poke(PhyStateReq.linkReset)
                c.io.fdi_lp_state_req_prev.poke(PhyStateReq.nop)
                c.clock.step(1)   
                c.io.fdi_lp_state_req.poke(PhyStateReq.linkReset)
                c.io.fdi_lp_state_req_prev.poke(PhyStateReq.linkReset)
                c.clock.step(1)        
                // 
                while(c.io.linkreset_sb_snd.peek().litValue != SideBandMessage.REQ_LINKRESET.litValue){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)   
                }
                for(i <- 0 until 10){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.REQ_LINKRESET)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }
                // send req
                c.io.linkreset_sb_rdy.poke(true.B)
                c.clock.step(1)
                c.io.linkreset_sb_rdy.poke(false.B)
                c.clock.step(1)      
                //
                for(i <- 0 until 10){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }              
                // get rsp linkreset
                c.io.linkreset_sb_rcv.poke(SideBandMessage.RSP_LINKRESET)
                c.clock.step(1)
                c.io.linkreset_sb_rcv.poke(SideBandMessage.NOP)
                c.clock.step(1)  

                while(c.io.linkreset_entry.peek().litValue == false.B.litValue){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)  
                }

                for(i <- 0 until 10){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(true.B)
                    
                    c.clock.step(1)                   
                }   
                // transition to linkreset
                c.io.link_state.poke(PhyState.linkReset)
                c.clock.step(1)
                // should request for rdi to linkreset
                for(i <- 0 until 10){
                    c.io.linkreset_sb_snd.expect(SideBandMessage.NOP)
                    c.io.linkreset_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }   
                // transition to reset go again
                c.io.fdi_lp_state_req.poke(PhyStateReq.active)
                c.clock.step(100)  
                c.io.link_state.poke(PhyState.reset)
                c.clock.step(100)     
                c.io.fdi_lp_state_req.poke(PhyStateReq.nop)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(PhyStateReq.active)
                c.io.fdi_lp_state_req_prev.poke(PhyStateReq.active)
                c.clock.step(100)
            }
        }
    }
}
