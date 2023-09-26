package ucie.interfaces

import chisel3._
import chisel3.util._
import edu.berkeley.cs.ucie.digital.Decoupled3 // remove this when Decoupled3 is moved to interfaces

/** The Flit-aware D2D interface (FDI), from the perspective of the Protocol
  * layer.
  */
class Fdi(width: Int, sbWidth: Int) extends Bundle {

  /** lp_*: protocol to d2d adapter pl_*: d2d adapter to protocol
    */

  /** Protocol Layer to Adapter data.
    *
    * Encompasses lp_irdy, lp_valid, and pl_trdy from the UCIe specification.
    */
  val lpData = Decoupled3(Vec(width, UInt(8.W)))

  /** Adapter to Protocol Layer data.
    *
    * Encompasses `pl_valid` and `pl_data` from the UCIe specification. Note
    * that backpressure is not possible. Data should be sampled whenever valid
    * is asserted at a clock edge.
    */
  val plData = Flipped(Valid(Vec(width, UInt(8.W))))

  /** Protocol Layer to Adapter signal that indicates the stream ID to use with
    * data. Each stream ID maps to a unique protocol and stack
    */
  val lpStream = Output(ProtoStream())

  /** Adapter to Protocol Layer signal that indicates the stream ID to use with
    * data. Each stream ID maps to a unique protocol and stack
    */
  val plStream = Input(ProtoStream())

  /** When asserted at a rising clock edge, it indicates a single credit return
    * from the Protocol Layer to the Adapter for the Retimer Receiver buffers.
    * Each credit corresponds to 256B of mainband data. This signal must NOT
    * assert for dies that are not UCIe Retimers.
    */
  val lpRetimerCrd = Output(Bool())

  /** When asserted at a rising clock edge, it indicates a single credit return
    * from the Retimer to the Adapter. Each credit corresponds to 256B of
    * mainband data. This signal must NOT assert if the remote Link partner is
    * not a Retimer.
    */
  val plRetimerCrd = Input(Bool())

  /** Protocol layer request to Adapter to request state change. */
  val lpStateReq = Output(ProtoStateReq())

  /** Protocol Layer to Adapter indication that an error has occurred which
    * requires the Link to go down. Adapter must propagate this request to RDI,
    * and move the Adapter LSMs (and CXL vLSMs if applicable) to LinkError state
    * once RDI is in LinkError state. It must stay there as long as
    * lp_linkerror=1. The reason for having this be an indication decoupled from
    * regular state transitions is to allow immediate action on part of the
    * Protocol Layer and Adapter in order to provide the quickest path for error
    * containment when applicable (for example, a viral error escalation could
    * map to the LinkError state)
    */
  val lpLinkError = Output(Bool())

  /** Adapter to Protocol Layer Status indication of the Interface.
    *
    * The status signal is permitted to transition from Adapter autonomously
    * when applicable. For example the Adapter asserts the Retrain status when
    * it decides to enter retraining either autonomously or when requested by
    * remote agent.
    */
  val plStateStatus = Input(ProtoState())

  val plInbandPres = Input(Bool())
  val plError = Input(Bool())
  val plCorrectableError = Input(Bool())
  val plNonFatalError = Input(Bool())
  val plTrainError = Input(Bool())

  val plRxActiveReq = Input(Bool())
  val lpRxActiveStatus = Output(Bool())

  val plProto = Input(ProtoType())
  val plProtoFlitFmt = Input(ProtoFlitFmt())
  val plProtoValid = Input(Bool())

  val plStallReq = Input(Bool())
  val lpStallAck = Output(Bool())
  val plPhyInRecenter = Input(Bool())
  val plPhyinL1 = Input(Bool())
  val plPhyinL2 = Input(Bool())
  val plSpeedMode = Input(SpeedMode())
  val plLinkWidth = Input(PhyWidth())

  // Tie to 1 if clock gating not supported.
  val plClkReq = Input(Bool())
  val lpClkAck = Output(Bool())

  // Tie to 1 if clock gating not supported.
  val lpWakeReq = Output(Bool())
  val plWakeAck = Input(Bool())

  val plConfig = Flipped(Valid(UInt(sbWidth.W)))
  val plConfigCredit = Input(Bool())
  val lpConfig = Valid(UInt(sbWidth.W))
  val lpConfigCredit = Output(Bool())
}

object ProtoStream extends ChiselEnum {
  val stack0PCIe = Value(0x1.U(8.W))
  val stack0CXLI = Value(0x2.U(8.W))
  val stack0CXLC = Value(0x3.U(8.W))
  val stack0Stream = Value(0x4.U(8.W))
  val stack1PCIe = Value(0x11.U(8.W))
  val stack1CXLI = Value(0x12.U(8.W))
  val stack1CXLC = Value(0x13.U(8.W))
  val stack1Stream = Value(0x14.U(8.W))
}

object ProtoState extends ChiselEnum {
  val reset = Value(0x0.U(4.W))
  val active = Value(0x1.U(4.W))
  val activePmNak = Value(0x3.U(4.W))
  val l1 = Value(0x4.U(4.W))
  val l2 = Value(0x8.U(4.W))
  val linkReset = Value(0x9.U(4.W))
  val linkError = Value(0xa.U(4.W))
  val retrain = Value(0xb.U(4.W))
  val disabled = Value(0xc.U(4.W))
}

object ProtoStateReq extends ChiselEnum {
  val nop = Value(0x0.U(4.W))
  val active = Value(0x1.U(4.W))
  val l1 = Value(0x4.U(4.W))
  val l2 = Value(0x8.U(4.W))
  val linkReset = Value(0x9.U(4.W))
  val retrain = Value(0xb.U(4.W))
  val disabled = Value(0xc.U(4.W))
}

object ProtoType extends ChiselEnum {
  val pcie = Value(0x0.U(3.W))
  val cxl1 = Value(0x3.U(3.W))
  val cxl2 = Value(0x4.U(3.W))
  val cxl3 = Value(0x5.U(3.W))
  val cxl4 = Value(0x6.U(3.W))
  val stream = Value(0x7.U(3.W))
}

object ProtoFlitFmt extends ChiselEnum {
  val flitRaw = Value(0x1.U(4.W))
  val flit68 = Value(0x2.U(4.W))
  val flitStd256EndHead = Value(0x3.U(4.W))
  val flitStd256StartHead = Value(0x4.U(4.W))
  val flitOpt256NoBytesFlit = Value(0x5.U(4.W))
  val flitOpt256BytesFlit = Value(0x6.U(4.W))
}

object SpeedMode extends ChiselEnum {
  val speed4 = Value(0x0.U(3.W))
  val speed8 = Value(0x1.U(3.W))
  val speed12 = Value(0x2.U(3.W))
  val speed16 = Value(0x3.U(3.W))
  val speed24 = Value(0x4.U(3.W))
  val speed32 = Value(0x5.U(3.W))
}

object PhyWidth extends ChiselEnum {
  val width8 = Value(0x1.U(3.W))
  val width16 = Value(0x2.U(3.W))
  val width32 = Value(0x3.U(3.W))
  val width64 = Value(0x4.U(3.W))
  val width128 = Value(0x5.U(3.W))
  val width256 = Value(0x6.U(3.W))
}
