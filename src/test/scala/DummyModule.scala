package edu.berkeley.cs.ucie.digital

import chisel3._
import chiseltest._
import org.scalatest.funspec.AnyFunSpec

class DummyModuleTest extends AnyFunSpec with ChiselScalatestTester {
  describe("DummyModule") {
    it("should invert its input") {
      test(new DummyModule()) { c =>
        c.io.a.poke(true.B)
        c.io.b.expect(false.B)
      }
    }
  }
}
