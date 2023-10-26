package edu.berkeley.cs.ucie.digital.d2dadapter

import chisel3._
import chisel3.util._
import edu.berkeley.cs.ucie.digital.d2dadapter.CRC16Lookup

// TODO: change variable names to snake_case


/** Generates the CRC of data using the polynomial x^16 + x^15 + x^2 + 1
  * 
  */

class CRCGenerator(width: Int) extends Module { // width is word size in bits (must be whole number of bytes)
    val io = IO(new Bundle {
        val rst = Input(Bool())                 // reset CRC generator and registers

        // TODO: Replace with ReadyValid3IO
        val data_in = Input(Bits(width.W))      // Accepts next word of data from consumer
        val data_val = Input(Bool())            // Signal from consumer that data at data_in is valid and can be processed
        val data_rdy = Output(Bool())           // Signal to consumer if generator is ready to accept another word of data
                                                // TODO: Replace with ReadyValid3IO

        val crc0_out = Output(UInt(8.W))        // CRC 0 Byte output 
        val crc1_out = Output(UInt(8.W))        // CRC 1 Byte output
        val crc_val = Output(Bool())            // Signal to consumer that data on crc0_out and crc1_out are valid
    })

    // Output data registers
    val crc0 = RegInit(UInt(8.W), 0.U)
    val crc1 = RegInit(UInt(8.W), 0.U)

    // Output signal registers
    val data_rdy = RegInit(Bool(), false.B)
    val crc_val = RegInit(Bool(), false.B)

    // CRC calculating registers
    val step = RegInit(UInt(log2Ceil(width/8+1).W), 0.U)      // Store number of bytes in width bits
    val data_in = RegInit(Bits(width.W), 0.U)                 // Latches full word of data when data_rdy and data_val

    // CRC calculating table
    val lookup = new CRC16Lookup

    // Propogate output data register
    io.crc0_out := crc0
    io.crc1_out := crc1

    // Signal Valid and Ready
    io.crc_val := crc_val
    io.data_rdy := data_rdy

    // Reset logic
    when (io.rst) {
        // Reset output data registers
        crc0 := 0.U
        crc1 := 0.U

        // Signal Valid and Ready
        crc_val := true.B
        data_rdy := true.B

        // Reset CRC calculating registers
        step := 0.U
    }

    // Latch data on data_rdy and data_val
    when (io.data_val & io.data_rdy) {
        step := (width/8).U     // Number of bytes in word
        data_in := io.data_in
        crc_val := false.B
        data_rdy := false.B
    }

    // Computation not finished when step is not 0
    when (step > 0.U) {
        crc1 := crc0 ^ lookup.table(crc1 ^ data_in(width-1, width-8))(15, 8)    
        crc0 := lookup.table(crc1 ^ data_in(width-1, width-8))(7, 0)
        data_in := data_in << 8
        step := step - 1.U
        crc_val := false.B
        data_rdy := false.B
    } .otherwise {
        crc_val := true.B
        data_rdy := true.B
    }
}

