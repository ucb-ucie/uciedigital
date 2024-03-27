package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.experimental._
import chiseltest._
import interfaces.{AfeParams, RdiParams}
import sideband.SidebandParams
import org.scalatest.flatspec.AnyFlatSpec

class LinkTrainingFSMTest extends AnyFlatSpec with ChiselScalatestTester {
  val linkTrainingParams = LinkTrainingParams()
  val sbParams = SidebandParams()
  val afeParams = AfeParams()
  val rdiParams = RdiParams(128, 128)

  behavior of "Link Training FSM"
  it should "correctly transition between states -- basic simulation" in {
    test(
      new LinkTrainingFSM(
        linkTrainingParams = linkTrainingParams,
        sbParams = sbParams,
        afeParams = afeParams,
        rdiParams = rdiParams,
      ),
    ) { c =>
      // c.io.mainbandLaneIO.txData.initSink().setSinkClock(c.clock)
      // c.io.mainbandLaneIO.rxData.initSource().setSourceClock(c.clock)

    }
  }
}
