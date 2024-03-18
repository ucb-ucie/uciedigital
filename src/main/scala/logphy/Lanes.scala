package edu.berkeley.cs.ucie.digital
package logphy

import interfaces._
import chisel3._
import freechips.rocketchip.util.{AsyncQueue, AsyncQueueParams}

class Lanes(
    afeParams: AfeParams,
    queueParams: AsyncQueueParams,
) extends Module {
  val io = IO(new Bundle() {
    val mainbandIo = new MainbandIO(afeParams)
    val mainbandLaneIO = new MainbandLaneIO(afeParams)
  })

  val txMBFifo =
    new AsyncQueue(
      Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W)),
      queueParams,
    )
  val rxMBFifo =
    new AsyncQueue(
      Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W)),
      queueParams,
    )

  rxMBFifo.io.enq <> io.mainbandIo.rxData
  rxMBFifo.io.deq_clock := clock
  rxMBFifo.io.deq_reset := reset
  rxMBFifo.io.enq_clock := io.mainbandIo.fifoParams.clk
  rxMBFifo.io.enq_reset := io.mainbandIo.fifoParams.reset
  txMBFifo.io.deq <> io.mainbandIo.txData
  txMBFifo.io.enq_clock := clock
  txMBFifo.io.enq_reset := reset
  txMBFifo.io.deq_clock := io.mainbandIo.fifoParams.clk
  txMBFifo.io.deq_reset := io.mainbandIo.fifoParams.reset

  txMBFifo.io.enq.valid := io.mainbandLaneIO.txData.valid
  io.mainbandLaneIO.rxData.valid := rxMBFifo.io.deq.valid
  assert(
    afeParams.mbSerializerRatio > 8 && afeParams.mbSerializerRatio % 8 == 0,
  )
  for (i <- 0 until afeParams.mbLanes) {
    for (j <- 0 until afeParams.mbSerializerRatio / 8) {
      txMBFifo.io.enq.bits(i) := io.mainbandLaneIO.txData.bits(
        afeParams.mbLanes * 8 * j + (i * 8),
        afeParams.mbLanes * 8 * j + (i * 8) + 8,
      )
      io.mainbandLaneIO.rxData
        .bits(
          afeParams.mbLanes * 8 * j + i * 8,
          afeParams.mbLanes * 8 * j + (i * 8) + 8,
        ) := rxMBFifo.io.deq.bits(i)
    }
  }
}
