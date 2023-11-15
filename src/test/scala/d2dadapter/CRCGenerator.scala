package edu.berkeley.cs.ucie.digital.d2dadapter

import chisel3._
import chiseltest._
import org.scalatest.funspec.AnyFunSpec
import chiseltest.simulator.WriteVcdAnnotation

class CRCGeneratorTest extends AnyFunSpec with ChiselScalatestTester {
  /*
  describe("CRCGenerator, 32-bit messages") {
    it("should produce h8BC as the 16-bit CRC of hF3D1AB23") {
      test(new CRCGenerator(32)).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.io.message.initSource().setSourceClock(c.clock)
        c.io.crc.initSink().setSinkClock(c.clock)
        c.io.message.enqueueNow("hF3D1AB23".U)
        c.io.crc.waitForValid()
        c.io.crc.expectDequeueNow("h08BC".U)
      }
    }

    it("should produce hD34D as the 16-bit CRC of hB481A391") {
      test(new CRCGenerator(32)).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.io.message.initSource().setSourceClock(c.clock)
        c.io.crc.initSink().setSinkClock(c.clock)
        c.io.message.enqueueNow("hB481A391".U)
        c.io.crc.waitForValid()
        c.io.crc.expectDequeueNow("hD34D".U)
      }
    }

    it(
      "should produce back to back valid 16-bit CRCs of hF3D1AB23 and hB481A391",
    ) {
      test(new CRCGenerator(32)).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.io.message.initSource().setSourceClock(c.clock)
        c.io.crc.initSink().setSinkClock(c.clock)

        c.io.message.enqueueNow("hF3D1AB23".U)
        c.io.crc.waitForValid()
        c.io.crc.expectDequeueNow("h08BC".U)

        c.io.message.enqueue("hB481A391".U)
        c.io.crc.waitForValid()
        c.io.crc.expectDequeueNow("hD34D".U)
      }
    }
  }
  */

  describe("CRCGenerator, 1024-bit messages") {
    it("should produce h5557 as the 16-bit CRC of he3c3598e...") {
      test(new CRCGenerator(1024, 1)).withAnnotations(Seq(WriteVcdAnnotation)) {
        c =>
          c.io.message.initSource().setSourceClock(c.clock)
          c.io.crc.initSink().setSinkClock(c.clock)
          val num =
            ("he3c3598e_dd1a3be2_d429ad05_2b927c61_" +
              "c2ba41b6_fc7ac0df_093b5d8b_b754d97d_" +
              "36b105a3_64fce07b_bc68ccef_3b391448_" +
              "225bc955_7052a494_49168926_c2429be9_" +
              "66775914_9bf34300_ceee9ae0_7b681d27_" +
              "bba8b55f_0eadc080_b0955182_0d8200ce_" +
              "31a58b25_6c8086f6_d535913e_e7867535_" +
              "f5e15126_f682f852_0fb831c3_ba7e5b69")
          c.io.message.enqueueNow((num).U)
          c.io.crc.waitForValid()
          c.io.crc.expectDequeueNow("h5557".U)
      }
    }

    it("should produce h5b39 as the 16-bit CRC of h21940141...") {
      test(new CRCGenerator(1024, 1)).withAnnotations(Seq(WriteVcdAnnotation)) {
        c =>
          c.io.message.initSource().setSourceClock(c.clock)
          c.io.crc.initSink().setSinkClock(c.clock)
          val num =
            "h21940141_94204c5b_66aadb33_39ff52dd_" +
              "59187e4d_8d7f45a0_92f32508_9fdc1174_" +
              "62f98566_b5767b24_38ffcf48_dcd48173_" +
              "0d2d0706_19653a06_a208e4d2_ed55d4a7_" +
              "6cf0e086_db7be8f6_95d30337_c72e6072_" +
              "1651c46c_3f9a38dd_50d0b5a9_4a8f23e3_" +
              "f1915b39_e0570141_22bdfa54_293ad6fe_" +
              "3ef3d240_ae894873_835dc657_881e5c7d"
          c.io.message.enqueueNow((num).U)
          c.io.crc.waitForValid()
          c.io.crc.expectDequeueNow("h5B39".U)
      }
    }

    it(
      "should produce back to back valid 16-bit CRCs of he3c3598e... and h21940141...",
    ) {
      test(new CRCGenerator(1024, 1)).withAnnotations(Seq(WriteVcdAnnotation)) {
        c =>
          c.io.message.initSource().setSourceClock(c.clock)
          c.io.crc.initSink().setSinkClock(c.clock)
          val num1 =
            ("he3c3598e_dd1a3be2_d429ad05_2b927c61_" +
              "c2ba41b6_fc7ac0df_093b5d8b_b754d97d_" +
              "36b105a3_64fce07b_bc68ccef_3b391448_" +
              "225bc955_7052a494_49168926_c2429be9_" +
              "66775914_9bf34300_ceee9ae0_7b681d27_" +
              "bba8b55f_0eadc080_b0955182_0d8200ce_" +
              "31a58b25_6c8086f6_d535913e_e7867535_" +
              "f5e15126_f682f852_0fb831c3_ba7e5b69")
          c.io.message.enqueueNow((num1).U)
          c.io.crc.waitForValid()
          c.io.crc.expectDequeueNow("h5557".U)

          val num2 = "h21940141_94204c5b_66aadb33_39ff52dd_" +
            "59187e4d_8d7f45a0_92f32508_9fdc1174_" +
            "62f98566_b5767b24_38ffcf48_dcd48173_" +
            "0d2d0706_19653a06_a208e4d2_ed55d4a7_" +
            "6cf0e086_db7be8f6_95d30337_c72e6072_" +
            "1651c46c_3f9a38dd_50d0b5a9_4a8f23e3_" +
            "f1915b39_e0570141_22bdfa54_293ad6fe_" +
            "3ef3d240_ae894873_835dc657_881e5c7d"
          c.io.message.enqueue((num2).U)
          c.io.crc.waitForValid()
          c.io.crc.expectDequeueNow("h5B39".U)
      }
    }
  }

  /*
  describe("CRCGenerator, 1023-bit width") {
    it("should fail due to invalid width = 1023 % 8 != 0") {
      assertThrows[AssertionError] {
        test(new CRCGenerator(1023)) { c => }
      }
    }
  }
  */
}
