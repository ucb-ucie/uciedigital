package ucie.d2dadapter

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class LinkInitSubmoduleTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "LinkInitSubmoduleTest"
    it should "bring up fdi and rdi for link initialization" in {
        val params = new D2DAdapterParams()
        test(new LinkInitSubmodule(params)) { c => 
            // init
            c.io.fdi_lp_state_req.poke(StateReq.NOP)
            c.io.fdi_lp_state_req_prev.poke(StateReq.NOP)
            c.io.fdi_lp_rx_active_sts.poke(false.B)
            c.io.rdi_pl_state_sts.poke(InterfaceStatus.RESET)
            c.io.rdi_pl_inband_pres.poke(false.B)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.io.linkinit_sideband_rdy.poke(false.B)
            c.io.link_state.poke(InterfaceStatus.RESET)
            c.clock.step(1)
            // start
            println("Test started")
            for(i <- 0 until 10){
                // should not give any signal
                c.io.linkinit_fdi_pl_rx_active_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(StateReq.NOP)
                c.io.init_complete.expect(false.B)
                c.io.linkinit_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)
            }

            // RDI bring up
            c.io.rdi_pl_inband_pres.poke(true.B)
            c.clock.step(1)
            while(c.io.linkinit_rdi_lp_state_req.peek().litValue != StateReq.ACTIVE.litValue){
                c.io.linkinit_fdi_pl_rx_active_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(StateReq.NOP)
                c.io.init_complete.expect(false.B)
                c.io.linkinit_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)
            }
            // waiting for RDI to be active
            for(i <- 0 until 10){
                // should not give any signal
                c.io.linkinit_fdi_pl_rx_active_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(StateReq.ACTIVE)
                c.io.init_complete.expect(false.B)
                c.io.linkinit_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)
            }
            c.io.rdi_pl_state_sts.poke(InterfaceStatus.ACTIVE)
            // RDI bring up complete

            // parameter exchange
            while(c.io.linkinit_sideband_snt.peek().litValue != SideBandMessageOP.ADV_CAP.litValue){
                c.io.linkinit_fdi_pl_rx_active_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(StateReq.ACTIVE)
                c.io.init_complete.expect(false.B)
                c.io.linkinit_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)
            }
            // give adv_cap rcv
            c.io.sideband_rcv.poke(SideBandMessageOP.ADV_CAP)
            for(i <- 0 until 10){
                c.io.linkinit_fdi_pl_rx_active_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(StateReq.ACTIVE)
                c.io.init_complete.expect(false.B)
                c.io.linkinit_sideband_snt.expect(SideBandMessageOP.ADV_CAP)
                c.clock.step(1)
            }
            // sideband send
            c.io.linkinit_sideband_rdy.poke(true.B)
            c.clock.step(1)
            c.io.linkinit_sideband_rdy.poke(false.B)
            c.clock.step(1)
            // FDI bring up
            // wait for FDI bring up begins
            while(c.io.linkinit_fdi_pl_inband_pres.peek().litValue != true.B.litValue){
                c.io.linkinit_fdi_pl_rx_active_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(StateReq.ACTIVE)
                c.io.init_complete.expect(false.B)
                c.io.linkinit_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)
            }
            // inband_pres = true -> begin FDI bring up
            // Condition one: protocol layer request first
            c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
            c.clock.step(1)
            // should send req sideband
            while(c.io.linkinit_sideband_snt.peek().litValue != SideBandMessageOP.REQ_ACTIVE.litValue){
                c.io.linkinit_fdi_pl_rx_active_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(true.B)
                c.io.linkinit_rdi_lp_state_req.expect(StateReq.ACTIVE)
                c.io.init_complete.expect(false.B)
                c.io.linkinit_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)
            }
            for(i <- 0 until 10){
                c.io.linkinit_fdi_pl_rx_active_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(true.B)
                c.io.linkinit_rdi_lp_state_req.expect(StateReq.ACTIVE)
                c.io.init_complete.expect(false.B)
                c.io.linkinit_sideband_snt.expect(SideBandMessageOP.REQ_ACTIVE)
                c.clock.step(1)
            }            
            c.io.linkinit_sideband_rdy.poke(true.B)
            c.clock.step(1)
            c.io.linkinit_sideband_rdy.poke(false.B)
            c.clock.step(1)
            // at the same time, request for active
            c.io.sideband_rcv.poke(SideBandMessageOP.REQ_ACTIVE)
            c.clock.step(1)            

            // wait for rx_active_req
            while(c.io.linkinit_fdi_pl_rx_active_req.peek().litValue != true.B.litValue){
                c.io.linkinit_fdi_pl_rx_active_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(true.B)
                c.io.linkinit_rdi_lp_state_req.expect(StateReq.ACTIVE)
                c.io.init_complete.expect(false.B)
                c.io.linkinit_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)
            }
            // respons come back
            c.io.sideband_rcv.poke(SideBandMessageOP.RSP_ACTIVE)
            c.clock.step(1) 
            // module wait for active_sts
            for(i <- 0 until 10){
                c.io.linkinit_fdi_pl_rx_active_req.expect(true.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(true.B)
                c.io.linkinit_rdi_lp_state_req.expect(StateReq.ACTIVE)
                c.io.init_complete.expect(false.B)
                c.io.linkinit_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)
            }
            c.io.fdi_lp_rx_active_sts.poke(true.B)
            c.clock.step(1)

            while(c.io.linkinit_sideband_snt.peek().litValue != SideBandMessageOP.RSP_ACTIVE.litValue){
                c.io.linkinit_fdi_pl_rx_active_req.expect(true.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(true.B)
                c.io.linkinit_rdi_lp_state_req.expect(StateReq.ACTIVE)
                c.io.init_complete.expect(false.B)
                c.io.linkinit_sideband_snt.expect(SideBandMessageOP.RSP_ACTIVE)
                c.clock.step(1)
            }            
            // 

            c.io.linkinit_sideband_rdy.poke(true.B)
            c.clock.step(1)
            c.io.linkinit_sideband_rdy.poke(false.B)
            c.clock.step(1)

            for(i <- 0 until 10){
                c.io.linkinit_fdi_pl_rx_active_req.expect(true.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(true.B)
                c.io.linkinit_rdi_lp_state_req.expect(StateReq.ACTIVE)
                c.io.init_complete.expect(true.B)
                c.io.linkinit_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)   
            }

            // leave reset
            c.io.link_state.poke(InterfaceStatus.ACTIVE)
            c.clock.step(1)
            c.io.link_state.poke(InterfaceStatus.RESET)
            c.clock.step(1)

            for(i <- 0 until 10){
                // should not give any signal
                c.io.linkinit_fdi_pl_rx_active_req.expect(false.B)
                c.io.linkinit_fdi_pl_inband_pres.expect(false.B)
                c.io.linkinit_rdi_lp_state_req.expect(StateReq.NOP)
                c.io.init_complete.expect(false.B)
                c.io.linkinit_sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)
            }
        }
    }
}
