import chisel3._
import chisel3.util._
import chisel3.tester._
import chisel3.tester.RawTester.test
import github.com/ucb-ucie/uciedigital/blob/main/src/main/scala/interfaces/Types.scala

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
  PCIe1_bool = Output(Bool)
  PCIe2_bool = Output(Bool)
  CXLI_bool = Output(Bool)
  CXLC_bool = Output(Bool)
  STREAM_bool = Output(Bool)
  })



  /** Non-flit Mode **/
  val PCIe1 = Value(Bits("10010"))

  
  /** PCIe flit Mode **/
  val PCIe2 = Value(Bits("10110"))

  /** CXL Non - 256 Flit Mode */
  val CXLI = Value(Bits("10001"))

  /** CXL 256 Flit Mode */
  val CXLC = Value(Bits("11101"))

  when(fCMPFCInp.sixtyEightFlitMode == 1 
  && fCMPFCInp.cxlTwoFiftySixBFlitMode == 0
  && fCMPFCInp.PCIeFlitMode == 0
  && fCCXLInp.PCIeBit == 1
  && fCCXLInp.CXLioBit == 0) {
    PCIe1_bool := true
    PCIe2_bool := false
    CXLI_bool := false
    CXLC_bool := false
    STREAM_bool := false
  } .elsewhen(fCMPFCInp.sixtyEightFlitMode == 1 
  && fCMPFCInp.cxlTwoFiftySixBFlitMode == 0
  && fCMPFCInp.PCIeFlitMode == 1
  && fCCXLInp.PCIeBit == 1
  && fCCXLInp.CXLioBit == 0) {
    PCIe1_bool := false
    PCIe2_bool := true
    CXLI_bool := false
    CXLC_bool := false
    STREAM_bool := false
  } .elsewhen(fCMPFCInp.sixtyEightFlitMode == 1 
  && fCMPFCInp.cxlTwoFiftySixBFlitMode == 0
  && fCMPFCInp.PCIeFlitMode == 0
  && fCCXLInp.PCIeBit == 0
  && fCCXLInp.CXLioBit == 1) {
    PCIe1_bool := false
    PCIe2_bool := false
    CXLI_bool := true
    CXLC_bool := false
    STREAM_bool := false
  } .elsewhen(fCMPFCInp.sixtyEightFlitMode == 1 
  && fCMPFCInp.cxlTwoFiftySixBFlitMode == 1
  && fCMPFCInp.PCIeFlitMode == 1
  && fCCXLInp.PCIeBit == 0
  && fCCXLInp.CXLioBit == 1) {
    PCIe1_bool := false
    PCIe2_bool := false
    CXLI_bool := false
    CXLC_bool := true
    STREAM_bool := false
  } .otherwise {
    PCIe1_bool := false
    PCIe2_bool := false
    CXLI_bool := false
    CXLC_bool := false
    STREAM_bool := true
  }

}