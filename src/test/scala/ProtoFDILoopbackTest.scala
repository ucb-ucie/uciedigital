package edu.berkeley.cs.ucie.digital

import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

import org.chipsalliance.cde.config.{Config, Parameters}
import chiseltest._
import chiseltest.simulator.{VerilatorFlags, VerilatorCFlags, SimulatorDebugAnnotation, VerilatorLinkFlags}
import org.scalatest.flatspec.AnyFlatSpec
import chisel3._

class ProtoLBChiselTester(implicit val p: Parameters) extends Module {
  val th = Module(new ProtoFDILBTestHarness)
  when (th.io.success) { stop() }
}

abstract class BaseProtoLBTest(
  gen: Parameters => Module,
  configs: Seq[Config],
  extraVerilatorFlags: Seq[String] = Nil) extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "UCIe-Protocol"

  configs.foreach { config =>
    it should s"pass test with config ${config.getClass.getName}" in {
      implicit val p: Parameters = config
      test(gen(p))
        .withAnnotations(Seq(
          SimulatorDebugAnnotation,
          VerilatorBackendAnnotation,
          VerilatorFlags(extraVerilatorFlags),
          VerilatorLinkFlags(Seq(
            "-Wl,--allow-multiple-definition",
            "-fcommon")),
          VerilatorCFlags(Seq(
            "-DNO_VPI",
            "-fcommon",
            "-fpermissive"))
        ))
        .runUntilStop(timeout = 1000 * 10000)
    }
  }
}

abstract class ProtoLBTest(configs: Seq[Config]) extends BaseProtoLBTest(p => new ProtoLBChiselTester()(p), configs)

// these tests allow you to run an infividual config
//class ProtoLBTest00 extends ProtoLBTest(Seq(new ProtoLBTestConfig00))