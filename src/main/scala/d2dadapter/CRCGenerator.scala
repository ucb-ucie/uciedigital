package edu.berkeley.cs.ucie.digital.d2dadapter

import chisel3._
import chisel3.util._

class CRCGenerator(width: Int) extends Module {
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
    val CRC_Val = RegInit(Bool(), 1.U)
    val Data_Rdy = RegInit(Bool(), 1.U)

    // CRC calculating registers
    val step = RegInit(UInt(log2Ceil(width).W), 0.U)


    // Reset logic
    when (io.rst) {
        // Reset output data registers
        CRC0 := 0.U
        CRC1 := 0.U
        CRC_Val := 1.U
        Data_Rdy := 0.U

        // Propogate output data register
        io.crc0_out := CRC0
        io.crc1_out := CRC1
        io.crc_val := CRC_Val
        io.data_rdy := Data_Rdy

        // Reset CRC calculating registers
        step := 0.U
    }




}

