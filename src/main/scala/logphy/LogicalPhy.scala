package edu.berkeley.cs.ucie.digital
package logphy

import interfaces._
import sideband._

import chisel3._
import freechips.rocketchip.util.AsyncQueueParams

class LogicalPhy(
    myId: BigInt,
    linkTrainingParams: LinkTrainingParams,
    afeParams: AfeParams,
    rdiParams: RdiParams,
    fdiParams: FdiParams,
    sbParams: SidebandParams,
    laneAsyncQueueParams: AsyncQueueParams,
) extends Module {
  val io = IO(new Bundle {
    val rdi = Flipped(new Rdi(rdiParams))
    val mbAfe = new MainbandAfeIo(afeParams)
    val sbAfe = new SidebandAfeIo(afeParams)
  })

  val trainingModule = {
    new LinkTrainingFSM(linkTrainingParams, sbParams, afeParams, rdiParams)
  }
  val lanes = new Lanes(afeParams, laneAsyncQueueParams)
  val rdiDataMapper = new RdiDataMapper(rdiParams, afeParams)

  /** Connect internal FIFO to AFE */
  lanes.io.mainbandIo.txData <> io.mbAfe.txData
  lanes.io.mainbandIo.rxData <> io.mbAfe.rxData
  lanes.io.mainbandIo.fifoParams <> io.mbAfe.fifoParams
  // lanes.io.sidebandIo.txData <> io.sbAfe.txData
  // lanes.io.sidebandIo.rxData <> io.sbAfe.rxData
  // lanes.io.sidebandIo.fifoParams <> io.sbAfe.fifoParams

  // when(trainingModule.io.active) {
  /** Connect RDI to Mainband IO */
  rdiDataMapper.io.rdi.lpData <> io.rdi.lpData
  io.rdi.plData <> rdiDataMapper.io.rdi.plData
  rdiDataMapper.io.mainbandLaneIO <> lanes.io.mainbandLaneIO
  // }.otherwise {
  //   lanes.io.mainbandLaneIO <> trainingModule.io.mainbandLaneIO
  // }

  private val sidebandChannel =
    new PHYSidebandChannel(myId, sbParams, fdiParams)
  assert(
    afeParams.sbSerializerRatio == 1,
    "connecting sideband module directly to training module, sb serializer ratio must be 1!",
  )

  /** TODO: Double check that this is the right direction */
  sidebandChannel.io.to_upper_layer.tx.bits <> io.rdi.plConfig.bits
  sidebandChannel.io.to_upper_layer.tx.valid <> io.rdi.plConfig.valid
  sidebandChannel.io.to_upper_layer.tx.credit <> io.rdi.plConfigCredit
  sidebandChannel.io.to_upper_layer.rx.bits <> io.rdi.lpConfig.bits
  sidebandChannel.io.to_upper_layer.rx.valid <> io.rdi.lpConfig.valid
  sidebandChannel.io.to_upper_layer.rx.credit <> io.rdi.lpConfigCredit

  /** Inner connections to lower layer */
  sidebandChannel.io.inner.switcherBundle.layer_to_node_below <> trainingModule.io.sidebandFSMIO.packetTxData
  sidebandChannel.io.inner.switcherBundle.node_to_layer_below <> trainingModule.io.sidebandFSMIO.rxData
  sidebandChannel.io.inner.rawInput <> trainingModule.io.sidebandFSMIO.patternTxData
  sidebandChannel.io.inner.inputMode := trainingModule.io.sidebandFSMIO.txMode
  sidebandChannel.io.inner.rxMode := trainingModule.io.sidebandFSMIO.rxMode

  /** TODO: layer to node above not connected? Not sure when might receive SB
    * packet from above layer
    */
  sidebandChannel.io.inner.switcherBundle.layer_to_node_above.noenq()
  sidebandChannel.io.inner.switcherBundle.node_to_layer_above.nodeq()

  assert(
    afeParams.sbWidth == fdiParams.sbWidth,
    "AFE SB width and FDI SB width must match",
  )
  io.sbAfe.txData <> sidebandChannel.io.to_lower_layer.tx.bits
  io.sbAfe.txClock <> sidebandChannel.io.to_lower_layer.tx.clock
  io.sbAfe.rxData <> sidebandChannel.io.to_lower_layer.rx.bits
  io.sbAfe.rxClock <> sidebandChannel.io.to_lower_layer.rx.clock

}
