package edu.berkeley.cs.ucie.digital

import chisel3._
import chisel3.util._
import chisel3.util.random._

// 

class Scrambler extends Module {
  val io = IO(new Bundle {
    val L0_in = Input(UInt(16.W))
    val valid = Input(Bool())
    val rst = Input(Bool())
    val L0_out = Output(UInt(16.W))
  })
  val L0_LFSR = Module(new FibonacciLFSR(23, Set(23,21,18,15,7,2,1), Some(BigInt(1949628)), XOR, 16, false))
  L0_LFSR.io.increment := io.valid
  L0_LFSR.io.seed.bits := VecInit(1949628.U(23.W).asBools)
  L0_LFSR.io.seed.valid := (reset.asBool || io.rst)
  
  val L0_LFSR_result = L0_LFSR.io.out
  printf(cf"$L0_LFSR_result.asUInt")
  io.L0_out :=  L0_LFSR_result.asUInt ^ io.L0_in
}

