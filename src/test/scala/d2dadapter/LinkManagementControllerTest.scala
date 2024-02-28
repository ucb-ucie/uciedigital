package ucie.d2dadapter

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
    // val fdi_lp_state_req = Input(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val fdi_lp_linkerror = Input(Bool())

    // val fdi_pl_state_sts = Output(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val fdi_pl_inband_pres = Output(Bool())

    // val fdi_pl_error = Output(Bool())
    // val fdi_pl_cerror = Output(Bool())
    // val fdi_pl_nerror = Output(Bool())
    // val fdi_pl_trainerror = Output(Bool())
    // val fdi_pl_phyinrecenter = Output(Bool())
    // val fdi_pl_phyinl1 = Output(Bool())
    // val fdi_pl_phyinl2 = Output(Bool())
    // /*
    // */
    // val fdi_pl_rx_active_req = Output(Bool())
    // val fdi_lp_rx_active_sts = Input(Bool())
    // /*
    // */
    // val fdi_pl_protocol = Output(UInt(D2DAdapterSignalSize.PROTOCOL_ID_WIDTH))
    // val fdi_pl_protocol_flitfmt = Output(UInt(D2DAdapterSignalSize.PROTOCOL_FLITFMT_WIDTH))
    // val fdi_pl_protocol_vld = Output(Bool())
    // /*
    // Config related
    // */
    // val fdi_pl_speedmode = Output(UInt(D2DAdapterSignalSize.SPEED_MODE_WIDTH))
    // val fdi_pl_lnk_cfg = Output(UInt(D2DAdapterSignalSize.LNK_CFG_WIDTH))

    // /*
    // State related IO
    // */
    // val rdi_lp_state_req = Output(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val rdi_lp_linkerror = Output(Bool())
    // val rdi_pl_state_sts = Input(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val rdi_pl_inband_pres = Input(Bool())
    // val rdi_pl_error = Input(Bool())
    // val rdi_pl_cerror = Input(Bool())
    // val rdi_pl_nerror = Input(Bool())
    // val rdi_pl_trainerror = Input(Bool())
    // val rdi_pl_phyinrecenter = Input(Bool())
    // /*
    // Config related
    // */
    // val rdi_pl_speedmode = Input(UInt(D2DAdapterSignalSize.SPEED_MODE_WIDTH))
    // val rdi_pl_lnk_cfg = Input(UInt(D2DAdapterSignalSize.LNK_CFG_WIDTH))
    // /************************************
    // D2D internal signal
    // ************************************/
    // // val uc_error_status
    // // val uc_error_update
    // // val uc_error_mask
    // // val uc_error_severity
    // // val c_error_status
    // // val c_error_update
    // // val c_error_mask
    // // val c_error_severity    
    // // val log_data
    // // val log_offset
    // // val gate_allow
    // // val gate_active
    // val sideband_rcv = Input(UInt(D2DAdapterSignalSize.STATE_WIDTH)) // sideband requested signals
    // val sideband_snt = Output(UInt(D2DAdapterSignalSize.STATE_WIDTH)) // tell sideband module to send request of state change
    // val sideband_rdy = Input(Bool())// sideband can consume the op in sideband_snt. 
    // val cycles_2us = Input(UInt(32.W))// number of cycles to 1 us
    // // mainband
    // val stallhandler_stallreq = Output(Bool())
    // val stallhandler_stallcpt = Input(Bool())// 

    // // parity
    // val parity_tx_sw_en = Input(Bool())// if parity tx is enabled by software
    // val parity_rx_sw_en = Input(Bool())// if parity rx is enabled by software
    // val parity_rx_enable = Output(Bool())// tell parity if the parity should be used for receiving
    // val parity_tx_enable = Output(Bool())// tell parity if the parity should be used for sending
class LinkManagementControllerTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "LinkManagementController"
    it should "init and then link error" in {
        val params = new D2DAdapterParams()
        test(new LinkManagementController(params)) { c => 
            //init
            val cycles_2us = 100
            c.io.fdi_lp_state_req.poke(StateReq.NOP)
            c.io.fdi_lp_linkerror.poke(false.B)
            c.io.rdi_pl_state_sts.poke(InterfaceStatus.RESET)
            c.io.rdi_pl_inband_pres.poke(false.B)
            c.io.rdi_pl_error.poke(false.B)
            c.io.rdi_pl_cerror.poke(false.B)
            c.io.rdi_pl_nerror.poke(false.B)
            c.io.rdi_pl_trainerror.poke(false.B)
            c.io.rdi_pl_phyinrecenter.poke(false.B)
            c.io.rdi_pl_speedmode.poke(SpeedMode.SPEED_24_GT_S) 
            c.io.rdi_pl_lnk_cfg.poke(LnkCfg.X32)
            c.io.stallhandler_stallcpt.poke(false.B)
            c.io.parity_tx_sw_en.poke(false.B)
            c.io.parity_rx_sw_en.poke(false.B)
            c.io.cycles_2us.poke(cycles_2us.U)
            c.clock.step(1)
            for(i <- 0 until 10){
                // should not give any signal
                c.io.rdi_lp_state_req.expect(StateReq.NOP)
                c.io.fdi_pl_inband_pres.expect(false.B)
                c.io.fdi_pl_rx_active_req.expect(false.B)
                c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                c.clock.step(1)
            }           
            // nothing
            for (i <- 0 until 5){

                //println("Start Link Init!!")
                c.io.rdi_pl_inband_pres.poke(true.B)
                c.clock.step(1)

                while(c.io.rdi_lp_state_req.peek().litValue != StateReq.ACTIVE.litValue){
                    c.io.rdi_lp_state_req.expect(StateReq.NOP)
                    c.io.fdi_pl_inband_pres.expect(false.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }
                // waiting for RDI to be active
                for(i <- 0 until 10){
                    // should not give any signal
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    c.io.fdi_pl_inband_pres.expect(false.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }
                c.io.rdi_pl_state_sts.poke(InterfaceStatus.ACTIVE)
                // RDI bring up complete

                // parameter exchange
                //println("Parameter Exchage!!")
                while(c.io.sideband_snt.peek().litValue != SideBandMessageOP.ADV_CAP.litValue){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    c.io.fdi_pl_inband_pres.expect(false.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }
                // give adv_cap rcv
                c.io.sideband_rcv.poke(SideBandMessageOP.ADV_CAP)
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    c.io.fdi_pl_inband_pres.expect(false.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                    c.io.sideband_snt.expect(SideBandMessageOP.ADV_CAP)
                    c.clock.step(1)
                }
                // sideband send
                c.io.sideband_rdy.poke(true.B)
                c.clock.step(1)
                c.io.sideband_rdy.poke(false.B)
                c.clock.step(1)
                // FDI bring up
                // wait for FDI bring up begins
                while(c.io.fdi_pl_inband_pres.peek().litValue != true.B.litValue){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    c.io.fdi_pl_inband_pres.expect(false.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }
                // inband_pres = true -> begin FDI bring up
                // Condition one: protocol layer request first
                //println("FDI bring up!!")
                c.io.fdi_lp_state_req.poke(StateReq.NOP)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
                c.clock.step(1)
                // should send req sideband
                //println("REQ SND!!")
                while(c.io.sideband_snt.peek().litValue != SideBandMessageOP.REQ_ACTIVE.litValue){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                    c.io.sideband_snt.expect(SideBandMessageOP.REQ_ACTIVE)
                    c.clock.step(1)
                }            
                c.io.sideband_rdy.poke(true.B)
                c.clock.step(1)
                c.io.sideband_rdy.poke(false.B)
                c.clock.step(1)
                // at the same time, request for active
                //println("REQ RCV!!")
                c.io.sideband_rcv.poke(SideBandMessageOP.REQ_ACTIVE)
                c.clock.step(1)            

                // wait for rx_active_req
                while(c.io.fdi_pl_rx_active_req.peek().litValue != true.B.litValue){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }
                // respons come back
                //println("RSP RCV!!")
                c.io.sideband_rcv.poke(SideBandMessageOP.RSP_ACTIVE)
                c.clock.step(1) 
                // module wait for active_sts
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }
                //println("rx active open!!")
                c.io.fdi_lp_rx_active_sts.poke(true.B)
                c.clock.step(1)

                while(c.io.sideband_snt.peek().litValue != SideBandMessageOP.RSP_ACTIVE.litValue){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }            
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                    c.io.sideband_snt.expect(SideBandMessageOP.RSP_ACTIVE)
                    c.clock.step(1)
                }
                //println("RSP SND!!")
                c.io.sideband_rdy.poke(true.B)
                c.clock.step(1)
                c.io.sideband_rdy.poke(false.B)
                c.clock.step(1)
                // should complete handshake to active
                while(c.io.fdi_pl_state_sts.peek().litValue != InterfaceStatus.ACTIVE.litValue){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.RESET)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }            
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.ACTIVE)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }
                //println("ACTIVE!!")
                // link error
                c.io.fdi_lp_linkerror.poke(true.B)
                c.clock.step(1)

                // should request rdi to be linkerror
                //println("Wait for linkerror!!")
                while(c.io.rdi_lp_linkerror.peek().litValue != true.B.litValue){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.ACTIVE)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }            
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.ACTIVE)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }
                // rdi goes to link error
                c.io.rdi_pl_state_sts.poke(InterfaceStatus.LINKERROR)
                c.clock.step(1)

                // should go to linkerror
                //println("go to linkerror!!")
                while(c.io.fdi_pl_state_sts.peek().litValue != InterfaceStatus.LINKERROR.litValue){
                    //c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.ACTIVE)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }            
                for(i <- 0 until 10){
                    //c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.LINKERROR)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }

                // linkerror resolved
                c.io.fdi_lp_linkerror.poke(false.B)
                c.clock.step(1)
                c.io.fdi_lp_state_req.poke(StateReq.ACTIVE)
                c.clock.step(1)
                // should request active to rdi
                //println("request active!!")
                while(c.io.rdi_lp_state_req.peek().litValue != StateReq.ACTIVE.litValue){
                    //c.io.rdi_lp_state_req.expect(StateReq.NOP)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.LINKERROR)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }            
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(StateReq.ACTIVE)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.LINKERROR)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }
                // rdi resets
                c.io.rdi_pl_state_sts.poke(InterfaceStatus.RESET)
                c.clock.step(1)
                // should deaccert rx
                while(c.io.fdi_pl_rx_active_req.peek().litValue != false.B.litValue){
                    c.io.rdi_lp_state_req.expect(StateReq.NOP)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    //c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.LINKERROR)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }            
                for(i <- 0 until 10){
                    c.io.rdi_lp_state_req.expect(StateReq.NOP)
                    // c.io.fdi_pl_inband_pres.expect(true.B)
                    c.io.fdi_pl_rx_active_req.expect(false.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.LINKERROR)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }
                //
                c.io.fdi_lp_rx_active_sts.poke(false.B)
                c.clock.step(1)
                // should also goes to resets
                //println("go to reset!!")
                while(c.io.fdi_pl_state_sts.peek().litValue != InterfaceStatus.RESET.litValue){
                    c.io.rdi_lp_state_req.expect(StateReq.NOP)
                    //c.io.fdi_pl_inband_pres.expect(false.B)
                    // c.io.fdi_pl_rx_active_req.expect(true.B)
                    c.io.fdi_pl_state_sts.expect(InterfaceStatus.LINKERROR)
                    c.io.sideband_snt.expect(SideBandMessageOP.NOP)
                    c.clock.step(1)
                }                     
            }
        }
    }
}
