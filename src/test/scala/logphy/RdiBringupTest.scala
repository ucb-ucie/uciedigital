package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class RdiBringupTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "RDI Bringup Module"
  it should "correctly transition" in {
    test(new RdiBringup) { c =>
      c.io.sbTrainIO.msgReq.initSink().setSinkClock(c.clock)
      c.io.sbTrainIO.msgReqStatus.initSource().setSourceClock(c.clock)


    }
  }

}
