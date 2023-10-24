package edu.berkeley.cs.ucie.digital.d2dadapter

import chisel3._
import chisel3.util._
import edu.berkeley.cs.ucie.digital.d2dadapter.CRC16Lookup

class CRCGenerator(width: Int) extends Module { // width is word size in bits (must be whole number of bytes)
    val io = IO(new Bundle {
        val data_in = Input(UInt(width.W))      // Accepts next word of data from client
        val data_val = Input(Bool())            // Signal from client that data at data_in is valid and can be processed

        // val clk = Input(Clock())             // synchronous clock signal
        val rst = Input(Bool())                 // reset CRC generator and registers


        val data_rdy = Output(Bool())           // Signal to client if generator is ready to accept another word of data

        val crc0_out = Output(UInt(8.W))        // CRC 0 Byte output 
        val crc1_out = Output(UInt(8.W))        // CRC 1 Byte output
        val crc_val = Output(Bool())            // Signal to client that data on crc0_out and crc1_out are valid
    })

    // Output data registers
    val CRC0 = RegInit(UInt(8.W), 0.U)
    val CRC1 = RegInit(UInt(8.W), 0.U)

    // CRC calculating registers
    val step = RegInit(UInt(log2Ceil(width/8).W), 0.U)    // Big enough to store byte position over width bits
    val temp = RegInit(UInt(8.W), 0.U)                      // Stores 1 byte of data for lookup calculations
    val Data_In = RegInit(UInt(width.W), 0.U)               // Latches full word of data when data_rdy and data_val

    // CRC calculating table
    val lookup = new CRC16Lookup

    // Reset logic
    when (io.rst) {
        // Reset output data registers
        CRC0 := 0.U
        CRC1 := 0.U

        // Propogate output data register
        io.crc0_out := CRC0
        io.crc1_out := CRC1

        // Signal Valid and Ready
        io.crc_val := true.B
        io.data_rdy := true.B

        // Reset CRC calculating registers
        step := 0.U
    }

    // TODO: Sequential CRC Calculation

    // Latch data on data_rdy and data_val
    when (io.data_val && io.data_rdy) {
        step := (width/8).U - 1.U  // Number of bytes in word

        Data_In := io.data_in
        io.data_rdy := false.B
        io.crc_val := false.B
    }

    // Computation not finished when step is not 0
    when (step > 0.U) {
        temp := CRC1 ^ Data_In(step*8.U - 1.U, step * 8.U)
        CRC1 := CRC0 ^ lookup.table(temp)(15, 8)    
        CRC0 := lookup.table(temp)(8, 0)
        step := step - 1.U
        if (step == 0.U) {
            io.crc0_out := CRC0
            io.crc1_out := CRC1
            io.crc_val := true.B
            io.data_rdy := true.B
        }
    }


}

