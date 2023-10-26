package edu.berkeley.cs.ucie.digital.d2dadapter

import chisel3._
import chiseltest._
import org.scalatest.funspec.AnyFunSpec
import chiseltest.simulator.WriteVcdAnnotation




class CRCGeneratorTest extends AnyFunSpec with ChiselScalatestTester {
  describe("CRCGenerator") {
    it("Produce 16-bit CRC") {
        test(new CRCGenerator(32)).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.io.rst.poke(true.B)
        c.clock.step()
        c.io.rst.poke(false.B)
        c.io.data_in.poke(0x7E348ADB)
        c.clock.step()
        c.io.data_val.poke(true.B)
        c.clock.step()
        while (c.io.crc_val != true.B) {
            c.clock.step()
        }
        c.io.crc1_out.expect(0xDD.U)
        c.io.crc0_out.expect(0x53.U)
      }
    }
  }
}
