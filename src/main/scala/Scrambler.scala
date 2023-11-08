package edu.berkeley.cs.ucie.digital
package interfaces

import chisel3._
import chisel3.util._
import chisel3.util.random._

// 

class Scrambler (
  afeParams: AfeParams,
  width: Int,
  seed: BigInt
) extends Module {
  val io = IO(new Bundle {
    val data_in = Input(UInt(afeParams.mbSerializerRatio.W))
    val valid = Input(Bool())
    val seed = Input(UInt(23.W))
    val data_out = Output(UInt(afeParams.mbSerializerRatio.W))
  })
  val LFSR = Module(new FibonacciLFSR(23, Set(23,21,18,15,7,2,1), Some(seed), XOR, width, false))
  LFSR.io.increment := io.valid
  LFSR.io.seed.bits := VecInit(io.seed.asBools)
  LFSR.io.seed.valid := (reset.asBool)
  val LFSR_result = LFSR.io.out
  //printf(cf"$LFSR_result.asUInt")
  io.data_out := LFSR_result.asUInt ^ io.data_in
}

class UCIeScrambler (
  afeParams: AfeParams,
  width: Int
) extends Module {
  val io = IO(new Bundle {
    val data_in = Input(Vec(12, UInt(afeParams.mbSerializerRatio.W)))
    val valid = Input(Bool())
    val data_out = Output(Vec(12, UInt(afeParams.mbSerializerRatio.W)))
  })
  val seeds = List("1DBFBC", "0607BB", "1EC760", "18C0DB", "010F12", "19CFC9", "0277CE", "1BB807", "18C0DB", "010F12", "18C0DB", "010F12")
  val scramblers = seeds.map(seed => Module(new Scrambler(afeParams, width, BigInt(seed, 16))));
  
  for (i <- 0 until scramblers.length) {
    scramblers.apply(i).io.data_in := io.data_in(0);
    scramblers.apply(i).io.valid := io.valid;
    scramblers.apply(i).io.seed := seeds.apply(i).U(23.W);
    io.data_out(i) := scramblers.apply(i).io.data_out
  }
}

