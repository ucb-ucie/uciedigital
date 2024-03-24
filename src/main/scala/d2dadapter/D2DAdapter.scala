package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
import chisel3.util._
import chisel3.experimental._

import interfaces._
import sideband._

class D2DAdapterIO (val fdiParams: FdiParams, val rdiParams: RdiParams) extends Bundle {
    val fdi = Flipped(new Fdi(fdiParams))
    val rdi = new Rdi(rdiParams)
}

/**
  * Top module for the D2D adapter which instantiates:
  * 1) LinkManagement Controller
  * 2) SB node
  * 3) MB node
  * 4) FDI and RDI stall handlers
  * @param fdiParams
  * @param rdiParams
  * @param sbParams
  */
class D2DAdapter(val fdiParams: FdiParams, val rdiParams: RdiParams, 
                 val sbParams: SidebandParams) extends Module {
    val io = IO(new D2DAdapterIO(fdiParams, rdiParams))

    assert(fdiParams.width == rdiParams.width)
    assert(fdiParams.sbWidth == rdiParams.sbWidth)

    val link_manager = Module(new LinkManagementController(fdiParams, rdiParams, sbParams))
    val fdi_stall_handler = Module(new FDIStallHandler())
    val rdi_stall_handler = Module(new RDIStallHandler())

    val d2d_sideband = Module(new D2DSidebandModule(fdiParams, sbParams))
    val d2d_mainband = Module(new D2DMainbandModule(fdiParams, rdiParams, sbParams))

    // link management controller
    // FDI interface
    link_manager.io.fdi_lp_state_req := io.fdi.lpStateReq
    link_manager.io.fdi_lp_linkerror := io.fdi.lpLinkError
    link_manager.io.fdi_lp_rx_active_sts := io.fdi.lpRxActiveStatus
    io.fdi.plStateStatus := link_manager.io.fdi_pl_state_sts
    io.fdi.plRxActiveReq := link_manager.io.fdi_pl_rx_active_req
    io.fdi.plInbandPres := link_manager.io.fdi_pl_inband_pres
    // RDI interface
    io.rdi.lpLinkError := link_manager.io.rdi_lp_linkerror
    io.rdi.lpStateReq:= link_manager.io.rdi_lp_state_req
    link_manager.io.rdi_pl_state_sts := io.rdi.plStateStatus
    link_manager.io.rdi_pl_inband_pres := io.rdi.plInbandPres

    // link manager <-> D2D sideband
    d2d_sideband.io.sideband_snt := link_manager.io.sb_snd
    link_manager.io.sb_rcv := d2d_sideband.io.sideband_rcv
    link_manager.io.sb_rdy := d2d_sideband.io.sideband_rdy
        
    // stall handler <-> LinkManagementController
    link_manager.io.linkmgmt_stalldone := fdi_stall_handler.io.linkmgmt_stalldone
    fdi_stall_handler.io.linkmgmt_stallreq := link_manager.io.linkmgmt_stallreq

    // Sideband 
    io.fdi.plConfig.bits := d2d_sideband.io.fdi_pl_cfg
    io.fdi.plConfig.valid := d2d_sideband.io.fdi_pl_cfg_vld
    d2d_sideband.io.fdi_pl_cfg_crd := io.fdi.plConfigCredit
    d2d_sideband.io.fdi_lp_cfg := io.fdi.lpConfig.bits
    d2d_sideband.io.fdi_lp_cfg_vld := io.fdi.lpConfig.valid
    io.fdi.lpConfigCredit := d2d_sideband.io.fdi_lp_cfg_crd

    d2d_sideband.io.rdi_pl_cfg := io.rdi.plConfig.bits
    d2d_sideband.io.rdi_pl_cfg_vld := io.rdi.plConfig.valid
    io.rdi.plConfigCredit := d2d_sideband.io.rdi_pl_cfg_crd
    io.rdi.lpConfig.bits := d2d_sideband.io.rdi_lp_cfg
    io.rdi.lpConfig.valid := d2d_sideband.io.rdi_lp_cfg_vld
    d2d_sideband.io.rdi_lp_cfg_crd := io.rdi.lpConfigCredit

    // stall handler
    io.fdi.plStallReq := fdi_stall_handler.io.fdi_pl_stallreq
    fdi_stall_handler.io.fdi_lp_stallack := io.fdi.lpStallAck

    rdi_stall_handler.io.rdi_pl_stallreq := io.rdi.plStallReq
    io.rdi.lpStallAck := rdi_stall_handler.io.rdi_lp_stallack

    // mainband module
    // FDI
    d2d_mainband.io.fdi_lp_irdy := io.fdi.lpData.irdy
    d2d_mainband.io.fdi_lp_valid := io.fdi.lpData.valid
    d2d_mainband.io.fdi_lp_data := io.fdi.lpData.bits
    d2d_mainband.io.fdi_lp_stream := io.fdi.lpStream
    io.fdi.lpData.ready := d2d_mainband.io.fdi_pl_trdy
    io.fdi.plData.valid := d2d_mainband.io.fdi_pl_valid
    io.fdi.plData.bits := d2d_mainband.io.fdi_pl_data
    io.fdi.plStream := d2d_mainband.io.fdi_pl_stream
    // RDI
    io.rdi.lpData.irdy := d2d_mainband.io.rdi_lp_irdy
    io.rdi.lpData.valid := d2d_mainband.io.rdi_lp_valid
    io.rdi.lpData.bits := d2d_mainband.io.rdi_lp_data
    d2d_mainband.io.rdi_pl_trdy := io.rdi.lpData.ready
    d2d_mainband.io.rdi_pl_valid := io.rdi.plData.valid
    d2d_mainband.io.rdi_pl_data := io.rdi.plData.bits

    d2d_mainband.io.d2d_state := link_manager.io.fdi_pl_state_sts

    // stall handler <-> mainband
    d2d_mainband.io.mainband_stallreq := rdi_stall_handler.io.mainband_stallreq
    rdi_stall_handler.io.mainband_stalldone := d2d_mainband.io.mainband_stalldone

    // d2d_mainband.io.snd_data = Output(Bits((8 * fdiParams.width).W))
    // d2d_mainband.io.snd_data_vld = Output(Bool())
    // d2d_mainband.io.rcv_data = Output(Bits((8 * rdiParams.width).W))
    // d2d_mainband.io.rcv_data_vld = Output(Bool())
    // d2d_mainband.io.parity_insert = Input(Bool())// need to send parity
    // d2d_mainband.io.parity_data = Input(Bits((8 * fdiParams.width).W))// the data needed to be sent first as parity
    // d2d_mainband.io.parity_rdy = Output(Bool()) // indicating that the parity data is sent, next can come in
    // d2d_mainband.io.parity_check = Input(Bool())
}