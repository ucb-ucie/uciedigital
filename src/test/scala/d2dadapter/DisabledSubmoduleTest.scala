package ucie.d2dadapter

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

    // val fdi_lp_state_req = Input(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val disabled_rdi_lp_state_req = Output(UInt(D2DAdapterSignalSize.STATE_WIDTH))

    // val disabled_complete = Output(Bool())

    // val link_state = Input(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val sideband_rcv = Input(UInt(D2DAdapterSignalSize.SIDEBAND_MESSAGE_OP_WIDTH)) // sideband requested signals
    // val disabled_sideband_snt = Output(UInt(D2DAdapterSignalSize.SIDEBAND_MESSAGE_OP_WIDTH)) // tell sideband module to send request of state change
    // val disabled_sideband_rdy = Input(Bool())// sideband can consume the op in sideband_snt. 

class DisabledSubmoduleTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "DisabledSubmoduleTest"
    it should "complete disable flow as DP Adapter" in {
        val params = new D2DAdapterParams()
        test(new DisabledSubmodule(params)) { c => 
            // init
            c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
            c.io.link_state.poke(InterfaceStatus.ACTIVE)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.io.disabled_sideband_rdy.poke(false.B)

            c.clock.step(1)
            for(i <- 0 until 5){
                // fdi request disabled
                c.io.fdi_lp_state_req.poke(StateReq.DISABLED)
                c.clock.step(1)
                while(c.io.disabled_sideband_snt.peek().litValue != SideBandMessageOP.REQ_DISABLED.litValue){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)   
                }
                for(i <- 0 until 10){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.REQ_DISABLED)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }
                // send req
                c.io.disabled_sideband_rdy.poke(true.B)
                c.clock.step(1)
                c.io.disabled_sideband_rdy.poke(false.B)
                c.clock.step(1)      
                //
                for(i <- 0 until 10){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }              
                // get rsp disabled
                c.io.sideband_rcv.poke(SideBandMessageOP.RSP_DISABLED)
                c.clock.step(1)
                c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
                c.clock.step(1)  

                while(c.io.disabled_complete.peek().litValue == false.B.litValue){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)  
                }

                for(i <- 0 until 10){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(true.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }   
                // transition to disabled
                c.io.link_state.poke(InterfaceStatus.DISABLED)
                c.clock.step(1)
                // should request for rdi to disabled
                for(i <- 0 until 10){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.DISABLED)
                    c.clock.step(1)                   
                }   
                // transition to reset go again
                c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
                c.clock.step(100)  
                c.io.link_state.poke(InterfaceStatus.RESET)
                c.clock.step(100)     
                c.io.fdi_lp_state_req.poke(StateReq.NOP)
                c.io.disabled_complete.expect(false.B) 
                c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
                c.io.disabled_complete.expect(false.B) 
                c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(100)  
                c.io.link_state.poke(InterfaceStatus.ACTIVE)
                c.io.disabled_complete.expect(false.B) 
                c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(100)  
                c.io.disabled_complete.expect(false.B) 
                c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(100)  
            }
      
        }
    }

    it should "complete disable flow as UP Adapter" in {
        val params = new D2DAdapterParams()
        test(new DisabledSubmodule(params)) { c => 
            // init
            c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
            c.io.link_state.poke(InterfaceStatus.ACTIVE)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.io.disabled_sideband_rdy.poke(false.B)

            c.clock.step(1)
            for(i <- 0 until 5){
                // fdi request disabled
                c.io.sideband_rcv.poke(SideBandMessageOP.REQ_DISABLED)
                c.clock.step(1)
                c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
                c.clock.step(1)

                while(c.io.disabled_sideband_snt.peek().litValue != SideBandMessageOP.RSP_DISABLED.litValue){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)   
                }
                for(i <- 0 until 10){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.RSP_DISABLED)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }
                // send rsp
                c.io.disabled_sideband_rdy.poke(true.B)
                c.clock.step(1)
                c.io.disabled_sideband_rdy.poke(false.B)
                c.clock.step(1)      

                while(c.io.disabled_complete.peek().litValue == false.B.litValue){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)  
                }
                // complete disabled
                for(i <- 0 until 10){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(true.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }   
                // transition to disabled
                c.io.link_state.poke(InterfaceStatus.DISABLED)
                c.clock.step(1)
                // no need to request for rdi
                for(i <- 0 until 10){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }   
                // transition to reset go again
                c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
                c.clock.step(100)  
                c.io.link_state.poke(InterfaceStatus.RESET)
                c.clock.step(100)     
                c.io.fdi_lp_state_req.poke(StateReq.NOP)
                c.io.disabled_complete.expect(false.B) 
                c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
                c.io.disabled_complete.expect(false.B) 
                c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(100)  
                c.io.link_state.poke(InterfaceStatus.ACTIVE)
                c.io.disabled_complete.expect(false.B) 
                c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(100)  
                c.io.disabled_complete.expect(false.B) 
                c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(100)  
            }
      
        }
    }

    it should "complete disable flow as DP Adapter from reset" in {
        val params = new D2DAdapterParams()
        test(new DisabledSubmodule(params)) { c => 
            // init
            c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
            c.io.fdi_lp_state_req_prev.poke(StateReq.ACTIVE)
            c.io.link_state.poke(InterfaceStatus.RESET)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.io.disabled_sideband_rdy.poke(false.B)

            c.clock.step(1)
            for(i <- 0 until 5){
                // fdi request disabled but not from NOP
                c.io.fdi_lp_state_req.poke(StateReq.DISABLED)
                c.io.fdi_lp_state_req_prev.poke(StateReq.ACTIVE)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(StateReq.DISABLED)
                c.io.fdi_lp_state_req_prev.poke(StateReq.DISABLED)
                // should do nothing
                for(i <- 0 until 10){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }
                // give the correct transition
                c.io.fdi_lp_state_req.poke(StateReq.NOP)
                c.io.fdi_lp_state_req_prev.poke(StateReq.DISABLED)
                c.clock.step(1) 
                c.io.fdi_lp_state_req.poke(StateReq.DISABLED)
                c.io.fdi_lp_state_req_prev.poke(StateReq.NOP)
                c.clock.step(1)   
                c.io.fdi_lp_state_req.poke(StateReq.DISABLED)
                c.io.fdi_lp_state_req_prev.poke(StateReq.DISABLED)
                c.clock.step(1)        
                // 
                while(c.io.disabled_sideband_snt.peek().litValue != SideBandMessageOP.REQ_DISABLED.litValue){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)   
                }
                for(i <- 0 until 10){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.REQ_DISABLED)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }
                // send req
                c.io.disabled_sideband_rdy.poke(true.B)
                c.clock.step(1)
                c.io.disabled_sideband_rdy.poke(false.B)
                c.clock.step(1)      
                //
                for(i <- 0 until 10){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }              
                // get rsp disabled
                c.io.sideband_rcv.poke(SideBandMessageOP.RSP_DISABLED)
                c.clock.step(1)
                c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
                c.clock.step(1)  

                while(c.io.disabled_complete.peek().litValue == false.B.litValue){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)  
                }

                for(i <- 0 until 10){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(true.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }   
                // transition to disabled
                c.io.link_state.poke(InterfaceStatus.DISABLED)
                c.clock.step(1)
                // should request for rdi to disabled
                for(i <- 0 until 10){
                    c.io.disabled_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.disabled_complete.expect(false.B)
                    c.io.disabled_rdi_lp_state_req.expect(StateReq.DISABLED)
                    c.clock.step(1)                   
                }   
                // transition to reset go again
                c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
                c.clock.step(100)  
                c.io.link_state.poke(InterfaceStatus.RESET)
                c.clock.step(100)     
                c.io.fdi_lp_state_req.poke(StateReq.NOP)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
                c.io.fdi_lp_state_req_prev.poke(StateReq.ACTIVE)
                c.clock.step(100)
            }
        }
    }
}
