/**  Truth table for deciding the Flit Format in which to operate if PCIe
or CXL protocols are negotiated, and none of the following are negotiated:
• Enhanced Multi_Protocol_Enable
• Standard 256B Start Header for PCIe protocol capability
• Latency-Optimized Flit with Optional Bytes for PCIe protocol capability **/

import chisel3._
import chisel3.util._
import chisel3.tester._
import chisel3.tester.RawTester.test
import edu.berkeley.cs.ucie.digital.interfaces._
// Bundle for FinCap.Adapter or MultiProtFinCap.Adapter bits: 
class finCapMultiProtFinCap extends Bundle {
    // 68 Flit Mode Bit:
    val sixtyEightFlitMode = 1.U
    // CXL 256B Flit Mode Bit:
    val cxlTwoFiftySixBFlitMode = 1.U
    // PCIe Flit Mode Bit:
    val PCIeFlitMode = 1.U
    // StreamingMode Bit:
    val streamingMode = 1.U

} 

// Bundle for FinCap.CXL bits:
class finCapCXL extends Bundle {
    // PCIe bit:
    val PCIeBit = 1.U
    // CXL.io Bit:
    val CXLioBit = 1.U
    

} 
class DecisionTable() extends Module {
  /** PCIe Non-flit Mode **/
  val IO = IO(Bundle{
  val fCMPFCInp = Input(new finCapMultiProtFinCapInput)
  val fCCXLInp = Input(new finCapCXLInput)
  val PCIe1_bool = Output(Bool)
  val PCIe2_bool = Output(Bool)
  val CXLI_bool = Output(Bool)
  val  CXLC_bool = Output(Bool)

  })


  //** Concatenated input **/
  val concat = Cat(fCMPFCInp.sixtyEightFlitMode
  , fCMPFCInp.cxlTwoFiftySixBFlitMode, fCMPFCInp.PCIeFlitMode
  , fCMPFCInp.streamingMode, fCCXLInp.PCIeBit, fCCXLInp.CXLioBit)
  
 

/** Non-flit Mode **/
  when(concat === BitPat("b100?10")) {
    PCIe1_bool := true
    PCIe2_bool := false
    CXLI_bool := false
    CXLC_bool := false

  } /** PCIe flit Mode **/
  .elsewhen(concat === BitPat("b101?10")) {
    PCIe1_bool := false
    PCIe2_bool := true
    CXLI_bool := false
    CXLC_bool := false
 
  } /** CXL Non - 256 Flit Mode */
  .elsewhen(concat === BitPat("b100?01")) {
    PCIe1_bool := false
    PCIe2_bool := false
    CXLI_bool := true
    CXLC_bool := false

  }  /** CXL 256 Flit Mode */
  .elsewhen(concat === BitPat("b111?01")) {
    PCIe1_bool := false
    PCIe2_bool := false
    CXLI_bool := false
    CXLC_bool := true

  } 
  .otherwise {
    PCIe1_bool := false
    PCIe2_bool := false
    CXLI_bool := false
    CXLC_bool := false
  }

}