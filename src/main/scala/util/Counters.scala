package edu.berkeley.cs.ucie.digital
package util

import chisel3._
import chisel3.util._

case class WideCounter(
    width: Int,
    inc: UInt = 1.U,
    reset: Boolean = true,
    inhibit: Bool = false.B,
) {
  private val isWide = width > (2 * inc.getWidth)
  private val smallWidth = if (isWide) inc.getWidth max log2Up(width) else width
  private val small =
    if (reset) RegInit(0.U(smallWidth.W)) else Reg(UInt(smallWidth.W))
  private val nextSmall = small +& inc
  when(!inhibit) { small := nextSmall }

  private val large = if (isWide) {
    val r =
      if (reset) RegInit(0.U((width - smallWidth).W))
      else Reg(UInt((width - smallWidth).W))
    when(nextSmall(smallWidth) && !inhibit) { r := r + 1.U }
    r
  } else null

  val value = if (isWide) Cat(large, small) else small
  lazy val carryOut = {
    val lo = (small ^ nextSmall) >> 1
    if (!isWide)
      lo
    else {
      val hi = Mux(nextSmall(smallWidth), large ^ (large +& 1.U), 0.U) >> 1
      Cat(hi, lo)
    }
  }

  def :=(x: UInt) = {
    small := x
    if (isWide) large := x >> smallWidth
  }
}
