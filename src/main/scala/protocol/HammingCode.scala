package edu.berkeley.cs.ucie.digital
package protocol

import chisel3._
import chisel3.util._

class HammingEncode(val protoParams: ProtocolLayerParams) extends Module {
  val io = IO(new Bundle {
    val data = Input(UInt(protoParams.ucieNonEccWidth.W))
    val checksum = Output(UInt(protoParams.ucieEccWidth.W))
  })

  def hammingEncode(data: Vec[UInt]): UInt = { 
    val p1 = Wire(UInt(1.W))
    val p1_taps = Wire(Vec(protoParams.ucieNonEccWidth, UInt(1.W)))
    data.zipWithIndex.map { case (d, i) => 
      i % 2 >= 1 match {
        case true => p1_taps(i) := 1.U
        case false => p1_taps(i) := 0.U
      }
    }
    p1 := (p1_taps.asUInt & data.asUInt).xorR
    val p2 = Wire(UInt(1.W))
    val p2_taps = Wire(Vec(protoParams.ucieNonEccWidth, UInt(1.W)))
    data.zipWithIndex.map { case (d, i) => 
      i % 4 >= 2 match {
        case true => p2_taps(i) := 1.U
        case false => p2_taps(i) := 0.U
      }
    }
    p2 := (p2_taps.asUInt & data.asUInt).xorR
    val p4 = Wire(UInt(1.W))
    val p4_taps = Wire(Vec(protoParams.ucieNonEccWidth, UInt(1.W)))
    data.zipWithIndex.map { case (d, i) => 
      i % 8 >= 4 match {
        case true => p4_taps(i) := 1.U
        case false => p4_taps(i) := 0.U
      }
    }
    p4 := (p4_taps.asUInt & data.asUInt).xorR
    val p8 = Wire(UInt(1.W))
    val p8_taps = Wire(Vec(protoParams.ucieNonEccWidth, UInt(1.W)))
    data.zipWithIndex.map { case (d, i) => 
      i % 16 >= 8 match {
        case true => p8_taps(i) := 1.U
        case false => p8_taps(i) := 0.U
      }
    }
    p8 := (p8_taps.asUInt & data.asUInt).xorR
    val p16 = Wire(UInt(1.W))
    val p16_taps = Wire(Vec(protoParams.ucieNonEccWidth, UInt(1.W)))
    data.zipWithIndex.map { case (d, i) => 
      i % 32 >= 16 match {
        case true => p16_taps(i) := 1.U
        case false => p16_taps(i) := 0.U
      }
    }
    p16 := (p16_taps.asUInt & data.asUInt).xorR
    val p32 = Wire(UInt(1.W))
    val p32_taps = Wire(Vec(protoParams.ucieNonEccWidth, UInt(1.W)))
    data.zipWithIndex.map { case (d, i) => 
      i % 64 >= 32 match {
        case true => p32_taps(i) := 1.U
        case false => p32_taps(i) := 0.U
      }
    }
    p32 := (p32_taps.asUInt & data.asUInt).xorR
    val p64 = Wire(UInt(1.W))
    val p64_taps = Wire(Vec(protoParams.ucieNonEccWidth, UInt(1.W)))
    data.zipWithIndex.map { case (d, i) => 
      i % 128 >= 64 match {
        case true => p64_taps(i) := 1.U
        case false => p64_taps(i) := 0.U
      }
    }
    p64 := (p64_taps.asUInt & data.asUInt).xorR
    val p128 = Wire(UInt(1.W))
    val p128_taps = Wire(Vec(protoParams.ucieNonEccWidth, UInt(1.W)))
    data.zipWithIndex.map { case (d, i) => 
      i % 256 >= 128 match {
        case true => p128_taps(i) := 1.U
        case false => p128_taps(i) := 0.U
      }
    }
    p128 := (p128_taps.asUInt & data.asUInt).xorR
    val p256 = Wire(UInt(1.W))
    val p256_taps = Wire(Vec(protoParams.ucieNonEccWidth, UInt(1.W)))
    data.zipWithIndex.map { case (d, i) => 
      i % 512 >= 256 match {
        case true => p256_taps(i) := 1.U
        case false => p256_taps(i) := 0.U
      }
    }
    p256 := (p256_taps.asUInt & data.asUInt).xorR
    Cat(p1, p2, p4, p8, p16, p32, p64, p128, p256)
  }

  io.checksum := hammingEncode(io.data.asTypeOf(Vec(protoParams.ucieNonEccWidth, UInt(1.W))))
}

class HammingDecode(val protoParams: ProtocolLayerParams) extends Module {
  val io = IO(new Bundle {
    val data = Input(UInt(protoParams.ucieNonEccWidth.W))
    val checksum = Input(UInt(protoParams.ucieEccWidth.W))
    val matches = Output(Bool())
  })
  val hammingEncode = Module(new HammingEncode(protoParams))
  hammingEncode.io.data := io.data
  io.matches := hammingEncode.io.checksum === io.checksum
}

