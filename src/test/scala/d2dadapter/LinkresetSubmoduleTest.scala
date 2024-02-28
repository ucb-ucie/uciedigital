package ucie.d2dadapter

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

    // val fdi_lp_state_req = Input(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val linkreset_rdi_lp_state_req = Output(UInt(D2DAdapterSignalSize.STATE_WIDTH))

    // val linkreset_complete = Output(Bool())

    // val link_state = Input(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val sideband_rcv = Input(UInt(D2DAdapterSignalSize.SIDEBAND_MESSAGE_OP_WIDTH)) // sideband requested signals
    // val linkreset_sideband_snt = Output(UInt(D2DAdapterSignalSize.SIDEBAND_MESSAGE_OP_WIDTH)) // tell sideband module to send request of state change
    // val linkreset_sideband_rdy = Input(Bool())// sideband can consume the op in sideband_snt. 

class LinkresetSubmoduleTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "LinkresetSubmoduleTest"
    it should "complete disable flow as DP Adapter" in {
        val params = new D2DAdapterParams()
        test(new LinkresetSubmodule(params)) { c => 
            // init
            c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
            c.io.link_state.poke(InterfaceStatus.ACTIVE)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.io.linkreset_sideband_rdy.poke(false.B)

            c.clock.step(1)
            for(i <- 0 until 5){
                // fdi request linkreset
                c.io.fdi_lp_state_req.poke(StateReq.LINKRESET)
                c.clock.step(1)
                while(c.io.linkreset_sideband_snt.peek().litValue != SideBandMessageOP.REQ_LINKRESET.litValue){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)   
                }
                for(i <- 0 until 10){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.REQ_LINKRESET)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }
                // send req
                c.io.linkreset_sideband_rdy.poke(true.B)
                c.clock.step(1)
                c.io.linkreset_sideband_rdy.poke(false.B)
                c.clock.step(1)      
                //
                for(i <- 0 until 10){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }              
                // get rsp linkreset
                c.io.sideband_rcv.poke(SideBandMessageOP.RSP_LINKRESET)
                c.clock.step(1)
                c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
                c.clock.step(1)  

                while(c.io.linkreset_complete.peek().litValue == false.B.litValue){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)  
                }

                for(i <- 0 until 10){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(true.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }   
                // transition to linkreset
                c.io.link_state.poke(InterfaceStatus.LINKRESET)
                c.clock.step(1)
                // should request for rdi to linkreset
                for(i <- 0 until 10){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.LINKRESET)
                    c.clock.step(1)                   
                }   
                // transition to reset go again
                c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
                c.clock.step(100)  
                c.io.link_state.poke(InterfaceStatus.RESET)
                c.clock.step(100)     
                c.io.fdi_lp_state_req.poke(StateReq.NOP)
                c.io.linkreset_complete.expect(false.B) 
                c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
                c.io.linkreset_complete.expect(false.B) 
                c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(100)  
                c.io.link_state.poke(InterfaceStatus.ACTIVE)
                c.io.linkreset_complete.expect(false.B) 
                c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(100)  
                c.io.linkreset_complete.expect(false.B) 
                c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(100)  
            }
      
        }
    }

    it should "complete disable flow as UP Adapter" in {
        val params = new D2DAdapterParams()
        test(new LinkresetSubmodule(params)) { c => 
            // init
            c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
            c.io.link_state.poke(InterfaceStatus.ACTIVE)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.io.linkreset_sideband_rdy.poke(false.B)

            c.clock.step(1)
            for(i <- 0 until 5){
                // fdi request linkreset
                c.io.sideband_rcv.poke(SideBandMessageOP.REQ_LINKRESET)
                c.clock.step(1)
                c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
                c.clock.step(1)

                while(c.io.linkreset_sideband_snt.peek().litValue != SideBandMessageOP.RSP_LINKRESET.litValue){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)   
                }
                for(i <- 0 until 10){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.RSP_LINKRESET)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }
                // send rsp
                c.io.linkreset_sideband_rdy.poke(true.B)
                c.clock.step(1)
                c.io.linkreset_sideband_rdy.poke(false.B)
                c.clock.step(1)      

                while(c.io.linkreset_complete.peek().litValue == false.B.litValue){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)  
                }
                // complete linkreset
                for(i <- 0 until 10){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(true.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }   
                // transition to linkreset
                c.io.link_state.poke(InterfaceStatus.LINKRESET)
                c.clock.step(1)
                // no need to request for rdi
                for(i <- 0 until 10){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }   
                // transition to reset go again
                c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
                c.clock.step(100)  
                c.io.link_state.poke(InterfaceStatus.RESET)
                c.clock.step(100)     
                c.io.fdi_lp_state_req.poke(StateReq.NOP)
                c.io.linkreset_complete.expect(false.B) 
                c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
                c.io.linkreset_complete.expect(false.B) 
                c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(100)  
                c.io.link_state.poke(InterfaceStatus.ACTIVE)
                c.io.linkreset_complete.expect(false.B) 
                c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(100)  
                c.io.linkreset_complete.expect(false.B) 
                c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                c.clock.step(100)  
            }
      
        }
    }
    it should "complete linkreset flow as DP Adapter from reset" in {
        val params = new D2DAdapterParams()
        test(new LinkresetSubmodule(params)) { c => 
            // init
            c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
            c.io.fdi_lp_state_req_prev.poke(StateReq.ACTIVE)
            c.io.link_state.poke(InterfaceStatus.RESET)
            c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
            c.io.linkreset_sideband_rdy.poke(false.B)

            c.clock.step(1)
            for(i <- 0 until 5){
                // fdi request linkreset but not from NOP
                c.io.fdi_lp_state_req.poke(StateReq.LINKRESET)
                c.io.fdi_lp_state_req_prev.poke(StateReq.ACTIVE)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(StateReq.LINKRESET)
                c.io.fdi_lp_state_req_prev.poke(StateReq.LINKRESET)
                // should do nothing
                for(i <- 0 until 10){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }
                // give the correct transition
                c.io.fdi_lp_state_req.poke(StateReq.NOP)
                c.io.fdi_lp_state_req_prev.poke(StateReq.LINKRESET)
                c.clock.step(1) 
                c.io.fdi_lp_state_req.poke(StateReq.LINKRESET)
                c.io.fdi_lp_state_req_prev.poke(StateReq.NOP)
                c.clock.step(1)   
                c.io.fdi_lp_state_req.poke(StateReq.LINKRESET)
                c.io.fdi_lp_state_req_prev.poke(StateReq.LINKRESET)
                c.clock.step(1)        
                // 
                while(c.io.linkreset_sideband_snt.peek().litValue != SideBandMessageOP.REQ_LINKRESET.litValue){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)   
                }
                for(i <- 0 until 10){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.REQ_LINKRESET)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }
                // send req
                c.io.linkreset_sideband_rdy.poke(true.B)
                c.clock.step(1)
                c.io.linkreset_sideband_rdy.poke(false.B)
                c.clock.step(1)      
                //
                for(i <- 0 until 10){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }              
                // get rsp linkreset
                c.io.sideband_rcv.poke(SideBandMessageOP.RSP_LINKRESET)
                c.clock.step(1)
                c.io.sideband_rcv.poke(SideBandMessageOP.NOP)
                c.clock.step(1)  

                while(c.io.linkreset_complete.peek().litValue == false.B.litValue){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)  
                }

                for(i <- 0 until 10){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(true.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.NOP)
                    c.clock.step(1)                   
                }   
                // transition to linkreset
                c.io.link_state.poke(InterfaceStatus.LINKRESET)
                c.clock.step(1)
                // should request for rdi to linkreset
                for(i <- 0 until 10){
                    c.io.linkreset_sideband_snt.expect(SideBandMessageOP.NOP)
                    c.io.linkreset_complete.expect(false.B)
                    c.io.linkreset_rdi_lp_state_req.expect(StateReq.LINKRESET)
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
