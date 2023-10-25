package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
import chisel3.util._
import interfaces._

class LinkDisabledSubmoduleIO (params: D2DAdapterParams) extends Bundle {
    val fdi_lp_state_req = Input()
    val fdi_lp_state_req_prev = Input()
    val rdi_pl_state_sts = Input()
    val link_state = Input()
    val disabled_entry = Output(Bool())
    val disabled_sb_snd = Decoupled(UInt(16.W))
    val disabled_sb_rcv = Flipped(Valid(UInt(16.W)))
}

/**
  * LinkDisabledSubmodule handles the transition of FDI/RDI state machine
  * from Reset, Active, Retrain, and LinkReset to Disabled state. The 
  * transition is triggered by fdi_lp_state_req or though sideband messages
  * coming from partner link.
  * @param params
  */
class LinkDisabledSubmodule (params: D2DAdapterParams) extends Module {
    val io = IO(new LinkDisabledSubmoduleIO(params))

    val disabled_fdi_req_reg = RegInit(false.B)
    val disabled_sbmsg_int_req_reg = RegInit(false.B)
    val disabled_sbmsg_ext_req_reg = RegInit(false.B)
    
    when(io.link_state === PhyState.reset ||
         io.link_state === PhyState.active ||
         io.link_state === PhyState.retrain ||
         io.link_state === PhyState.linkReset) {

        io.disabled_entry := disabled_int_req_reg || disabled_sbmsg_int_req_reg ||
                             disabled_sbmsg_ext_req_reg
        
        // State change request by fdi
        when(io.link_state === PhyState.reset &&
             io.fdi_lp_state_req === PhyStateReq.disabled &&
             io.fdi_lp_state_req_prev === PhyState.nop) {
            disabled_fdi_req_reg := true.B
        }.elsewhen(io.fdi_lp_state_req === PhyStateReq.disabled &&
                   io.link_state =/= PhyState.reset) {
            disabled_fdi_req_reg := true.B
        }.otherwise{
            disabled_fdi_req_reg := disabled_fdi_req_reg
        }

        // TODO: State change requested with positive acknowledge from partner Die

        // TODO: State change requested by partner Die with positive response
    }.otherwise {

    }
}