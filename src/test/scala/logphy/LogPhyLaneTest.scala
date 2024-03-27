package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.experimental.VecLiterals.AddObjectLiteralConstructor
import chiseltest._
import freechips.rocketchip.util.AsyncQueueParams
import org.scalatest.flatspec.AnyFlatSpec
import interfaces._

class LogPhyLaneTest extends AnyFlatSpec with ChiselScalatestTester {
  val afeParams = AfeParams()
  val queueParams = new AsyncQueueParams()
  behavior of "log phy TX lanes"
  it should "correctly map TX bytes to their lanes" in {
    test(new SimLanes(afeParams, queueParams)) { c =>
      c.io.mainbandLaneIO.txData.initSource()
      c.io.mainbandLaneIO.txData.setSourceClock(c.clock)
      c.io.mainbandIo.txData.initSink()
      c.io.mainbandIo.txData.setSinkClock(c.clock)

      c.io.mainbandLaneIO.txData.enqueueNow(
        "h1234_5678_9abc_def0_0fed_cba9_8765_4321_1111_2222_3333_4444_5555_6666_7777_8888".U,
      )
      c.io.mainbandIo.txData
        .expectDequeueNow(
          Vec.Lit(
            "h1211".U,
            "h3411".U,
            "h5622".U,
            "h7822".U,
            "h9a33".U,
            "hbc33".U,
            "hde44".U,
            "hf044".U,
            "h0f55".U,
            "hed55".U,
            "hcb66".U,
            "ha966".U,
            "h8777".U,
            "h6577".U,
            "h4388".U,
            "h2188".U,
          ),
        )
    }
  }

  behavior of "log phy RX lanes"
  it should "correctly map RX bytes to their lanes" in {
    test(new SimLanes(afeParams, queueParams)) { c =>
      c.io.mainbandIo.rxData.initSource()
      c.io.mainbandIo.rxData.setSourceClock(c.clock)
      c.io.mainbandLaneIO.rxData.initSink()
      c.io.mainbandLaneIO.rxData.setSinkClock(c.clock)

      c.io.mainbandIo.rxData
        .enqueueNow(
          Vec.Lit(
            "h1211".U,
            "h3411".U,
            "h5622".U,
            "h7822".U,
            "h9a33".U,
            "hbc33".U,
            "hde44".U,
            "hf044".U,
            "h0f55".U,
            "hed55".U,
            "hcb66".U,
            "ha966".U,
            "h8777".U,
            "h6577".U,
            "h4388".U,
            "h2188".U,
          ),
        )

      c.io.mainbandLaneIO.rxData.expectDequeueNow(
        "h1234_5678_9abc_def0_0fed_cba9_8765_4321_1111_2222_3333_4444_5555_6666_7777_8888".U,
      )

    }

  }
}
