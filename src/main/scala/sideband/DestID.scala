package edu.berkeley.cs.ucie.digital
package sideband

import chisel3._

object DestID extends Bundle {
  val request_termination = RequestTermination()
  val destination = Destination()

  object RequestTermination extends ChiselEnum {

    /** Local die terminated request */
    val Local = Value(0x0.U(1.W))

    /** Remote die terminated request */
    val Remote = Value(0x1.U(1.W))
  }

  object Destination extends ChiselEnum {

    /** Die to Die Adapter */
    val DieToDieAdapter = Value("b01".U(3.W))

    /** Physical Layer */
    val PhysicalLayer = Value("b10".U(3.W))
  }
}
