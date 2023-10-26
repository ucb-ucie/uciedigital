package edu.berkeley.cs.ucie.digital.d2dadapter

import chisel3._
import chiseltest._
import org.scalatest.funspec.AnyFunSpec
import chiseltest.simulator.WriteVcdAnnotation

class CRCGeneratorTest extends AnyFunSpec with ChiselScalatestTester {
  describe("CRCGenerator, 32-bit messages") {
    it("should produce h8BC as the 16-bit CRC of hF3D1AB23") {
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
        c.io.crc1_out.expect("h08".U)
        c.io.crc0_out.expect("hBC".U)
      }
    }

    it("should produce hD34D as the 16-bit CRC of hB481A391") {
        test(new CRCGenerator(32)).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.io.rst.poke(true.B)
        c.clock.step()
        c.io.rst.poke(false.B)
        c.io.data_in.poke("hB481A391".U)
        c.clock.step()
        c.io.data_val.poke(true.B)
        c.clock.step()
        c.io.data_val.poke(false.B)
        c.clock.step()
        while (c.io.crc_val.peek().litValue != 1) {
            c.clock.step()
        }
        c.io.crc1_out.expect("hD3".U)
        c.io.crc0_out.expect("h4D".U)


      }
    }

    it("should produce back to back valid 16-bit CRCs of hF3D1AB23 and hB481A391") {
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
        c.io.crc1_out.expect("h08".U)
        c.io.crc0_out.expect("hBC".U)

        c.io.rst.poke(true.B)
        c.clock.step()
        c.io.rst.poke(false.B)
        c.io.data_in.poke("hB481A391".U)
        c.clock.step()
        c.io.data_val.poke(true.B)
        c.clock.step()
        c.io.data_val.poke(false.B)
        c.clock.step()
        while (c.io.crc_val.peek().litValue != 1) {
            c.clock.step()
        }
        c.io.crc1_out.expect("hD3".U)
        c.io.crc0_out.expect("h4D".U)
      }
    }
  }

  describe("CRCGenerator, 1024-bit messages") {
    it("should produce h5557 as the 16-bit CRC of he3c3598edd...") {
        test(new CRCGenerator(1024)).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.io.rst.poke(true.B)
        c.clock.step()
        c.io.rst.poke(false.B)
        val num = "he3c3598e_dd1a3be2_d429ad05_2b927c61_c2ba41b6_fc7ac0df_093b5d8b_b754d97d_36b105a3_64fce07b_bc68ccef_3b391448_225bc955_7052a494_49168926_c2429be9_66775914_9bf34300_ceee9ae0_7b681d27_bba8b55f_0eadc080_b0955182_0d8200ce_31a58b25_6c8086f6_d535913e_e7867535_f5e15126_f682f852_0fb831c3_ba7e5b69"
        c.io.data_in.poke((num).U)
        c.clock.step()
        c.io.data_val.poke(true.B)
        c.clock.step()
        c.io.data_val.poke(false.B)
        c.clock.step()
        while (c.io.crc_val.peek().litValue != 1) {
            c.clock.step()
        }
        c.io.crc1_out.expect("h55".U)
        c.io.crc0_out.expect("h57".U)
      }
    }
  }
}
