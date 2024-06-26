package edu.berkeley.cs.ucie.digital
package d2dadapter

// import chisel3._
// import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import interfaces._
import sideband._

    // val fdi_lp_state_req = Input(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val disabled_rdi_lp_state_req = Output(UInt(D2DAdapterSignalSize.STATE_WIDTH))

    // val disabled_complete = Output(Bool())

    // val link_state = Input(UInt(D2DAdapterSignalSize.STATE_WIDTH))
    // val sideband_rcv = Input(UInt(D2DAdapterSignalSize.SIDEBAND_MESSAGE_OP_WIDTH)) // sideband requested signals
    // val disabled_sideband_snt = Output(UInt(D2DAdapterSignalSize.SIDEBAND_MESSAGE_OP_WIDTH)) // tell sideband module to send request of state change
    // val disabled_sideband_rdy = Input(Bool())// sideband can consume the op in sideband_snt. 

class D2DSidebandModuleTest extends AnyFlatSpec with ChiselScalatestTester {
    val fdiParams = new FdiParams(width = 8, dllpWidth = 8, sbWidth = 32)
    val sbParams = new SidebandParams
    behavior of "D2DSidebandModuleTest"
    it should "don't know how to check" in {
        test(new D2DSidebandModule(fdiParams, sbParams)) { c => 
            println("Do nothing")
        }
    }
}