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
    val sideband = Flipped(new SidebandIo)
    val mbAfe = new MainbandAfeIo(afeParams)
    val sbAfe = new SidebandAfeIo(afeParams)
  })

  val trainingModule = {
    new LinkTrainingFSM(linkTrainingParams, afeParams, rdiParams)
  }
  val lanes = new Lanes(afeParams, laneAsyncQueueParams)
  val rdiDataMapper = new RdiDataMapper(rdiParams, afeParams)

}
