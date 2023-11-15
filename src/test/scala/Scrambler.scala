package edu.berkeley.cs.ucie.digital
package interfaces

import chisel3._
import chiseltest._
import org.scalatest.funspec.AnyFunSpec

// Test doesn't work

class ScramblerTest extends AnyFunSpec with ChiselScalatestTester {
  describe("Scrambler") {
    it("4 lane scrambler test") {
      test(new UCIeScrambler(new AfeParams(1,16,16), 16, 4)) { c =>
        val in = Vec(4, UInt(16.W))
        val out = Vec(4, UInt(16.W))
        // in(0) := 1.U(23.W)
        // in(1) := 1012.U(23.W)
        // in(2) := 823.U(23.W)
        // in(3) := 134.U(23.W)
        // out(0) := 49085.U(23.W)
        // out(1) := 1103.U(23.W)
        // out(2) := 50263.U(23.W)
        // out(3) := 49245.U(23.W)

        // c.reset.poke(true.B)
        // c.clock.step()
        // c.clock.step()
        // c.reset.poke(false.B)
        // c.clock.step()
        // c.io.valid.poke(true.B)

        // c.io.data_in.poke(in)
        // c.io.data_out.expect(out)
      }
    }
  }
}