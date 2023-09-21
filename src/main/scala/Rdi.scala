package edu.berkeley.cs.ucie.digital

import chisel3._
import chisel3.util._

/** The raw D2D interface (RDI), from the perspective of the D2D Adapter. */
class Rdi(width: Int, sbWidth: Int) extends Bundle {

  /** Adapter to Physical Layer data.
    *
    * Encompasses lp_irdy, lp_valid, and pl_trdy from the UCIe specification.
    */
  val lpData = Decoupled3(Vec(width, UInt(8.W)))

  /** Physical Layer to Adapter data.
    *
    * Encompasses `pl_valid` and `pl_data` from the UCIe specification. Note
    * that backpressure is not possible. Data should be sampled whenever valid
    * is asserted at a clock edge.
    */
  val plData = Flipped(Valid(Vec(width, UInt(8.W))))

  /** When asserted at a rising clock edge, it indicates a single credit return
    * from the Adapter to the Physical Layer for the Retimer Receiver buffers.
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

  /** Adapter request to Physical Layer to request state change. */
  val lpStateReq = Output(PhyStateReq())

  /** Adapter to Physical Layer indication that an error has occurred which
    * requires the Link to go down. Physical Layer must move to LinkError state
    * and stay there as long as lp_linkerror=1. The reason for having this be an
    * indication decoupled from regular state transitions is to allow immediate
    * action on part of the Adapter and Physical Layer in order to provide the
    * quickest path for error containment when applicable (for example, a viral
    * error escalation must map to the LinkError state). The Adapter must OR
    * internal error conditions with lp_linkerror received from Protocol Layer
    * on FDI.
    */
  val lpLinkError = Output(PhyStateReq())

  /** Physical Layer to Adapter Status indication of the Interface.
    *
    * The status signal is permitted to transition from Physical Layer
    * autonomously when applicable. For example the Physical Layer asserts the
    * Retrain status when it decides to enter retraining either autonomously or
    * when requested by remote agent.
    */
  val plStateStatus = Input(PhyState())

  val plInbandPres = Input(Bool())
  val plError = Input(Bool())
  val plCorrectableError = Input(Bool())
  val plNonFatalError = Input(Bool())
  val plTrainError = Input(Bool())
  val plPhyInRecenter = Input(Bool())
  val plStallReq = Input(Bool())
  val lpStallAck = Output(Bool())
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

object PhyState extends ChiselEnum {
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

object PhyStateReq extends ChiselEnum {
  val reset = Value(0x0.U(4.W))
  val active = Value(0x1.U(4.W))
  val l1 = Value(0x4.U(4.W))
  val l2 = Value(0x8.U(4.W))
  val linkReset = Value(0x9.U(4.W))
  val linkError = Value(0xa.U(4.W))
  val retrain = Value(0xb.U(4.W))
  val disabled = Value(0xc.U(4.W))
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
