package edu.berkeley.cs.ucie.digital

import chisel3._
import chisel3.util._

import interfaces._

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

/**
  * This class creates a loopback for testing the Protocol layer
  * It consists of the FDI interface and a latency pipe to emulate
  * the latency imparted by the D2D adapter.
  */
class ProtoFDILoopback(val fdiParams: FdiParams, val latency: Int = 8) extends Module {
    val io = IO(new Bundle{
      val fdi = Flipped(new Fdi(fdiParams))
    })

    when(io.fdi.lpData.valid) {
      /* For now, the protocol layer must assert lpData.valid and lpData.irdy
      * together */
      chisel3.assert(
        io.fdi.lpData.irdy,
        "lpData.valid was asserted without lpData.irdy",
      )
    }

    // Restructure lpData as Decoupled[UInt]
    val lpDataDecoupled = Wire(DecoupledIO(chiselTypeOf(io.fdi.lpData.bits)))
    lpDataDecoupled.bits := io.fdi.lpData.bits
    lpDataDecoupled.valid := io.fdi.lpData.valid
    io.fdi.lpData.ready := lpDataDecoupled.ready

    // Loopback lpData to plData with some fixed latency
    val pipe = Module(new LatencyPipe(chiselTypeOf(io.fdi.lpData.bits), latency))
    pipe.io.in <> lpDataDecoupled
    pipe.io.out.ready := true.B // immediately fetch the data after <latency> cycles
    io.fdi.plData.valid := pipe.io.out.valid
    io.fdi.plData.bits := pipe.io.out.bits

    // Signals from Protocol layer to D2D adapter. 
    // These needs to be driven by ProtocolLayer.scala
    /*
    io.fdi.lpRetimerCrd
    io.fdi.lpCorruptCrc
    io.fdi.lpDllp
    io.fdi.lpDllpOfc
    io.fdi.lpStream
    io.fdi.lpStateReq
    io.fdi.lpLinkError
    io.fdi.lpRxActiveStatus
    io.fdi.lpStallAck
    io.fdi.lpClkAck
    io.fdi.lpWakeReq
    io.fdi.lpConfig
    io.fdi.lpConfigCredit
    */

    // Signals from D2D adapter to Protocol layer
    // Tieoffs signals and drive from test harness
    io.fdi.plRetimerCrd := false.B
    io.fdi.plDllp.valid := false.B
    io.fdi.plDllp.bits := 0.U
    io.fdi.plDllpOfc := false.B
    io.fdi.plStream.protoStack := ProtoStack.stack0
    io.fdi.plStream.protoType := ProtoStreamType.Stream
    io.fdi.plFlitCancel := false.B
    io.fdi.plStateStatus := PhyState.disabled
    io.fdi.plInbandPres := false.B
    io.fdi.plError := false.B
    io.fdi.plCerror := false.B
    io.fdi.plNfError := false.B
    io.fdi.plTrainError := false.B
    io.fdi.plRxActiveReq := false.B
    io.fdi.plProtocol := Protocol.streaming
    io.fdi.plProtocolFlitFormat := FlitFormat.raw
    io.fdi.plProtocolValid := true.B
    io.fdi.plStallReq := false.B
    io.fdi.plPhyInRecenter := false.B
    io.fdi.plPhyInL1 := false.B
    io.fdi.plPhyInL2 := false.B
    io.fdi.plSpeedMode := SpeedMode.speed4
    io.fdi.plLinkWidth := PhyWidth.width64
    io.fdi.plClkReq := false.B
    io.fdi.plWakeAck := false.B
    io.fdi.plConfig.valid := false.B
    io.fdi.plConfig.bits := 0.U
    io.fdi.plConfigCredit := false.B
}
