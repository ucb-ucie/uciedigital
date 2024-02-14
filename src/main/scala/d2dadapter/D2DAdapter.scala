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

    val link_manager = Module(new LinkManagementController(fdiParams, rdiParams, sbParams))
    val fdi_stall_handler = Module(new FDIStallHandler(fdiParams, rdiParams))
    val rdi_stall_handler = Module(new RDIStallHandler(fdiParams, rdiParams))

    val d2d_sideband = Module(new D2DSidebandModule(fdiParams, sbParams))
    val d2d_mainband = Module(new D2DMainbandModule(fdiParams, sbParams))

}