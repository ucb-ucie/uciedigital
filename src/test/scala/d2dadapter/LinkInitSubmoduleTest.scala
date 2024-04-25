package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
//import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import interfaces._

class LinkInitSubmoduleTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "LinkInitSubmoduleTest"
    it should "bring up fdi and rdi for link initialization" in {
        test(new LinkInitSubmodule()) { c => 
            // init
            c.io.fdi_lp_state_req.poke(PhyStateReq.nop)
            c.io.fdi_lp_state_req_prev.poke(PhyStateReq.nop)
            c.io.fdi_lp_rxactive_sts.poke(false.B)
            c.io.rdi_pl_state_sts.poke(PhyState.reset)
            c.io.rdi_pl_inband_pres.poke(false.B)
            c.io.linkinit_sb_rcv.poke(SideBandMessage.NOP)
            c.io.linkinit_sb_rdy.poke(false.B)
            c.io.link_state.poke(PhyState.reset)
            c.clock.step(1)
            // start
            println("Test started")
            for(i <- 0 until 10){
                // should not give any signal
                c.io.linkinit_fdi_pl_rxactive_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.nop)
                c.io.active_entry.expect(false.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.NOP)
                c.clock.step(1)
            }

            // RDI bring up
            c.io.rdi_pl_inband_pres.poke(true.B)
            c.clock.step(1)
            while(c.io.linkinit_rdi_lp_state_req.peek().litValue != PhyStateReq.active.litValue){
                c.io.linkinit_fdi_pl_rxactive_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.nop)
                c.io.active_entry.expect(false.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.NOP)
                c.clock.step(1)
            }
            // waiting for RDI to be active
            for(i <- 0 until 10){
                // should not give any signal
                c.io.linkinit_fdi_pl_rxactive_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.active)
                c.io.active_entry.expect(false.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.NOP)
                c.clock.step(1)
            }
            c.io.rdi_pl_state_sts.poke(PhyState.active)
            // RDI bring up complete
            println("RDI Bringup complete")

            // parameter exchange
            while(c.io.linkinit_sb_snd.peek().litValue != SideBandMessage.ADV_CAP.litValue){
                c.io.linkinit_fdi_pl_rxactive_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.active)
                c.io.active_entry.expect(false.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.NOP)
                c.clock.step(1)
            }
            // give adv_cap rcv
            c.io.linkinit_sb_rcv.poke(SideBandMessage.ADV_CAP)
            for(i <- 0 until 10){
                c.io.linkinit_fdi_pl_rxactive_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.active)
                c.io.active_entry.expect(false.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.ADV_CAP)
                c.clock.step(1)
            }
            // sideband send
            c.io.linkinit_sb_rdy.poke(true.B)
            c.clock.step(1)
            c.io.linkinit_sb_rdy.poke(false.B)
            c.clock.step(1)
            // FDI bring up
            // wait for FDI bring up begins
            while(c.io.linkinit_fdi_pl_inband_pres.peek().litValue != true.B.litValue){
                c.io.linkinit_fdi_pl_rxactive_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.active)
                c.io.active_entry.expect(false.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.NOP)
                c.clock.step(1)
            }
            // inband_pres = true -> begin FDI bring up
            // Condition one: protocol layer request first
            c.io.fdi_lp_state_req.poke(PhyStateReq.active)
            c.clock.step(1)
            // should send req sideband
            while(c.io.linkinit_sb_snd.peek().litValue != SideBandMessage.REQ_ACTIVE.litValue){
                c.io.linkinit_fdi_pl_rxactive_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(true.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.active)
                c.io.active_entry.expect(false.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.NOP)
                c.clock.step(1)
            }
            for(i <- 0 until 10){
                c.io.linkinit_fdi_pl_rxactive_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(true.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.active)
                c.io.active_entry.expect(false.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.REQ_ACTIVE)
                c.clock.step(1)
            }            
            c.io.linkinit_sb_rdy.poke(true.B)
            c.clock.step(1)
            c.io.linkinit_sb_rdy.poke(false.B)
            c.clock.step(1)
            // at the same time, request for active
            c.io.linkinit_sb_rcv.poke(SideBandMessage.REQ_ACTIVE)
            c.clock.step(1)            

            // wait for rx_active_req
            while(c.io.linkinit_fdi_pl_rxactive_req.peek().litValue != true.B.litValue){
                c.io.linkinit_fdi_pl_rxactive_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(true.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.active)
                c.io.active_entry.expect(false.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.NOP)
                c.clock.step(1)
            }
            // respons come back
            c.io.linkinit_sb_rcv.poke(SideBandMessage.RSP_ACTIVE)
            c.clock.step(1) 
            // module wait for active_sts
            for(i <- 0 until 10){
                c.io.linkinit_fdi_pl_rxactive_req.expect(true.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(true.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.active)
                c.io.active_entry.expect(false.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.NOP)
                c.clock.step(1)
            }
            c.io.fdi_lp_rxactive_sts.poke(true.B)
            c.clock.step(1)

            while(c.io.linkinit_sb_snd.peek().litValue != SideBandMessage.RSP_ACTIVE.litValue){
                c.io.linkinit_fdi_pl_rxactive_req.expect(true.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(true.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.active)
                c.io.active_entry.expect(false.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.RSP_ACTIVE)
                c.clock.step(1)
            }            
            // 

            c.io.linkinit_sb_rdy.poke(true.B)
            c.clock.step(1)
            c.io.linkinit_sb_rdy.poke(false.B)
            c.clock.step(1)

            for(i <- 0 until 10){
                c.io.linkinit_fdi_pl_rxactive_req.expect(true.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(true.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.active)
                c.io.active_entry.expect(true.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.NOP)
                c.clock.step(1)   
            }

            // leave reset
            c.io.link_state.poke(PhyState.active)
            c.clock.step(1)

            for(i <- 0 until 10){
                // should not give any signal
                c.io.linkinit_fdi_pl_rxactive_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.NOP)
                c.io.active_entry.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.nop)
                c.clock.step(1)
            }

            c.io.link_state.poke(PhyState.reset)
            c.io.rdi_pl_inband_pres.poke(false.B)
            c.clock.step(1)

            for(i <- 0 until 10){
                // should not give any signal
                c.io.linkinit_fdi_pl_rxactive_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_sb_snd.expect(SideBandMessage.NOP)
                c.io.active_entry.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(PhyStateReq.nop)
                c.clock.step(1)
            }
        }
    }
}
