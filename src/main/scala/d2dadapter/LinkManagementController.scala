package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
import chisel3.util._
import interfaces._

class LinkManagementControllerIO (val params: D2DAdapterParams) extends Bundle {
    val fdi = Flipped(new Fdi(params))
    val rdi = new Rdi(params)
    val sb = new Sb(params)
} 

/**
  * LinkManagementController for top level FDI/RDI state machine implementation, 
  * decoding the sideband messages and arbitration of triggers for the D2D adapter
  * state machine
  * @param params
  */
class LinkManagementController (val params: D2DAdapterParams) extends Module {
    val io = IO(new LinkManagementControllerIO(params))

    // Output registers

    // LinkError signal propagated to the PHY
    val rdi_lp_linkerror_reg = RegInit(PhyStateReq.nop)

    // FDI/RDI state register
    val link_state_reg = RegInit(PhyState.reset)

    // Top level IO signal assignments
    io.rdi.lpLinkError := rdi_lp_linkerror_reg
    io.fdi.plStateStatus := link_state_reg

    // LinkError propagation from Protocol layer to PHY
    rdi_lp_linkerror_reg := io.fdi.lpLinkError

    // FDI/RDI common state change triggers

    // LinkError logic
    // PHY informs the adapter over RDI that it is in linkError state
    val linkerror_phy_sts = io.rdi.plStateStatus == PhyState.linkError
    // Protocol initiates linkError through lp_linkerror assertion
    val linkerror_fdi_req = io.fdi.lpLinkError
    // Placeholder for any other internal request logic which can trigger linkError

    // TODO:RDI lp state request generation logic

    // FDI/RDI state machine. We use the same SM for optimized code as the spec
    // seems to trigger the state machines in tandem with no intermediate signalling 
    switch(link_state_reg) {
        // RESET
        is(PhyState.reset){
            when(linkerror_phy_sts || linkerror_fdi_req) {
                link_state_reg := PhyState.linkError
            }.elsewhen() {

            }.otherwise {
                link_state_reg := link_state_reg
            }
        }
        // ACTIVE
        is(PhyState.active) {
            when(linkerror_phy_sts || linkerror_fdi_req) {
                link_state_reg := PhyState.linkError
            }.elsewhen() {

            }.otherwise {
                link_state_reg := link_state_reg
            }
        }
        // RETRAIN
        is(PhyState.retrain) {
            when(linkerror_phy_sts || linkerror_fdi_req) {
                link_state_reg := PhyState.linkError
            }.elsewhen() {

            }.otherwise {
                link_state_reg := link_state_reg
            }
        }
        // LINKERROR
        is(PhyState.linkError) {
            // TODO: Check this logic, also needs state change on internal reset request
            // 
            when(io.fdi.lpStateReq === PhyStateReq.active && !linkerror_fdi_req &&
                (io.rdi.plStateStatus === PhyState.linkError)) {
                    link_state_reg := PhyState.reset
                }.otherwise {
                    link_state_reg := link_state_reg
                }
        }
        // DISABLED
        is(PhyState.disabled) {
            when(linkerror_phy_sts || linkerror_fdi_req) {
                link_state_reg := PhyState.linkError
            }.elsewhen() {

            }.otherwise {
                link_state_reg := link_state_reg
            }
        }
        // LINKRESET
        is(PhyState.linkReset) {
            when(linkerror_phy_sts || linkerror_fdi_req) {
                link_state_reg := PhyState.linkError
            }.elsewhen() {

            }.otherwise {
                link_state_reg := link_state_reg
            }
        }
    }
}