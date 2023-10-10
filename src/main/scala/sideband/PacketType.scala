package edu.berkeley.cs.ucie.digital
package sideband

import chisel3._

/** Opcode <-> Packet Type (unspecified values are reserved) */
object PacketType extends ChiselEnum {

  /** 32b Memory Read */
  val MemoryRead32b = Value("b00000".U(5.W))

  /** 32b Memory Write */
  val MemoryWrite32b = Value("b00001".U(5.W))

  /** 32b Configuration Read */
  val ConfigurationRead32b = Value("b00100".U(5.W))

  /** 32b Configuration Write */
  val ConfigurationWrite32b = Value("b00101".U(5.W))

  /** 64b Memory Read */
  val MemoryRead64b = Value("b01000".U(5.W))

  /** 64b Memory Write */
  val MemoryWrite64b = Value("b01001".U(5.W))

  /** 64b Configuration Read */
  val ConfigurationRead64b = Value("b01100".U(5.W))

  /** 64b Configuration Write */
  val ConfigurationWrite64b = Value("b01101".U(5.W))

  /** Completion without Data */
  val CompletionWithoutData = Value("b10000".U(5.W))

  /** Completion with 32b Data */
  val CompletionWith32bData = Value("b10001".U(5.W))

  /** Message without Data */
  val MessageWithoutData = Value("b10010".U(5.W))

  /** Completion with 64b Data */
  val CompletionWith64bData = Value("b11001".U(5.W))

  /** Message with 64b Data */
  val MessageWith64bData = Value("b11011".U(5.W))
}
