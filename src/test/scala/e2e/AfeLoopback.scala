package edu.berkeley.cs.ucie.digital
package e2e

import chisel3._
import chisel3.util._
import interfaces._
import org.chipsalliance.cde.config.{Field, Parameters, Config}
import chisel3.experimental.hierarchy.{
  Definition,
  Instance,
  instantiable,
  public,
}
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import freechips.rocketchip.devices.tilelink.{TLTestRAM}
import protocol._

// @instantiable
class AfeLoopback(val afeParams: AfeParams) extends Module {
  val io = IO(new Bundle {
    // val finished = Output(Bool())
    val mbAfe = Flipped(new MainbandAfeIo(afeParams))
    val sbAfe = Flipped(new SidebandAfeIo(afeParams))
  })

  val latency = 2
  val delayerMb = Module(new Pipe(chiselTypeOf(io.mbAfe.txData.bits), latency))
  val delayerSb = Module(new Pipe(chiselTypeOf(io.sbAfe.txData), latency))
  val delayerSb_clock = Module(
    new Pipe(chiselTypeOf(io.sbAfe.txClock), latency),
  )

  delayerMb.io.enq.valid := io.mbAfe.txData.valid
  delayerMb.io.enq.bits := io.mbAfe.txData.bits
  io.mbAfe.rxData.bits := delayerMb.io.deq.bits
  io.mbAfe.rxData.valid := delayerMb.io.deq.valid
  io.mbAfe.txData.ready := true.B
  io.mbAfe.fifoParams.clk := clock
  io.mbAfe.fifoParams.reset := reset
  io.mbAfe.pllLock := true.B

  delayerSb.io.enq.valid := true.B // io.sbAfe.txData.valid
  delayerSb.io.enq.bits := io.sbAfe.txData
  delayerSb_clock.io.enq.valid := true.B
  delayerSb_clock.io.enq.bits := io.sbAfe.txClock

  io.sbAfe.rxData := delayerSb.io.deq.bits
  // io.sbAfe.rxData.valid   := delayerSb.io.deq.valid
  io.sbAfe.rxClock := Mux(delayerSb_clock.io.deq.bits, clock, false.asBool)
  io.sbAfe.fifoParams.clk := clock
  io.sbAfe.fifoParams.reset := reset
  io.sbAfe.pllLock := true.B
}
