package edu.berkeley.cs.ucie.digital
package logphy

import interfaces._

import chisel3._
import edu.berkeley.cs.ucie.digital.util.AsyncQueueParams

class LogicalPhy(
    linkTrainingParams: LinkTrainingParams,
    afeParams: AfeParams,
    rdiParams: RdiParams,
    laneAsyncQueueParams: AsyncQueueParams,
) extends Module {
  val io = IO(new Bundle {
    val rdi = Flipped(new Rdi(rdiParams))

    /** TODO: sideband interface */
    // val sideband = Flipped()
    val mbAfe = new MainbandAfeIo(afeParams)
    val sbAfe = new SidebandAfeIo(afeParams)
  })

  val trainingModule = {
    new LinkTrainingFSM(linkTrainingParams, afeParams, rdiParams)
  }
  val lanes = new Lanes(afeParams, laneAsyncQueueParams)
  val rdiDataMapper = new RdiDataMapper(rdiParams, afeParams)

  /** Connect internal FIFO to AFE */
  lanes.io.mainbandIo.txData <> io.mbAfe.txData
  lanes.io.mainbandIo.rxData <> io.mbAfe.rxData
  lanes.io.mainbandIo.fifoParams <> io.mbAfe.fifoParams
  lanes.io.sidebandIo.txData <> io.sbAfe.txData
  lanes.io.sidebandIo.rxData <> io.sbAfe.rxData
  lanes.io.sidebandIo.fifoParams <> io.sbAfe.fifoParams

  when(trainingModule.io.active) {

    /** Connect RDI to Mainband IO */
    rdiDataMapper.io.rdi.lpData <> io.rdi.lpData
    io.rdi.plData <> rdiDataMapper.io.rdi.plData
    rdiDataMapper.io.mainbandLaneIO <> lanes.io.mainbandLaneIO
  }.otherwise {
    lanes.io.mainbandLaneIO <> trainingModule.io.mainbandLaneIO
  }

}
