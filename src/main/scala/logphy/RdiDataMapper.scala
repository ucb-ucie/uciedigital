package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._
import interfaces._

class RdiDataMapper(
    rdiParams: RdiParams,
    afeParams: AfeParams,
) extends Module {

  val io = IO(new Bundle {
    val rdi = Flipped(new Rdi(rdiParams))
    val mainbandLaneIO = Flipped(new MainbandLaneIO(afeParams))
  })

  private object State extends ChiselEnum {
    val IDLE, CHUNK = Value
  }

  val currentTxState = RegInit(State.IDLE)
  val nextTxState = Wire(currentTxState)
  currentTxState := nextTxState

  /** send data to tx fifo */
  io.rdi.lpData.ready := true.B
  when(io.rdi.lpData.fire) {}

  /** send data to adapter */
  assert(afeParams.mbSerializerRatio * afeParams.mbLanes < rdiParams.width * 8)

  /** need to chunk RDI messages, and collect outgoing phy -> d2d */
  val afeBits = (afeParams.mbSerializerRatio * afeParams.mbLanes)
  val ratio =
    (rdiParams.width * 8) / afeBits

  /** collect outgoing phy -> d2d */
  val rxSliceCounter = RegInit(0.U(4.W))
  val rxData =
    RegInit(
      VecInit(
        Seq.fill(ratio)(
          0.U(afeBits.W),
        ),
      ),
    )
  io.mainbandLaneIO.rxData.ready := true.B
  when(io.mainbandLaneIO.rxData.fire) {

    /** chunk */
    rxData(rxSliceCounter) := io.mainbandLaneIO.rxData.bits
    rxSliceCounter := rxSliceCounter + 1
    when(rxSliceCounter === ratio - 1) {
      rxSliceCounter := 0.U
    }
  }
  io.rdi.plData.valid := rxSliceCounter === ratio - 1
  io.rdi.plData.bits := rxData.asUInt

  /** chunk RDI message to transmit */
  val txSliceCounter = RegInit(0.U(4.W))
  val txData = RegInit(0.U((rdiParams.width * 8).W))
  switch(currentTxState) {
    is(State.IDLE) {
      io.rdi.lpData.ready := true.B
      when(io.rdi.lpData.fire) {
        txData := io.rdi.lpData.bits
        txSliceCounter := 0.U
        nextTxState := State.CHUNK
      }
    }
    is(State.CHUNK) {
      val bitmask = 1 << (afeBits) - 1
      io.mainbandLaneIO.txData.bits :=
        ((txData & (bitmask << (txSliceCounter * afeBits)).U) >> (txSliceCounter * afeBits))(
          0,
          afeBits.W,
        )
      io.mainbandLaneIO.txData.valid := true.B
      when(io.mainbandLaneIO.txData.fire) {
        txSliceCounter := txSliceCounter + 1
        when(txSliceCounter === ratio - 1) {
          nextTxState := State.IDLE
        }
      }
    }
  }

}
