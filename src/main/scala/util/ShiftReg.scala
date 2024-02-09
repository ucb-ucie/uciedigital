package edu.berkeley.cs.ucie.digital
package util

import chisel3._

// Similar to the Chisel ShiftRegister but allows the user to suggest a
// name to the registers that get instantiated, and
// to provide a reset value.
object ShiftRegInit {
  def apply[T <: Data](in: T, n: Int, init: T, name: Option[String] = None): T =
    (0 until n).foldRight(in) {
      case (i, next) => {
        val r = RegNext(next, init)
        name.foreach { na => r.suggestName(s"${na}_${i}") }
        r
      }
    }
}

/** These wrap behavioral shift registers into specific modules to allow for
  * backend flows to replace or constrain them properly when used for CDC
  * synchronization, rather than buffering.
  *
  * The different types vary in their reset behavior: AsyncResetShiftReg --
  * Asynchronously reset register array A W(width) x D(depth) sized array is
  * constructed from D instantiations of a W-wide register vector. Functionally
  * identical to AsyncResetSyncrhonizerShiftReg, but only used for timing
  * applications
  */

abstract class AbstractPipelineReg(w: Int = 1) extends Module {
  val io = IO(new Bundle {
    val d = Input(UInt(w.W))
    val q = Output(UInt(w.W))
  })
}

object AbstractPipelineReg {
  def apply[T <: Data](
      gen: => AbstractPipelineReg,
      in: T,
      name: Option[String] = None,
  ): T = {
    val chain = Module(gen)
    name.foreach { chain.suggestName(_) }
    chain.io.d := in.asUInt
    chain.io.q.asTypeOf(in)
  }
}
