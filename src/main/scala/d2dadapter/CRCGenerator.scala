package edu.berkeley.cs.ucie.digital.d2dadapter

import chisel3._
import chisel3.util._
import chisel3.util.Decoupled
import edu.berkeley.cs.ucie.digital.d2dadapter.CRC16Lookup

// TODO: change variable names to snake_case


/** Generates the CRC of data using the polynomial x^16 + x^15 + x^2 + 1
  * 
  */

class CRCGenerator(width: Int) extends Module { // width is word size in bits (must be whole number of bytes)
    val io = IO(new Bundle {
        val rst = Input(Bool())                 

        val message = Flipped(Decoupled(Bits(width.W)))

        val crc0_out = Output(UInt(8.W))        // CRC 0 Byte output 
        val crc1_out = Output(UInt(8.W))        // CRC 1 Byte output
        val crc_val = Output(Bool())            // Signal to consumer that data on crc0_out and crc1_out are valid
    })

    // Output data registers
    val crc0 = RegInit(UInt(8.W), 0.U)
    val crc1 = RegInit(UInt(8.W), 0.U)

    // Output signal registers
    val message_ready = RegInit(Bool(), false.B)
    val crc_val = RegInit(Bool(), false.B)

    // CRC calculating registers
    val step = RegInit(UInt(log2Ceil(width/8+1).W), 0.U)      // Store number of bytes in width bits
    val message_bits = RegInit(Bits(width.W), 0.U)                 // Latches full word of data when message_ready and data_val

    // CRC calculating table
    val lookup = new CRC16Lookup

    // Propogate output data register
    io.crc0_out := crc0
    io.crc1_out := crc1

    // Signal Valid and Ready
    io.crc_val := crc_val
    io.message.ready := message_ready

    // Reset logic
    when (io.rst) {
        // Reset output data registers
        crc0 := 0.U
        crc1 := 0.U

        // Signal Valid and Ready
        crc_val := true.B
        message_ready := true.B

        // Reset CRC calculating registers
        step := 0.U
    }

    // Latch data on message_ready and data_val
    when (io.message.valid & io.message.ready) {
        step := (width/8).U     // Number of bytes in word
        message_bits := io.message.bits
        crc_val := false.B
        message_ready := false.B
    }

    // Computation not finished when step is not 0
    when (step > 0.U) {
        crc1 := crc0 ^ lookup.table(crc1 ^ message_bits(width-1, width-8))(15, 8)    
        crc0 := lookup.table(crc1 ^ message_bits(width-1, width-8))(7, 0)
        message_bits := message_bits << 8
        step := step - 1.U
        crc_val := false.B
        message_ready := false.B
    } .otherwise {
        crc_val := true.B
        message_ready := true.B
    }
}

