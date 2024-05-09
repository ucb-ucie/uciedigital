package edu.berkeley.cs.ucie.digital
package afe

import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage
import interfaces._
import chisel3.experimental.DataMirror
import freechips.rocketchip.util.{AsyncQueue, AsyncQueueParams}

// This module receives data from logphy and sends to analog
class TxMainband(afeParams: AfeParams, BYTE: Int = 8) extends Module {
  val io = IO(new Bundle {
    val rxMbAfe = Flipped(
      Decoupled(Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W))),
    )
    val txMbIo = Output(new MainbandIo())
  })

  val lanes = afeParams.mbLanes
  val width = afeParams.mbSerializerRatio
  val hasData = Wire(Bool())
  hasData := io.rxMbAfe.valid

  // receive data

  val txMbShiftRegs = Seq.fill(lanes)(RegInit(0.U(width.W)))
  val sending = RegInit(false.B)
  val clockGated = RegInit(false.B)
  val (txMbUICounter, uiCountDone) = Counter(sending, width)
  val (clockGateCounter, clockGateDone) = Counter(!sending, width)

  io.rxMbAfe.ready := !sending && !clockGated
  when(io.rxMbAfe.fire) {
    sending := true.B
  }.elsewhen(uiCountDone) {
    sending := false.B
    clockGated := true.B
  }.elsewhen(clockGateDone) {
    clockGated := false.B
  }

  val shift = RegInit(false.B)

  io.txMbIo.clkn := Mux(
    !clockGated,
    false.B,
    !clock.asBool,
  ).asClock

  io.txMbIo.clkp := Mux(
    !clockGated,
    false.B,
    clock.asBool,
  ).asClock

  // Assign each async fifo individually
  io.rxMbAfe.bits.zipWithIndex.foreach { case (data, i) =>
    // Valid framing, up for first 4 ui, down for last 4 ui
    when(io.rxMbAfe.fire) {
      txMbShiftRegs(i) := data
    }.otherwise {
      txMbShiftRegs(i) := txMbShiftRegs(i) << 1.U
    }
  }

  io.txMbIo.data := VecInit(txMbShiftRegs.map(_.head(1))).asUInt
  io.txMbIo.valid := sending
  io.txMbIo.track := false.B
}

// This module accepts data from analog and send to adapter
class RxMainband(
    afeParams: AfeParams,
    queueParams: AsyncQueueParams,
    BYTE: Int = 8,
) extends Module {
  val io = IO(new Bundle {
    // should use rx of mbafeIo
    // val mbAfeIo = new MainbandAfeIo(AfeParams())
    val rxMbIo = Input(new MainbandIo())
    val txMbAfe =
      Decoupled(Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W)))
    // Dummy signals for testing
    val clkn_out = Output(Clock())

  })

  private val width = afeParams.mbSerializerRatio
  private val lanes = afeParams.mbLanes
  // Since sending data to adapter,
  // This module Should drive mbAfeIo tx data
  // io.mbAfeIo.rxData.ready := false.B
  io.clkn_out := io.rxMbIo.clkn
  // io.mbAfeIo.rxData.bits := Seq.fill(lanes)(0.U)
  // val txMbAfeData = io.mbAfeIo.txData
  val txMbAfeData = io.txMbAfe

  // This module receives data from analog, and store into async buffer
  // val rxMbFifos = Seq.fill(lanes)(Module (new AsyncFifoStefan(depth, width)))

  // val rxMbFifos = Seq.fill(lanes)(Module (
  //     new AsyncQueue(
  //         Bits(afeParams.mbSerializerRatio.W),
  //         queueParams)
  //     )
  // )
  val rxMbFifos = Seq.fill(lanes)(
    Module(
      new AsyncFifoCustom(32, afeParams.mbSerializerRatio),
    ),
  )

  // Enqueue end from analog
  withClock(io.rxMbIo.clkp) {
    val mbIoValid_pipe_0 = RegNext(io.rxMbIo.valid)
    val mbIoValid_pipe_1 = RegNext(mbIoValid_pipe_0)
    val mbIoValid_pipe_2 = RegNext(mbIoValid_pipe_1)
    val mbIoValid_next = RegNext(mbIoValid_pipe_2)

    // Shiftregs to deserialize and store into the async buffer
    val rxMbShiftRegs = Seq.fill(lanes)(RegInit(0.U(width.W)))
    val rxMbShiftRegs_next = Seq.fill(lanes)(RegInit(0.U(width.W)))
    val rxMbShiftRegs_xor = Seq.fill(lanes)(WireInit(0.U(width.W)))

    val rxMbUICounter = RegInit(0.U(log2Ceil(width).W))

    val rxMbUICounter_next = RegNext(rxMbUICounter)

    // val rxMbIoData_next = RegNext(io.rxMbIo.data)
    val rxMbIoData_next = RegInit(0.U(width.W))
    val fifo_enq_valid_next =
      RegNext(rxMbUICounter_next === (width - 1).U && rxMbUICounter === 0.U)
    val internal_valid =
      (mbIoValid_next ^ io.rxMbIo.valid) | (mbIoValid_next & io.rxMbIo.valid)

    rxMbFifos.zipWithIndex.foreach { case (rxMbFifo, i) =>
      rxMbFifo.io.enq_clock := io.rxMbIo.clkp
      rxMbFifo.io.enq_reset := reset
      rxMbFifo.io.enq.valid := false.B
      /* For clear testing visuals, should always connect to signal path for
       * minimal delay */
      rxMbFifo.io.enq.bits := 0.U
      // rxMbFifo.io.enq.bits := Cat(rxMbShiftRegs.reverse)
      /* There's a little overlap of assert high of io.rxMbIo.valid and last
       * stage pipeline */
      //
      when(internal_valid) {
        rxMbIoData_next := io.rxMbIo.data
        when(rxMbUICounter === 0.U) {
          for (i <- 0 until lanes) {
            rxMbShiftRegs(i) := 0.U | io.rxMbIo.data(i)
          }
          rxMbShiftRegs_next(i) := rxMbShiftRegs(i)

        }.otherwise {
          for (i <- 0 until lanes) {
            rxMbShiftRegs(i) := rxMbShiftRegs(i) << 1.U | io.rxMbIo.data(i)
          }
          rxMbShiftRegs_next(i) := 0.U

        }
        rxMbUICounter := rxMbUICounter + 1.U
      }
      rxMbShiftRegs_xor(i) := rxMbShiftRegs(i) ^ rxMbShiftRegs_next(i)
      when(
        (rxMbUICounter_next === (width - 1).U && rxMbUICounter === 0.U)
          ^ fifo_enq_valid_next,
      ) {
        rxMbFifo.io.enq.valid := true.B
        rxMbFifo.io.enq.bits := Cat(rxMbShiftRegs_xor.reverse)
      }
    }
  }
  withClock(clock) {

    rxMbFifos.zipWithIndex.foreach { case (rxMbFifo, i) =>
      // Dequeue end to drive
      rxMbFifo.io.deq_clock := clock
      rxMbFifo.io.deq_reset := reset
      txMbAfeData.bits(i) := rxMbFifo.io.deq.bits
      txMbAfeData.valid := rxMbFifo.io.deq.valid
      rxMbFifo.io.deq.ready := txMbAfeData.ready
    }
  }
}

class PhyTest extends Module {
  val io = IO(new Bundle {
    val tx_user = new MainbandAfeIo(AfeParams())
    val rx_user = new MainbandAfeIo(AfeParams())
    val clkp = Input(Clock())
    val clkn = Input(Clock())
    // val startDeq = Input(Bool())
    // val startEnq = Input(Bool())
    val clkn_out = Output(Clock())
  })

  val sender = Module(new TxMainband(AfeParams()))
  val receiver = Module(new RxMainband(AfeParams(), AsyncQueueParams()))
  sender.io.rxMbAfe <> io.tx_user.rxData
  sender.io.txMbIo <> receiver.io.rxMbIo

  // sender.io.startDeq := io.startDeq

  receiver.io.txMbAfe <> io.rx_user.txData
  // receiver.io.startEnq := io.startEnq
  io.clkn_out := receiver.io.clkn_out

  io.tx_user.txData.bits := Seq.fill(16)(0.U)
  io.tx_user.txData.valid := false.B
  io.rx_user.rxData.ready := false.B

  io.tx_user.txFreqSel := SpeedMode.speed16
  io.tx_user.rxEn := false.B
  io.rx_user.txFreqSel := SpeedMode.speed16
  io.rx_user.rxEn := false.B
}

class MbAfe(afeParams: AfeParams, queueParams: AsyncQueueParams)
    extends Module {
  val io = IO(new Bundle {
    val mbAfeIo = new MainbandAfeIo(AfeParams())
    val sbAfeIo = new SidebandAfeIo(AfeParams())
    val stdIo = new StandardPackageIo()
    // The following differential clock comes from pll
    val clkp = Input(Clock())
    val clkn = Input(Clock())
    val clk_800 = Input(Clock())
  })

// This module accepts data from analog and send to adapter
// class RxMainband(afeParams: AfeParams, queueParams: AsyncQueueParams, BYTE: Int = 8) extends Module {
  val txMainband = Module(new TxMainband(afeParams))
  val rxMainband = Module(new RxMainband(afeParams, queueParams))
  // val txSideband = Module(new TxSideband(depth))
  // val rxSideband = Module(new RxSideband(depth))

  // txMainband
  txMainband.io.rxMbAfe <> io.mbAfeIo.rxData
  io.stdIo.tx.mainband := txMainband.io.txMbIo

  rxMainband.io.txMbAfe <> io.mbAfeIo.txData
  rxMainband.io.rxMbIo := io.stdIo.rx.mainband
  io.stdIo.tx.sideband.data := 0.U
  io.sbAfeIo.rxEn := false.B
  io.mbAfeIo.txFreqSel := SpeedMode.speed16
  io.sbAfeIo.txData := 0.U
  io.mbAfeIo.rxEn := false.B
  io.sbAfeIo.txClock := false.B
  io.stdIo.tx.sideband.clk := clock
}

// To execute do:
// runMain edu.berkeley.cs.ucie.digital.afe.TxMainbandVerilog
object TxMainbandVerilog extends App {
  (new ChiselStage).emitSystemVerilog(
    new TxMainband(AfeParams()),
  )
}

// To execute do:
// runMain edu.berkeley.cs.ucie.digital.afe.TxMainbandVerilog
object RxMainbandVerilog extends App {
  (new ChiselStage).emitSystemVerilog(
    new RxMainband(AfeParams(), AsyncQueueParams()),
  )
}

object PhyTestVerilog extends App {
  (new ChiselStage).emitSystemVerilog(new PhyTest())
}

object MbAfeVerilog extends App {
  (new ChiselStage).emitSystemVerilog(
    new MbAfe(AfeParams(), AsyncQueueParams()),
  )
}
