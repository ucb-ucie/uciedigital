package edu.berkeley.cs.ucie.digital
package logphy

import interfaces._
import chisel3._
import chisel3.util._
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
    Module(
      new AsyncQueue(
        Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W)),
        queueParams,
      ),
    )
  val rxMBFifo =
    Module(
      new AsyncQueue(
        Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W)),
        queueParams,
      ),
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
  val txDataVec = Wire(
    Vec(afeParams.mbLanes, Vec(afeParams.mbSerializerRatio / 8, UInt(8.W))),
  )
  val ratioBytes = afeParams.mbSerializerRatio / 8
  val rxDataVec = Wire(
    Vec(ratioBytes, Vec(afeParams.mbLanes, UInt(8.W))),
  )
  for (i <- 0 until afeParams.mbLanes) {
    for (j <- 0 until ratioBytes) {
      txDataVec(afeParams.mbLanes - 1 - i)(j) := io.mainbandLaneIO.txData
        .bits(
          afeParams.mbLanes * 8 * j + (i * 8) + 7,
          afeParams.mbLanes * 8 * j + (i * 8),
        )
      rxDataVec(j)(afeParams.mbLanes - 1 - i) := rxMBFifo.io.deq
        .bits(i)((j + 1) * 8 - 1, j * 8)
    }
    txMBFifo.io.enq.bits(i) := txDataVec(i).asUInt
  }
  io.mainbandLaneIO.rxData.bits := rxDataVec.asUInt
  rxMBFifo.io.deq.ready := true.B
  io.mainbandLaneIO.txData.ready := txMBFifo.io.enq.ready
}

class MainbandSimIO(afeParams: AfeParams) extends Bundle {
  val txData = Decoupled(
    Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W)),
  )
  val rxData = Flipped(
    Decoupled(Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W))),
  )
}

class SimLanes(
    afeParams: AfeParams,
    queueParams: AsyncQueueParams,
) extends Module {

  val io = IO(new Bundle() {
    val mainbandIo = new MainbandSimIO(afeParams)
    val mainbandLaneIO = new MainbandLaneIO(afeParams)
  })

  val txMBFifo =
    Module(
      new Queue(
        Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W)),
        queueParams.depth,
      ),
    )
  val rxMBFifo =
    Module(
      new Queue(
        Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W)),
        queueParams.depth,
      ),
    )

  rxMBFifo.io.enq <> io.mainbandIo.rxData
  txMBFifo.io.deq <> io.mainbandIo.txData

  txMBFifo.io.enq.valid := io.mainbandLaneIO.txData.valid
  io.mainbandLaneIO.rxData.valid := rxMBFifo.io.deq.valid
  assert(
    afeParams.mbSerializerRatio > 8 && afeParams.mbSerializerRatio % 8 == 0,
  )
  val txDataVec = Wire(
    Vec(afeParams.mbLanes, Vec(afeParams.mbSerializerRatio / 8, UInt(8.W))),
  )
  val ratioBytes = afeParams.mbSerializerRatio / 8
  val rxDataVec = Wire(
    Vec(ratioBytes, Vec(afeParams.mbLanes, UInt(8.W))),
  )
  for (i <- 0 until afeParams.mbLanes) {
    for (j <- 0 until ratioBytes) {
      txDataVec(afeParams.mbLanes - 1 - i)(j) := io.mainbandLaneIO.txData
        .bits(
          afeParams.mbLanes * 8 * j + (i * 8) + 7,
          afeParams.mbLanes * 8 * j + (i * 8),
        )
      rxDataVec(j)(afeParams.mbLanes - 1 - i) := rxMBFifo.io.deq
        .bits(i)((j + 1) * 8 - 1, j * 8)
    }
    txMBFifo.io.enq.bits(i) := txDataVec(i).asUInt
  }
  io.mainbandLaneIO.rxData.bits := rxDataVec.asUInt
  rxMBFifo.io.deq.ready := true.B
  io.mainbandLaneIO.txData.ready := txMBFifo.io.enq.ready

}
