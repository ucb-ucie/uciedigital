package edu.berkeley.cs.ucie.digital

import chisel3._
import chisel3.util._
import interfaces._
import chisel3.stage.ChiselStage

// LatencyPipe from rocket-chip
// https://github.com/chipsalliance/rocket-chip/blob/master/src/main/scala/util/LatencyPipe.scala
class LatencyPipe[T <: Data](typ: T, latency: Int) extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(typ))
    val out = Decoupled(typ)
  })

  def doN[S](n: Int, func: S => S, in: S): S =
    (0 until n).foldLeft(in)((last, _) => func(last))

  io.out <> doN(latency, (last: DecoupledIO[T]) => Queue(last, 1, true), io.in)
}

object LatencyPipe {
  def apply[T <: Data](in: DecoupledIO[T], latency: Int): DecoupledIO[T] = {
    val pipe = Module(new LatencyPipe(chiselTypeOf(in.bits), latency))
    pipe.io.in <> in
    pipe.io.out
  }
}

class D2DDummyLoopback(latency: Int = 8) extends Module {
  // 64 bit wide data bus, 8 bit wide side channel bus
  val fdiParams = FdiParams(width=8, dllpWidth=8, sbWidth=8)

  val io = IO(Flipped(new Fdi(fdiParams))) // implicit module clock = lclk

  when (io.lpData.valid) {
    // For now, the protocol layer must asssert lpData.valid and lpData.irdy together
    chisel3.assert(io.lpData.irdy, "lpData.valid was asserted without lpData.irdy")
  }

  // Restructure lpData as Decoupled[UInt]
  val lpDataDecoupled = Wire(DecoupledIO(chiselTypeOf(io.lpData.bits)))
  lpDataDecoupled.bits := io.lpData.bits
  lpDataDecoupled.valid := io.lpData.valid
  io.lpData.ready := lpDataDecoupled.ready

  // Loopback lpData to plData with some fixed latency
  val pipe = Module(new LatencyPipe(chiselTypeOf(io.lpData.bits), latency))
  pipe.io.in <> lpDataDecoupled
  pipe.io.out.ready := true.B // immediately fetch the data after <latency> cycles
  io.plData.valid := pipe.io.out.valid
  io.plData.bits := pipe.io.out.bits

  // Tieoffs
  io.plProtocol := Protocol.streaming
  io.plProtocolFlitFormat := FlitFormat.raw
}

object D2DDummyLoopbackMain extends App {
  new ChiselStage().emitVerilog(new D2DDummyLoopback())
}
