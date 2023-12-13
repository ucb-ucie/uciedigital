package edu.berkeley.cs.ucie.digital

import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

class ProtoFDILoopbackTest extends AnyFreeSpec with ChiselScalatestTester {
  "Protocol FDI Loopback module should elaborate" in {
    test(new ProtoFDILoopback()) { c =>
      c.clock.step()
    }
  }
}