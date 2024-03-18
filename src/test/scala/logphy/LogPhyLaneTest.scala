package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chiseltest._
import freechips.rocketchip.util.AsyncQueueParams
import org.scalatest.flatspec.AnyFlatSpec
import interfaces._

class LogPhyLaneTest extends AnyFlatSpec with ChiselScalatestTester {
  val afeParams = new AfeParams()
  val queueParams = new AsyncQueueParams()
  behavior of "log phy lanes"
  it should "correctly map bytes to their lanes" in {
    test(new Lanes())

  }
}
