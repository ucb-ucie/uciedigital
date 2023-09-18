package sideband

import chisel3._

/** Source identifier (unspecified values are reserved) */
object SourceID extends ChiselEnum {

  /** Stack 0 Protocol Layer */
  val Stack0Protocol = Value("b000".U(3.W))

  /** Stack 1 Protocol Layer */
  val Stack1Protocol = Value("b100".U(3.W))

  /** Die to Die Adapter */
  val DieToDieAdapter = Value("b001".U(3.W))

  /** Physical Layer */
  val PhysicalLayer = Value("b010".U(3.W))
}
