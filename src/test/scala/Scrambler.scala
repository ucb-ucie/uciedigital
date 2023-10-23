package edu.berkeley.cs.ucie.digital

import chisel3._
import chiseltest._
import org.scalatest.funspec.AnyFunSpec

class ScramblerTest extends AnyFunSpec with ChiselScalatestTester {
  describe("Scrambler") {
    it("should scramble one lane") {
      test(new Scrambler()) { c =>
        c.io.rst.poke(true.B)
        c.clock.step()
        c.clock.step()
        c.io.rst.poke(false.B)
        c.clock.step()
        c.io.valid.poke(true.B)
        c.io.L0_in.poke(9628)
        c.io.L0_out.expect(39456)
        c.clock.step()
        c.io.L0_in.poke(13458)
        c.io.L0_out.expect(48271)
      }
    }
  }
}