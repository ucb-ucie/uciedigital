package edu.berkeley.cs.ucie.digital
package afe

import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage
import interfaces._
import chisel3.experimental.DataMirror
import freechips.rocketchip.util.{AsyncQueue, AsyncQueueParams}

// This module receives data from logphy and sends to analog
class TxMainbandSerializer(afeParams: AfeParams) extends Module {
  val io = IO(new Bundle {
    val txInData = Flipped(
      Decoupled(Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W))),
    )
    val txMbIo = Output(new MainbandIo())
  })

  val lanes = afeParams.mbLanes
  val width = afeParams.mbSerializerRatio
  val hasData = Wire(Bool())
  hasData := io.txInData.valid

  // receive data

  val txMbShiftRegs = Seq.fill(lanes)(RegInit(0.U(width.W)))
  val sending = RegInit(false.B)
  val (txMbUICounter, uiCountDone) = Counter(sending, width)

  io.txInData.ready := !sending
  when(io.txInData.fire) {
    sending := true.B
  }.elsewhen(uiCountDone) {
    sending := false.B
  }

  val shift = RegInit(false.B)

  io.txMbIo.clkn := (!clock.asBool).asClock
  io.txMbIo.clkp := clock

  // Assign each async fifo individually
  io.txInData.bits.zipWithIndex.foreach { case (laneData, i) =>
    // Valid framing, up for first 4 ui, down for last 4 ui
    when(io.txInData.fire) {
      txMbShiftRegs(i) := laneData
    }.otherwise {
      txMbShiftRegs(i) := txMbShiftRegs(i) >> 1.U
    }
  }

  io.txMbIo.data := VecInit(txMbShiftRegs.map(_(0))).asUInt
  io.txMbIo.valid := sending
  io.txMbIo.track := false.B
}

// This module accepts data from analog and send to adapter
class RxMainbandDeserializer(
    afeParams: AfeParams,
) extends Module {
  val io = IO(new Bundle {
    val rxMbIo = Input(new MainbandIo())
    val rxOutData =
      Decoupled(Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W)))
  })

  private val width = afeParams.mbSerializerRatio
  private val lanes = afeParams.mbLanes

  val rxMbShiftRegs = Seq.fill(lanes)(RegInit(0.U(width.W)))
  val (_, done_sending) = Counter(io.rxMbIo.valid, width)

  rxMbShiftRegs.zipWithIndex.foreach { case (rxMbShiftReg, i) =>
    when(io.rxMbIo.valid) {
      for (i <- 0 until lanes) {
        rxMbShiftReg := (rxMbShiftReg << 1.U) | io.rxMbIo.data(i)
      }
    }
    io.rxOutData.bits(i) := rxMbShiftReg
  }
  io.rxOutData.valid := done_sending
}

class MbAfe(afeParams: AfeParams, queueParams: AsyncQueueParams)
    extends Module {
  val io = IO(new Bundle {
    val mbAfeIo = new MainbandAfeIo(AfeParams())
    val sbAfeIo = new SidebandAfeIo(AfeParams())
    val stdIo = new StandardPackageIo()
  })

  val txMainband = Module(new TxMainbandSerializer(afeParams))
  val rxMainband =
    withClockAndReset(io.stdIo.rx.mainband.clkp, reset.asAsyncReset)(
      Module(new RxMainbandDeserializer(afeParams)),
    )

  // txMainband
  txMainband.io.txInData <> io.mbAfeIo.rxData
  io.stdIo.tx.mainband := txMainband.io.txMbIo

  rxMainband.io.rxOutData <> io.mbAfeIo.txData
  rxMainband.io.rxMbIo := io.stdIo.rx.mainband
  io.stdIo.tx.sideband.data := 0.U
  io.sbAfeIo.rxEn := false.B
  io.mbAfeIo.txFreqSel := SpeedMode.speed16
  io.sbAfeIo.txData := 0.U
  io.mbAfeIo.rxEn := false.B
  io.sbAfeIo.txClock := false.B
  io.stdIo.tx.sideband.clk := clock
}
