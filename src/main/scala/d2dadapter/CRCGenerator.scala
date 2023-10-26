package edu.berkeley.cs.ucie.digital.d2dadapter

import chisel3._
import chisel3.util._
import edu.berkeley.cs.ucie.digital.d2dadapter.CRC16Lookup

// TODO: change variable names to snake_case



class CRCGenerator(width: Int) extends Module { // width is word size in bits (must be whole number of bytes)
    val io = IO(new Bundle {
        val data_in = Input(Bits(width.W))      // Accepts next word of data from consumer
        val data_val = Input(Bool())            // Signal from consumer that data at data_in is valid and can be processed

        // val clk = Input(Clock())             // synchronous clock signal
        val rst = Input(Bool())                 // reset CRC generator and registers

                                                // TODO: Replace with ReadyValid3IO

        val data_rdy = Output(Bool())           // Signal to consumer if generator is ready to accept another word of data

        val crc0_out = Output(UInt(8.W))        // CRC 0 Byte output 
        val crc1_out = Output(UInt(8.W))        // CRC 1 Byte output
        val crc_val = Output(Bool())            // Signal to consumer that data on crc0_out and crc1_out are valid
    })

    // Output data registers
    val CRC0 = RegInit(UInt(8.W), 0.U)
    val CRC1 = RegInit(UInt(8.W), 0.U)

    // Ouput signal resgisters
    val Data_Rdy = RegInit(Bool(), false.B)
    val CRC_Val = RegInit(Bool(), false.B)


    // CRC calculating registers
    val step = RegInit(UInt(log2Ceil(width/8+1).W), 0.U)      // Big enough to store byte position over width bits
    val Data_In = RegInit(Bits(width.W), 0.U)              // Latches full word of data when data_rdy and data_val
    

    // CRC calculating table
    val lookup = new CRC16Lookup

    // Propogate output data register
    io.crc0_out := CRC0
    io.crc1_out := CRC1

    // Signal Valid and Ready
    io.crc_val := CRC_Val
    io.data_rdy := Data_Rdy

    // Reset logic
    when (io.rst) {
        // Reset output data registers
        CRC0 := 0.U
        CRC1 := 0.U

        // Signal Valid and Ready
        CRC_Val := true.B
        Data_Rdy := true.B

        // Reset CRC calculating registers
        step := 0.U
    }

    // TODO: Sequential CRC Calculation

    // Latch data on data_rdy and data_val
    when (io.data_val & io.data_rdy) {
        step := (width/8).U   // Number of bytes in word
        Data_In := io.data_in
        CRC_Val := false.B
        Data_Rdy := false.B
    }

    // Computation not finished when step is not 0
    when (step > 0.U) {
        CRC1 := CRC0 ^ lookup.table(CRC1 ^ Data_In(width-1, width-8))(15, 8)    
        CRC0 := lookup.table(CRC1 ^ Data_In(width-1, width-8))(7, 0)
        Data_In := Data_In << 8
        step := step - 1.U
        CRC_Val := false.B
        Data_Rdy := false.B
    } .otherwise {
        CRC_Val := true.B
        Data_Rdy := true.B
    }


}

