package edu.berkeley.cs.ucie.digital

import chisel3._
import chisel3.util._
import interfaces._
import org.chipsalliance.cde.config.{Field, Parameters, Config}
import chisel3.experimental.hierarchy.{Definition, Instance, instantiable, public}
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import freechips.rocketchip.devices.tilelink.{TLTestRAM}

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

// @instantiable
class FdiLoopback(val fdiParams: FdiParams) extends Module {
      val io = IO(new Bundle {
          // val finished = Output(Bool())
          val fdi1 = Flipped(new Fdi(fdiParams))
      })
    //fdi2's input data comes from fdi1
    // might be problematic

  // val ram = LazyModule(new TLRAM(AddressSet(0x0, 0xffffff), beatBytes=32))

  // lazy val module = new Impl
  // class Impl extends LazyModuleImp(this) {
  //     val io = IO(new Bundle {
  //         // val finished = Output(Bool())
  //         val fdi1 = Flipped(new Fdi(fdiParams))
  //     })

    val latency = 2
    val delayer = Module(new Pipe(chiselTypeOf(io.fdi1.lpData.bits), latency))
    delayer.io.enq.valid := io.fdi1.lpData.valid
    delayer.io.enq.bits := io.fdi1.lpData.bits
    io.fdi1.plData.bits := delayer.io.deq.bits
    io.fdi1.plData.valid := delayer.io.deq.valid
    // pl* are all outputs
    io.fdi1.lpData.ready := true.B
    // io.fdi1.plData.valid := pipe.io.out.valid
    // io.fdi1.plData.bits := pipe.io.out.bits

    // Tieoffs
    io.fdi1.plRetimerCrd := false.B //optional signal

    io.fdi1.plDllp.valid := false.B //we don't use the DLLP interface due to no PCIe support
    io.fdi1.plDllp.bits := 0.U
    io.fdi1.plDllpOfc := false.B

    io.fdi1.plStream.protoStack := ProtoStack.stack0
    io.fdi1.plStream.protoType := ProtoStreamType.Stream
    io.fdi1.plFlitCancel := false.B
    io.fdi1.plStateStatus := PhyState.active //this is part of the FDI state machine
    io.fdi1.plInbandPres := false.B //depends on the FSM state
    io.fdi1.plError := false.B //need to model framing errors
    io.fdi1.plCerror := false.B
    io.fdi1.plNfError := false.B //we want to be able to inject errors from the adapter to protocol layer
    io.fdi1.plTrainError := false.B //need to model fatal errors of the PHY
    io.fdi1.plRxActiveReq := false.B // this signal indicates when protocol layer can receive bits

    io.fdi1.plProtocol := Protocol.streaming
    io.fdi1.plProtocolFlitFormat := FlitFormat.raw
    io.fdi1.plProtocolValid := true.B //need to model this signal changing from false to true upon training completion
    io.fdi1.plStallReq := false.B //need to model D2D adapter requesting protocol layer to flush flits
    io.fdi1.plPhyInRecenter := false.B //this is part of the FDI state machine
    io.fdi1.plPhyInL1 := false.B //need to use these if the D2D adapter handles multiple power states
    io.fdi1.plPhyInL2 := false.B
    io.fdi1.plSpeedMode := SpeedMode.speed4 //this is a runtime PHY knob
    io.fdi1.plLinkWidth := PhyWidth.width64 //this is a PHY-level parameter
    io.fdi1.plClkReq := true.B //we don't support dynamic clock gating
    
    // TODO: CHECK THIS
    io.fdi1.plWakeAck := RegNext(io.fdi1.lpWakeReq)

    io.fdi1.plConfig.valid := false.B
    io.fdi1.plConfig.bits := 0.U
    io.fdi1.plConfigCredit := false.B //need to handle sideband packets
  }
  
