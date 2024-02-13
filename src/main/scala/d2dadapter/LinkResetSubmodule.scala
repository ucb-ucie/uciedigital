package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
import chisel3.util._
import interfaces._

class LinkResetSubmoduleIO(params: D2DAdapterParams) extends Bundle {
  val fdi_lp_state_req = Input(PhyStateReq())
  val fdi_lp_state_req_prev = Input(PhyStateReq())
  val link_state = Input(PhyState())
  val linkreset_entry = Output(Bool())
  val linkreset_sb_snd = Decoupled(UInt(16.W))
  val linkreset_sb_rcv = Flipped(Valid(UInt(16.W)))
}

/** LinkResetSubmodule handles the transition of FDI/RDI state machine from
  * Reset, Active, and Retrain to LinkReset state. The transition is triggered
  * by fdi_lp_state_req or though sideband messages coming from partner link.
  * @param params
  */
// This is a modified copy of LinkResetSubmodule.scala file
class LinkResetSubmodule(params: D2DAdapterParams) extends Module {
  val io = IO(new LinkResetSubmoduleIO(params))

  val linkreset_fdi_req_reg = RegInit(false.B)
  val linkreset_sbmsg_req_rcv_flag = RegInit(false.B)
  val linkreset_sbmsg_rsp_rcv_flag = RegInit(false.B)
  val linkreset_sbmsg_ext_rsp_reg = RegInit(
    false.B,
  ) // receive and respond to sb linkreset request
  val linkreset_sbmsg_ext_req_reg = RegInit(
    false.B,
  ) // send and wait for sb linkreset response

  when(
    io.link_state === PhyState.reset ||
      io.link_state === PhyState.active ||
      io.link_state === PhyState.retrain,
  ) {

    io.linkreset_entry := linkreset_sbmsg_ext_rsp_reg ||
      linkreset_sbmsg_ext_req_reg

    // State change request by fdi
    when(
      io.link_state === PhyState.reset &&
        io.fdi_lp_state_req === PhyStateReq.linkReset &&
        io.fdi_lp_state_req_prev === PhyState.nop,
    ) {
      linkreset_fdi_req_reg := true.B
    }.elsewhen(
      io.fdi_lp_state_req === PhyStateReq.linkReset &&
        io.link_state =/= PhyState.reset,
    ) {
      linkreset_fdi_req_reg := true.B
    }.otherwise {
      linkreset_fdi_req_reg := linkreset_fdi_req_reg
    }

    // Check whether there is inflight linkreset request sbmsg from partner link
    when(
      io.linkreset_sb_rcv.valid &&
        io.linkreset_sb_rcv.bits === SidebandMessage.REQ_LINKRESET,
    ) {
      linkreset_sbmsg_req_rcv_flag := true.B
    }.otherwise {
      linkreset_sbmsg_req_rcv_flag := linkreset_sbmsg_req_rcv_flag
    }

    /* Check whether there is inflight disaabled response sbmsg from partner
     * link */
    when(
      io.linkreset_sb_rcv.valid &&
        io.linkreset_sb_rcv.bits === SidebandMessage.RSP_LINKRESET,
    ) {
      linkreset_sbmsg_rsp_rcv_flag := true.B
      linkreset_sbmsg_ext_req_reg := true.B
    }.otherwise {
      linkreset_sbmsg_rsp_rcv_flag := linkreset_sbmsg_req_rcv_flag
      linkreset_sbmsg_ext_req_reg := linkreset_sbmsg_ext_req_reg
    }

    // TODO: Check if this logic works on all corner cases
    // lp_state_req triggers sideband message
    when(
      linkreset_fdi_req_reg && io.linkreset_sb_snd.ready &&
        !linkreset_sbmsg_req_rcv_flag && !linkreset_sbmsg_rsp_rcv_flag,
    ) {
      io.linkreset_sb_snd.valid := true.B
      io.linkreset_sb_snd.bits := SidebandMessage.REQ_LINKRESET
      linkreset_sbmsg_ext_rsp_reg := linkreset_sbmsg_ext_rsp_reg
    }.elsewhen(io.linkreset_sb_snd.ready && linkreset_sbmsg_req_rcv_flag) {
      io.linkreset_sb_snd.valid := true.B
      io.linkreset_sb_snd.bits := SidebandMessage.RSP_LINKRESET
      linkreset_sbmsg_ext_rsp_reg := true.B
    }.otherwise {
      io.linkreset_sb_snd.valid := false.B
      io.linkreset_sb_snd.bits := SidebandMessage.NOP
      linkreset_sbmsg_ext_rsp_reg := linkreset_sbmsg_ext_rsp_reg
    }
  }.otherwise {
    linkreset_fdi_req_reg := false.B
    linkreset_sbmsg_req_rcv_flag := false.B
    linkreset_sbmsg_rsp_rcv_flag := false.B
    linkreset_sbmsg_ext_req_reg := false.B
    linkreset_sbmsg_ext_rsp_reg := false.B
    io.linkreset_entry := false.B
    io.linkreset_sb_snd.valid := false.B
    io.linkreset_sb_snd.bits := SidebandMessage.NOP
  }
}
