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
        c.io.data_in.poke("hF3D1AB23".U)
        c.clock.step()
        c.io.data_val.poke(true.B)
        c.clock.step()
        c.io.data_val.poke(false.B)
        c.clock.step()
        while (c.io.crc_val.peek().litValue != 1) {
            c.clock.step()
        }
        c.io.crc1_out.expect("hC4".U)
        c.io.crc0_out.expect("h14".U)
      }
    }
  }
}
