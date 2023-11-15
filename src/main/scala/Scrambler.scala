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
  width: Int, 
  numLanes: Int
) extends Module {
  val io = IO(new Bundle {
    val data_in = Input(Vec(numLanes, UInt(afeParams.mbSerializerRatio.W)))
    val valid = Input(Bool())
    val data_out = Output(Vec(numLanes, UInt(afeParams.mbSerializerRatio.W)))
  })
  val UCIe_seeds = List("1dbfbc", "0607bb", "1ec760", "18c0db", "010f12", "19cfc9", "0277ce", "1bb807")
  val seeds = (for (i <- 0 until numLanes) yield UCIe_seeds.apply(i % 8)).toList
  val scramblers = seeds.map(seed => Module(new Scrambler(afeParams, width, BigInt(seed, 16))))
  for (i <- 0 until scramblers.length) {
    scramblers.apply(i).io.data_in := io.data_in(i)
    scramblers.apply(i).io.valid := io.valid
    scramblers.apply(i).reset := reset
    scramblers.apply(i).clock := clock
    scramblers.apply(i).io.seed := ("h" + seeds.apply(i)).U(23.W)
    io.data_out(i) := scramblers.apply(i).io.data_out
  }
}

