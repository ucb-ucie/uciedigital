package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import interfaces._
import sideband._

class D2DAdapterTest extends AnyFlatSpec with ChiselScalatestTester {
    val fdiParams = new FdiParams(width = 8, dllpWidth = 8, sbWidth = 32)
    val rdiParams = new RdiParams(width = 8, sbWidth = 32)
    val sbParams = new SidebandParams
    behavior of "D2DAdapterTest"
    it should "don't know how to check" in {
        test(new D2DAdapter(fdiParams, rdiParams, sbParams)) { c => 
            println("Do nothing")
        }
    }
}