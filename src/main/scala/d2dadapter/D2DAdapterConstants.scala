package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
import chisel3.util._
import interfaces._
import sideband._

case class D2DAdapterParams () {
    
}

object LinkInitState extends ChiselEnum {
    val INIT_START = Value(0x0.U(3.W))
    val RDI_BRINGUP = Value(0x1.U(3.W))
    val PARAM_EXCH = Value(0x2.U(3.W))
    val FDI_BRINGUP = Value(0x3.U(3.W))
    val INIT_DONE = Value(0x4.U(3.W))
}