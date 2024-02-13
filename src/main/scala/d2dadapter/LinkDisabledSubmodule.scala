package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
import chisel3.util._
import interfaces._

class LinkDisabledSubmoduleIO(params: D2DAdapterParams) extends Bundle {
  val fdi_lp_state_req = Input(PhyStateReq())
  val fdi_lp_state_req_prev = Input(PhyStateReq())
  val link_state = Input(PhyState())
  val disabled_entry = Output(Bool())
  val disabled_sb_snd = Decoupled(UInt(16.W))
  val disabled_sb_rcv = Flipped(Valid(UInt(16.W)))
}

/** LinkDisabledSubmodule handles the transition of FDI/RDI state machine from
  * Reset, Active, Retrain, and LinkReset to Disabled state. The transition is
  * triggered by fdi_lp_state_req or though sideband messages coming from
  * partner link.
  * @param params
  */
class LinkDisabledSubmodule(params: D2DAdapterParams) extends Module {
  val io = IO(new LinkDisabledSubmoduleIO(params))

  val disabled_fdi_req_reg = RegInit(false.B)
  val disabled_sbmsg_req_rcv_flag = RegInit(false.B)
  val disabled_sbmsg_rsp_rcv_flag = RegInit(false.B)
  val disabled_sbmsg_ext_rsp_reg = RegInit(
    false.B,
  ) // receive and respond to sb disabled request
  val disabled_sbmsg_ext_req_reg = RegInit(
    false.B,
  ) // send and wait for sb disabled response

  when(
    io.link_state === PhyState.reset ||
      io.link_state === PhyState.active ||
      io.link_state === PhyState.retrain ||
      io.link_state === PhyState.linkReset,
  ) {

    io.disabled_entry := disabled_sbmsg_ext_rsp_reg ||
      disabled_sbmsg_ext_req_reg

    // State change request by fdi
    when(
      io.link_state === PhyState.reset &&
        io.fdi_lp_state_req === PhyStateReq.disabled &&
        io.fdi_lp_state_req_prev === PhyState.nop,
    ) {
      disabled_fdi_req_reg := true.B
    }.elsewhen(
      io.fdi_lp_state_req === PhyStateReq.disabled &&
        io.link_state =/= PhyState.reset,
    ) {
      disabled_fdi_req_reg := true.B
    }.otherwise {
      disabled_fdi_req_reg := disabled_fdi_req_reg
    }

    // Check whether there is inflight disabled request sbmsg from partner link
    when(
      io.disabled_sb_rcv.valid &&
        io.disabled_sb_rcv.bits === SidebandMessage.REQ_DISABLED,
    ) {
      disabled_sbmsg_req_rcv_flag := true.B
    }.otherwise {
      disabled_sbmsg_req_rcv_flag := disabled_sbmsg_req_rcv_flag
    }

    /* Check whether there is inflight disaabled response sbmsg from partner
     * link */
    when(
      io.disabled_sb_rcv.valid &&
        io.disabled_sb_rcv.bits === SidebandMessage.RSP_DISABLED,
    ) {
      disabled_sbmsg_rsp_rcv_flag := true.B
      disabled_sbmsg_ext_req_reg := true.B
    }.otherwise {
      disabled_sbmsg_rsp_rcv_flag := disabled_sbmsg_req_rcv_flag
      disabled_sbmsg_ext_req_reg := disabled_sbmsg_ext_req_reg
    }

    // TODO: Check if this logic works on all corner cases
    // lp_state_req triggers sideband message
    // TODO: find a way to enable valid without probing ready
    when(
      disabled_fdi_req_reg && io.disabled_sb_snd.ready &&
        !disabled_sbmsg_req_rcv_flag && !disabled_sbmsg_rsp_rcv_flag,
    ) {
      io.disabled_sb_snd.valid := true.B
      io.disabled_sb_snd.bits := SidebandMessage.REQ_DISABLED
      disabled_sbmsg_ext_rsp_reg := disabled_sbmsg_ext_rsp_reg
    }.elsewhen(io.disabled_sb_snd.ready && disabled_sbmsg_req_rcv_flag) {
      io.disabled_sb_snd.valid := true.B
      io.disabled_sb_snd.bits := SidebandMessage.RSP_DISABLED
      disabled_sbmsg_ext_rsp_reg := true.B
    }.otherwise {
      io.disabled_sb_snd.valid := false.B
      io.disabled_sb_snd.bits := SidebandMessage.NOP
      disabled_sbmsg_ext_rsp_reg := disabled_sbmsg_ext_rsp_reg
    }
  }.otherwise {
    disabled_fdi_req_reg := false.B
    disabled_sbmsg_req_rcv_flag := false.B
    disabled_sbmsg_rsp_rcv_flag := false.B
    disabled_sbmsg_ext_req_reg := false.B
    disabled_sbmsg_ext_rsp_reg := false.B
    io.disabled_entry := false.B
    io.disabled_sb_snd.valid := false.B
    io.disabled_sb_snd.bits := SidebandMessage.NOP
  }
}
