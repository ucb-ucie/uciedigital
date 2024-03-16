package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import interfaces._
import sideband._

class LinkManagementControllerTest extends AnyFlatSpec with ChiselScalatestTester {
    val fdiParams = new FdiParams(width = 8, dllpWidth = 8, sbWidth = 32)
    val rdiParams = new RdiParams(width = 8, sbWidth = 32)
    val sbParams = new SidebandParams
    behavior of "LinkManagementController"
    it should "init and then link error" in {
        test(new LinkManagementController(fdiParams, rdiParams, sbParams)) { c => 
            //init
            val cycles_2us = 100
            c.io.fdi_lp_state_req.poke(PhyStateReq.nop)
            c.io.fdi_lp_linkerror.poke(false.B)
            c.io.rdi_pl_state_sts.poke(PhyState.reset)
            c.io.rdi_pl_inband_pres.poke(false.B)
            // c.io.rdi_pl_error.poke(false.B)
            // c.io.rdi_pl_cerror.poke(false.B)
            // c.io.rdi_pl_nerror.poke(false.B)
            // c.io.rdi_pl_trainerror.poke(false.B)
            // c.io.rdi_pl_phyinrecenter.poke(false.B)
            // c.io.rdi_pl_speedmode.poke(SpeedMode.SPEED_24_GT_S) 
            // c.io.rdi_pl_lnk_cfg.poke(LnkCfg.X32)
            c.io.linkmgmt_stalldone.poke(false.B)
            // c.io.parity_tx_sw_en.poke(false.B)
            // c.io.parity_rx_sw_en.poke(false.B)
            // c.io.cycles_2us.poke(cycles_2us.U)
            c.clock.step(1)
            for(i <- 0 until 10){
                // should not give any signal
                c.io.rdi_lp_state_req.expect(PhyStateReq.nop)
                c.io.fdi_pl_inband_pres.expect(false.B)
                c.io.fdi_pl_rx_active_req.expect(false.B)
                c.io.fdi_pl_state_sts.expect(PhyState.reset)
                c.io.sb_snd.expect(SideBandMessage.NOP)
                c.clock.step(1)
            }           
            // nothing
            for (i <- 0 until 5){

                //println("Start Link Init!!")
                c.io.rdi_pl_inband_pres.poke(true.B)
                c.clock.step(1)

                while(c.io.rdi_lp_state_req.peek().litValue != PhyStateReq.active.litValue){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.nop)
                    c.io.fdi_pl_inband_pres.expect(false.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.reset)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }
                // waiting for RDI to be active
                for(i <- 0 until 10){
                    // should not give any signal
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    c.io.fdi_pl_inband_pres.expect(false.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.reset)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }
                c.io.rdi_pl_state_sts.poke(PhyState.active)
                // RDI bring up complete

                // parameter exchange
                //println("Parameter Exchage!!")
                while(c.io.sb_snd.peek().litValue != SideBandMessage.ADV_CAP.litValue){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    c.io.fdi_pl_inband_pres.expect(false.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.reset)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }
                // give adv_cap rcv
                c.io.sb_rcv.poke(SideBandMessage.ADV_CAP)
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    c.io.fdi_pl_inband_pres.expect(false.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.reset)
                    c.io.sb_snd.expect(SideBandMessage.ADV_CAP)
                    c.clock.step(1)
                }
                // sideband send
                c.io.sb_rdy.poke(true.B)
                c.clock.step(1)
                c.io.sb_rdy.poke(false.B)
                c.clock.step(1)
                // FDI bring up
                // wait for FDI bring up begins
                while(c.io.fdi_pl_inband_pres.peek().litValue != true.B.litValue){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    c.io.fdi_pl_inband_pres.expect(false.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.reset)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.reset)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.io.fdi_pl_rx_active_req.expect(false.B)                    
                    c.clock.step(1)
                }
                // inband_pres = true -> begin FDI bring up
                // Condition one: protocol layer request first
                //println("FDI bring up!!")
                c.io.fdi_lp_state_req.poke(PhyStateReq.nop)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(PhyStateReq.active)
                c.clock.step(1)
                // should send req sideband
                //println("REQ SND!!")
                while(c.io.sb_snd.peek().litValue != SideBandMessage.REQ_ACTIVE.litValue){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.reset)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.reset)
                    c.io.sb_snd.expect(SideBandMessage.REQ_ACTIVE)
                    c.clock.step(1)
                }            
                c.io.sb_rdy.poke(true.B)
                c.clock.step(1)
                c.io.sb_rdy.poke(false.B)
                c.clock.step(1)
                // at the same time, request for active
                //println("REQ RCV!!")
                c.io.sb_rcv.poke(SideBandMessage.REQ_ACTIVE)
                c.clock.step(1)            

                // wait for rx_active_req
                while(c.io.fdi_pl_rx_active_req.peek().litValue != true.B.litValue){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.reset)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }
                // respons come back
                //println("RSP RCV!!")
                c.io.sb_rcv.poke(SideBandMessage.RSP_ACTIVE)
                c.clock.step(1) 
                // module wait for active_sts
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.reset)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }
                //println("rx active open!!")
                c.io.fdi_lp_rx_active_sts.poke(true.B)
                c.clock.step(1)

                while(c.io.sb_snd.peek().litValue != SideBandMessage.RSP_ACTIVE.litValue){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.reset)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }            
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.reset)
                    c.io.sb_snd.expect(SideBandMessage.RSP_ACTIVE)
                    c.clock.step(1)
                }
                //println("RSP SND!!")
                c.io.sb_rdy.poke(true.B)
                c.clock.step(1)
                c.io.sb_rdy.poke(false.B)
                c.clock.step(1)
                // should complete handshake to active
                while(c.io.fdi_pl_state_sts.peek().litValue != PhyState.active.litValue){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.reset)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }            
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.active)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }
                //println("ACTIVE!!")
                // link error
                c.io.fdi_lp_linkerror.poke(true.B)
                c.clock.step(1)

                // should request rdi to be linkerror
                //println("Wait for linkerror!!")
                while(c.io.rdi_lp_linkerror.peek().litValue != true.B.litValue){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.active)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }            
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.active)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }
                // rdi goes to link error
                c.io.rdi_pl_state_sts.poke(PhyState.linkError)
                c.clock.step(1)

                // should go to linkerror
                //println("go to linkerror!!")
                while(c.io.fdi_pl_state_sts.peek().litValue != PhyState.linkError.litValue){
                    //c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.active)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }            
                for(i <- 0 until 10){
                    //c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.linkError)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }

                // linkerror resolved
                c.io.fdi_lp_linkerror.poke(false.B)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(PhyStateReq.active)
                c.clock.step(1)
                // should request active to rdi
                //println("request active!!")
                while(c.io.rdi_lp_state_req.peek().litValue != PhyStateReq.active.litValue){
                    //c.io.rdi_lp_state_req.expect(PhyStateReq.nop)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.linkError)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }            
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.active)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.linkError)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }
                // rdi resets
                c.io.rdi_pl_state_sts.poke(PhyState.reset)
                c.clock.step(1)
                // should deassert rx
                while(c.io.fdi_pl_rx_active_req.peek().litValue != false.B.litValue){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.nop)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    //c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.linkError)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }            
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.nop)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.linkError)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }
                //
                c.io.fdi_lp_rx_active_sts.poke(false.B)
                c.clock.step(1)
                // should also goes to resets
                println("go to reset!!")
                while(c.io.fdi_pl_state_sts.peek().litValue != PhyState.reset.litValue){
                    c.io.rdi_lp_state_req.expect(PhyStateReq.nop)
                    //c.io.fdi_pl_inband_pres.expect(false.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(PhyState.linkError)
                    c.io.sb_snd.expect(SideBandMessage.NOP)
                    c.clock.step(1)
                }
            }
        }
    }
}
