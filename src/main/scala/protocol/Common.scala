package ucie.tlfront

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import scala.math._

// Protocol layer and TileLink parameters

case class ProtocolLayerParams(
    val tlBaseAddress: BigInt,
    val configBaseAddress: BigInt,
) {
  val beatByte: Int = 32
}
