package edu.berkeley.cs.ucie.digital
package tilelink

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import scala.math._

// TileLink parameters for connecting the TL nodes to chipyard diplomacy

case class TileLinkParams(
    val address: BigInt,
    val addressRange: BigInt,
    val configAddress: BigInt,
    val inwardQueueDepth: Int,
    val outwardQueueDepth: Int
) {
  val CONFIG_ADDRESS = configAddress
  val ADDRESS = address
  val ADDR_RANGE = addressRange
  val BEAT_BYTES = 32 // 256 bits/8
  val opcodeWidth = 3
  val paramWidth = 3
  val sourceIDWidth = 8
  val sinkIDWidth = 8
  val addressWidth = 64
  val maskWidth = dataWidth/8
  val dataWidth = 256
  val sizeWidth = log2Ceil(dataWidth/8) // 5 bits if 256
  val deniedWidth = 1
  val corruptWidth = 1
}

/**
  * Class to pack A and D channels into one single bundle
  * Total 381 bits
  */
class TLBundleAUnionD(val tlParams: TileLinkParams) extends Bundle {
  val opcode = Input(UInt(tlParams.opcodeWidth.W))
  val param = Input(UInt(tlParams.paramWidth.W))
  val size = Input(UInt(tlParams.sizeWidth.W))
  val source = Input(UInt(tlParams.sourceIDWidth.W))
  val sink = Input(UInt(tlParams.sinkIDWidth.W))
  val address = Input(UInt(tlParams.addressWidth.W))
  val mask = Input(UInt(tlParams.maskWidth.W))
  val data = Input(UInt(tlParams.dataWidth.W))
  val denied = Input(UInt(tlParams.deniedWidth.W))
  val corrupt = Input(UInt(tlParams.corruptWidth.W))
}
