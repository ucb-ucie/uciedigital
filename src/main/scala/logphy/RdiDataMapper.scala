package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._
import interfaces._

class RdiDataMapperIO(rdiParams: RdiParams) extends Bundle {

  /** Adapter to Physical Layer data.
    *
    * Encompasses lp_irdy, lp_valid, and pl_trdy from the UCIe specification.
    */
  val lpData = Decoupled3(Bits((8 * rdiParams.width).W))

  /** Physical Layer to Adapter data.
    *
    * Encompasses `pl_valid` and `pl_data` from the UCIe specification. Note
    * that backpressure is not possible. Data should be sampled whenever valid
    * is asserted at a clock edge.
    */
  val plData = Flipped(Valid(Bits((8 * rdiParams.width).W)))
}

class SidebandDataMapperIO(fdiParams: FdiParams) extends Bundle {
  val tx = new Bundle {
    val bits = Output(UInt(fdiParams.sbWidth.W))
    val valid = Output(Bool())
    val credit = Input(Bool())
  }
  val rx = new Bundle {
    val bits = Input(UInt(fdiParams.sbWidth.W))
    val valid = Input(Bool())
    val credit = Output(Bool())
  }
}

class RdiDataMapper(
    rdiParams: RdiParams,
    fdiParams: FdiParams,
    afeParams: AfeParams,
) extends Module {

  val io = IO(new Bundle {
    val rdi = Flipped(new RdiDataMapperIO(rdiParams))
    val mainbandLaneIO = Flipped(new MainbandLaneIO(afeParams))
    val sidebandDataMapperIO = new SidebandDataMapperIO(fdiParams)
  })

  private object State extends ChiselEnum {
    val IDLE, CHUNK = Value
  }

  private val currentMBTxState = RegInit(State.IDLE)
  private val nextMBTxState = Wire(currentMBTxState)
  currentMBTxState := nextMBTxState

  assert(afeParams.mbSerializerRatio * afeParams.mbLanes < rdiParams.width * 8)

  /** need to chunk RDI messages, and collect outgoing phy -> d2d */
  val afeBits = (afeParams.mbSerializerRatio * afeParams.mbLanes)
  val ratio =
    (rdiParams.width * 8) / afeBits

  /** collect outgoing phy -> d2d */
  val rxSliceCounter = RegInit(0.U(log2Ceil(ratio).W))
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
    rxSliceCounter := rxSliceCounter + 1.U
    when(rxSliceCounter === (ratio - 1).U) {
      rxSliceCounter := 0.U
    }
  }
  io.rdi.plData.valid := rxSliceCounter === (ratio - 1).U
  io.rdi.plData.bits := rxData.asUInt

  /** chunk RDI message to transmit */
  private val txWidthCoupler = new DataWidthCoupler(
    DataWidthCouplerParams(
      inWidth = rdiParams.width * 8,
      outWidth = afeBits,
    ),
  )
  txWidthCoupler.io.out <> io.mainbandLaneIO.txData
  txWidthCoupler.io.in <> io.rdi.lpData

  // val txSliceCounter = RegInit(0.U(log2Ceil(ratio).W))
  // val txData = RegInit(0.U((rdiParams.width * 8).W))
  // switch(currentMBTxState) {
  //   is(State.IDLE) {
  //     io.rdi.lpData.ready := true.B
  //     when(io.rdi.lpData.fire) {
  //       txData := io.rdi.lpData.bits
  //       txSliceCounter := 0.U
  //       nextMBTxState := State.CHUNK
  //     }
  //   }
  //   is(State.CHUNK) {
  //     io.mainbandLaneIO.txData.bits := txData.asTypeOf(
  //       Vec(ratio, Bits(afeBits.W)),
  //     )(txSliceCounter)
  //     // val bitmask = 1 << (afeBits) - 1
  //     // io.mainbandLaneIO.txData.bits :=
  //     /* ((txData & (bitmask.U << (txSliceCounter * afeBits.U).asUInt)) >>
  //      * (txSliceCounter * afeBits.U))( */
  //     //     0,
  //     //     afeBits,
  //     //   )
  //     io.mainbandLaneIO.txData.valid := true.B
  //     when(io.mainbandLaneIO.txData.fire) {
  //       txSliceCounter := txSliceCounter + 1.U
  //       when(txSliceCounter === (ratio - 1).U) {
  //         nextMBTxState := State.IDLE
  //       }
  //     }
  //   }
  // }

  /** Sideband Code */
  /** need to chunk SB message from rdi -> phy, need to collect from phy -> rdi
    */
  assert(fdiParams.sbWidth * 8 > afeParams.sbSerializerRatio)
  val sbRatio = (fdiParams.sbWidth * 8) / afeParams.sbSerializerRatio
  val fdiSliceCounter = RegInit(0.U(log2Ceil(ratio).W))
  private val currentSBTxState = RegInit(State.IDLE)
  switch(currentSBTxState) {
    is(State.IDLE) {
      when(io.sidebandDataMapperIO.rx.valid) {

        /** TODO: no backpressure from RDI? */
      }
    }
    is(State.CHUNK) {}
  }

}
