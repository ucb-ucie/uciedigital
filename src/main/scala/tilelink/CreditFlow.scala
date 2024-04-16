package edu.berkeley.cs.ucie.digital
package tilelink

import chisel3._
import chisel3.util._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import org.chipsalliance.cde.config._
import freechips.rocketchip.util._
import freechips.rocketchip.prci._
import freechips.rocketchip.subsystem._

class DecoupledtoCreditedMsg[T <: Data](t: T, bufferSz: Int, flitWidth: Int) extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(t))
    val out = Decoupled(t)
    val credit = Flipped(Decoupled(UInt(flitWidth.W)))
  })
  val creditWidth = log2Ceil(bufferSz)
  require(creditWidth <= flitWidth)
  val credits = RegInit(0.U((creditWidth+1).W))
  val credit_incr = io.out.fire
  val credit_decr = io.credit.fire
  when (credit_incr || credit_decr) {
    credits := credits + credit_incr - Mux(io.credit.valid, io.credit.bits +& 1.U, 0.U)
  }

  io.out.valid := io.in.valid && credits < bufferSz.U
  io.out.bits := io.in.bits
  io.in.ready := io.out.ready && credits < bufferSz.U

  io.credit.ready := true.B
}