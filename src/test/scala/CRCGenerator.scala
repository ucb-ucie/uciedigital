package edu.berkeley.cs.ucie.digital.d2dadapter

import chisel3._
import chiseltest._
import org.scalatest.funspec.AnyFunSpec

class CRCGeneratorTest extends AnyFunSpec with ChiselScalatestTester {
  describe("CRCGenerator") {
    it("Produce 16-bit CRC") {
        test(new CRCGenerator(32)) { c =>
        //c.io.rst(true.B)
        //c.clock.step()
        //c.io.rst(false.B)
        c.io.data_in.poke(0x26BC.U)
        c.clock.step()
        c.io.data_val.poke(true.B)
        c.clock.step()
        while (c.io.crc_val != true.B) {
            c.clock.step()
        }
        c.io.crc0_out.expect(0x1B.U)
        c.io.crc1_out.expect(0xD1.U)
      }
    }
  }
}
