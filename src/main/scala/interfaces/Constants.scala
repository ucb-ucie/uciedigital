package edu.berkeley.cs.ucie.digital.interfaces

import chisel3._
import chisel3.util._

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

object ProtoStack extends ChiselEnum {
  val stack0 = Value(0x0.U(4.W))
  val stack1 = Value(0x1.U(4.W))
}

object ProtoStreamType extends ChiselEnum {
  val PCIe = Value(0x1.U(4.W))
  val CXLI = Value(0x2.U(4.W))
  val CXLC = Value(0x3.U(4.W))
  val Stream = Value(0x4.U(4.W))
}

class ProtoStream extends Bundle {
  val protoStack = Output(ProtoStack())
  val protoType = Output(ProtoStreamType())
}

object Protocol extends ChiselEnum {
  val pcie = Value(0x0.U(3.W))
  val cxl1 = Value(0x3.U(3.W))
  val cxl2 = Value(0x4.U(3.W))
  val cxl3 = Value(0x5.U(3.W))
  val cxl4 = Value(0x6.U(3.W))
  val streaming = Value(0x7.U(3.W))
}

object FlitFormat extends ChiselEnum {
  val raw = Value(0x1.U(4.W))
  val flit68 = Value(0x2.U(4.W))
  val standard256EndHeader = Value(0x3.U(4.W))
  val standard256StartHeader = Value(0x4.U(4.W))
  val latencyOpt256NoOptional = Value(0x5.U(4.W))
  val latencyOpt256Optional = Value(0x6.U(4.W))
}
