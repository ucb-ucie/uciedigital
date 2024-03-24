package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import interfaces._
import sideband._

    // val fdi_lp_state_req = Input(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val disabled_rdi_lp_state_req = Output(UInt(D2DAdapterSignalSize.STATE_WIDTH))

    // val disabled_entry = Output(Bool())

    // val link_state = Input(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val disabled_sb_rcv = Input(UInt(D2DAdapterSignalSize.SIDEBAND_MESSAGE_OP_WIDTH)) // sideband requested signals
    // val disabled_sb_snd = Output(UInt(D2DAdapterSignalSize.SIDEBAND_MESSAGE_OP_WIDTH)) // tell sideband module to send request of state change
    // val disabled_sb_rdy = Input(Bool())// sideband can consume the op in sb_snd. 

class LinkDisabledSubmoduleTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "LinkDisabledSubmoduleTest"
    it should "complete disable flow as DP Adapter" in {
        
        test(new LinkDisabledSubmodule()) { c => 
            // init
            c.io.fdi_lp_state_req.poke(PhyStateReq.active)
            c.io.link_state.poke(PhyState.active)
            c.io.disabled_sb_rcv.poke(SideBandMessage.NOP)
            c.io.disabled_sb_rdy.poke(false.B)

            c.clock.step(1)
            for(i <- 0 until 5){
                // fdi request disabled
                c.io.fdi_lp_state_req.poke(PhyStateReq.disabled)
                c.clock.step(1)
                while(c.io.disabled_sb_snd.peek().litValue != SideBandMessage.REQ_DISABLED.litValue){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)   
                }
                for(i <- 0 until 10){
                    c.io.disabled_sb_snd.expect(SideBandMessage.REQ_DISABLED)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }
                // send req
                c.io.disabled_sb_rdy.poke(true.B)
                c.clock.step(1)
                c.io.disabled_sb_rdy.poke(false.B)
                c.clock.step(1)      
                //
                for(i <- 0 until 10){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }              
                // get rsp disabled
                c.io.disabled_sb_rcv.poke(SideBandMessage.RSP_DISABLED)
                c.clock.step(1)
                c.io.disabled_sb_rcv.poke(SideBandMessage.NOP)
                c.clock.step(1)  

                while(c.io.disabled_entry.peek().litValue == false.B.litValue){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)  
                }

                for(i <- 0 until 10){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(true.B)
                    
                    c.clock.step(1)                   
                }   
                // transition to disabled
                c.io.link_state.poke(PhyState.disabled)
                c.clock.step(1)
                // should request for rdi to disabled
                for(i <- 0 until 10){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }   
                // transition to reset go again
                c.io.fdi_lp_state_req.poke(PhyStateReq.active)
                c.clock.step(100)  
                c.io.link_state.poke(PhyState.reset)
                c.clock.step(100)     
                c.io.fdi_lp_state_req.poke(PhyStateReq.nop)
                c.io.disabled_entry.expect(false.B) 
                
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(PhyStateReq.active)
                c.io.disabled_entry.expect(false.B) 
                
                c.clock.step(100)  
                c.io.link_state.poke(PhyState.active)
                c.io.disabled_entry.expect(false.B) 
                
                c.clock.step(100)  
                c.io.disabled_entry.expect(false.B) 
                
                c.clock.step(100)  
            }
      
        }
    }

    it should "complete disable flow as UP Adapter" in {
        test(new LinkDisabledSubmodule()) { c => 
            // init
            c.io.fdi_lp_state_req.poke(PhyStateReq.active)
            c.io.link_state.poke(PhyState.active)
            c.io.disabled_sb_rcv.poke(SideBandMessage.NOP)
            c.io.disabled_sb_rdy.poke(false.B)

            c.clock.step(1)
            for(i <- 0 until 5){
                // fdi request disabled
                c.io.disabled_sb_rcv.poke(SideBandMessage.REQ_DISABLED)
                c.clock.step(1)
                c.io.disabled_sb_rcv.poke(SideBandMessage.NOP)
                c.clock.step(1)

                while(c.io.disabled_sb_snd.peek().litValue != SideBandMessage.RSP_DISABLED.litValue){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)   
                }
                for(i <- 0 until 10){
                    c.io.disabled_sb_snd.expect(SideBandMessage.RSP_DISABLED)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }
                // send rsp
                c.io.disabled_sb_rdy.poke(true.B)
                c.clock.step(1)
                c.io.disabled_sb_rdy.poke(false.B)
                c.clock.step(1)      

                while(c.io.disabled_entry.peek().litValue == false.B.litValue){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)  
                }
                // complete disabled
                for(i <- 0 until 10){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(true.B)
                    
                    c.clock.step(1)                   
                }   
                // transition to disabled
                c.io.link_state.poke(PhyState.disabled)
                c.clock.step(1)
                // no need to request for rdi
                for(i <- 0 until 10){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }   
                // transition to reset go again
                c.io.fdi_lp_state_req.poke(PhyStateReq.active)
                c.clock.step(100)  
                c.io.link_state.poke(PhyState.reset)
                c.clock.step(100)     
                c.io.fdi_lp_state_req.poke(PhyStateReq.nop)
                c.io.disabled_entry.expect(false.B) 
                
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(PhyStateReq.active)
                c.io.disabled_entry.expect(false.B) 
                
                c.clock.step(100)  
                c.io.link_state.poke(PhyState.active)
                c.io.disabled_entry.expect(false.B) 
                
                c.clock.step(100)  
                c.io.disabled_entry.expect(false.B) 
                
                c.clock.step(100)  
            }
      
        }
    }

    it should "complete disable flow as DP Adapter from reset" in {
        test(new LinkDisabledSubmodule()) { c => 
            // init
            c.io.fdi_lp_state_req.poke(PhyStateReq.active)
            c.io.fdi_lp_state_req_prev.poke(PhyStateReq.active)
            c.io.link_state.poke(PhyState.reset)
            c.io.disabled_sb_rcv.poke(SideBandMessage.NOP)
            c.io.disabled_sb_rdy.poke(false.B)

            c.clock.step(1)
            for(i <- 0 until 5){
                // fdi request disabled but not from NOP
                c.io.fdi_lp_state_req.poke(PhyStateReq.disabled)
                c.io.fdi_lp_state_req_prev.poke(PhyStateReq.active)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(PhyStateReq.disabled)
                c.io.fdi_lp_state_req_prev.poke(PhyStateReq.disabled)
                // should do nothing
                for(i <- 0 until 10){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }
                // give the correct transition
                c.io.fdi_lp_state_req.poke(PhyStateReq.nop)
                c.io.fdi_lp_state_req_prev.poke(PhyStateReq.disabled)
                c.clock.step(1) 
                c.io.fdi_lp_state_req.poke(PhyStateReq.disabled)
                c.io.fdi_lp_state_req_prev.poke(PhyStateReq.nop)
                c.clock.step(1)   
                c.io.fdi_lp_state_req.poke(PhyStateReq.disabled)
                c.io.fdi_lp_state_req_prev.poke(PhyStateReq.disabled)
                c.clock.step(1)        
                // 
                while(c.io.disabled_sb_snd.peek().litValue != SideBandMessage.REQ_DISABLED.litValue){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)   
                }
                for(i <- 0 until 10){
                    c.io.disabled_sb_snd.expect(SideBandMessage.REQ_DISABLED)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }
                // send req
                c.io.disabled_sb_rdy.poke(true.B)
                c.clock.step(1)
                c.io.disabled_sb_rdy.poke(false.B)
                c.clock.step(1)      
                //
                for(i <- 0 until 10){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)                   
                }              
                // get rsp disabled
                c.io.disabled_sb_rcv.poke(SideBandMessage.RSP_DISABLED)
                c.clock.step(1)
                c.io.disabled_sb_rcv.poke(SideBandMessage.NOP)
                c.clock.step(1)  

                while(c.io.disabled_entry.peek().litValue == false.B.litValue){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(false.B)
                    
                    c.clock.step(1)  
                }

                for(i <- 0 until 10){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(true.B)
                    
                    c.clock.step(1)                   
                }   
                // transition to disabled
                c.io.link_state.poke(PhyState.disabled)
                c.clock.step(1)
                // should request for rdi to disabled
                for(i <- 0 until 10){
                    c.io.disabled_sb_snd.expect(SideBandMessage.NOP)
                    c.io.disabled_entry.expect(false.B)
                    
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
