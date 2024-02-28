package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
import chisel3.util._

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

    // stall
    io.fdi.plStallReq := fdi_stall_handler.io.fdi_pl_stallreq
    fdi_stall_handler.io.fdi_lp_stallack := io.fdi.lpStallAck

    rdi_stall_handler.io.rdi_pl_stallreq := io.rdi.plStallReq
    io.rdi.lpStallAck := rdi_stall_handler.io.rdi_lp_stallack

    // link management controller
    // stall handler
    link_manager.io.linkmgmt_stalldone := fdi_stall_handler.io.linkmgmt_stalldone
    fdi_stall_handler.io.linkmgmt_stallreq := link_manager.io.linkmgmt_stallreq

    // mainband module
    // stall handler
    d2d_mainband.io.mainband_stallreq := rdi_stall_handler.io.mainband_stallreq
    rdi_stall_handler.io.mainband_stalldone := d2d_mainband.io.mainband_stalldone
}