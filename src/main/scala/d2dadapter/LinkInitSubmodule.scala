package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
import chisel3.util._
import interfaces._
import sideband._

class LinkInitSubmoduleIO (params: D2DAdapterParams) extends Bundle {
    val fdi_lp_state_req = Input(PhyStateReq())
    val fdi_lp_state_req_prev = Input(PhyStateReq())
    val fdi_lp_inband_pres = Input(Bool())

    val fdi_pl_rxactive_sts = Input(Bool())
    
    val linkinit_fdi_pl_inband_pres = Output(Bool())
    val linkinit_fdi_pl_rxactive_req = Output(Bool())
    val linkinit_fdi_pl_state_sts = Output(Bool())

    val rdi_pl_state_sts = Input(PhyState())
    val rdi_pl_inband_pres = Input(Bool())
    val linkinit_rdi_lp_state_req = Output(PhyStateReq())

    val link_state = Input(PhyState())
    val active_entry = Output(Bool())
    val linkinit_sb_snd = Decoupled(UInt(16.W))
    val linkinit_sb_rcv = Flipped(Valid(UInt(16.W)))
}

/**
  * LinkInitSubmodule handles the transition from Reset to Active for the
  * FDI/RDI state machines. Whenever the links moves from the reset state
  * they need to go through the link initialization i.e., the RDI bringup,
  * the parameter negotiations and the FDI bringup. This is handled inside
  * this linkInit submodule.
  * @param params
  */
class LinkInitSubmodule (params: D2DAdapterParams) extends Module {
    val io = IO(new LinkInitSubmoduleIO(params))

    // State register for link initialization 
    val linkinit_state_reg = RegInit(LinkInitState.INIT_START)

    // Parameter exchange on sideband message arbitration flags
    val param_exch_sbmsg_rcv_flag = RegInit(false.B)
    val param_exch_sbmsg_snt_flag = RegInit(false.B)

    when(io.link_state === PhyState.reset) {
      // Defaults
      io.active_entry := false.B
      io.linkinit_rdi_lp_state_req := PhyStateReq.NOP
      param_exch_sbmsg_rcv_flag := false.B
      param_exch_sbmsg_snt_flag := false.B

      switch(linkinit_state_reg) {
        // INIT START
        is(LinkInitState.INIT_START) {
            when(io.rdi_pl_inband_pres) {
              linkinit_state_reg := LinkInitState.RDI_BRINGUP
            }.otherwise {
              linkinit_state_reg := linkinit_state_reg
            }
        }
        // RDI BRINGUP
        is(LinkInitState.RDI_BRINGUP) {
            io.linkinit_rdi_lp_state_req := PhyStateReq.active
            when(io.rdi_pl_state_sts === PhyState.active) {
              linkinit_state_reg := LinkInitState.PARAM_EXCH
            }.otherwise {
              linkinit_state_reg := linkinit_state_reg
            }
        }
        // PARAMETER EXCHANGE
        is(LinkInitState.PARAM_EXCH) {
            // Send the AdvCap sbmsg to the partner link adapter
            // TODO: find a way to enable valid without probing ready
            when(io.linkinit_sb_snd.ready) {
                io.linkinit_sb_snd.valid := true.B
                io.linkinit_sb_snd.bits := SidebandMessage.ADV_CAP
                param_exch_sbmsg_snt_flag := true.B
            }.otherwise {
                io.linkinit_sb_snd.valid := false.B
                io.linkinit_sb_snd.bits := SidebandMessage.NOP
                param_exch_sbmsg_snt_flag := param_exch_sbmsg_snt_flag
            }
            
            // Check whether there is inflight AdvCap sbmsg from partner link adapter
            when(io.linkinit_sb_rcv.valid && 
                io.disabled_sb_rcv.bits === SidebandMessage.ADV_CAP){
                param_exch_sbmsg_rcv_flag := true.B
            }.otherwise {
                param_exch_sbmsg_rcv_flag := param_exch_sbmsg_rcv_flag
            }

            // Check if AdvCap was sent and received and vice versa
            when(param_exch_sbmsg_snt_flag && param_exch_sbmsg_rcv_flag) {
                linkinit_state_reg := LinkInitState.FDI_BRINGUP   
            }.otherwise {
                linkinit_state_reg := linkinit_state_reg
            }
        }
        // FDI BRINGUP
        is(LinkInitState.FDI_BRINGUP) {
            io.linkinit_fdi_pl_inband_pres := true.B
            when(io.fdi_lp_state_req === PhyStateReq.active) {
                            
            }
        }
        // INIT DONE
        is(LinkInitState.INIT_DONE) {
            active_entry := true.B
        }
      }
    }
}