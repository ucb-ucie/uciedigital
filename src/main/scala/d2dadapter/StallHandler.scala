package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
import chisel3.util._
import chisel3.experimental._

class FDIStallHandlerIO() extends Bundle{
    // FDI: send request
    // RDI: receive request and perform actual stall
    val linkmngt_stallreq = Input(Bool())// 
    val linkmngt_stallcpt = Output(Bool())// complete stall
    val fdi_pl_stallreq = Output(Bool())
    val fdi_lp_stallack = Input(Bool())
}

class RDIStallHandlerIO() extends Bundle{
    // FDI: send request
    // RDI: receive request and perform actual stall
    val mainband_stallreq = Output(Bool())// 
    val mainband_stallcpt = Input(Bool())// complete stall
    val rdi_pl_stallreq = Input(Bool())
    val rdi_lp_stallack = Output(Bool())
}

class FDIStallHandler() extends Module{
    val io = IO(new FDIStallHandlerIO())
    val fdi_lp_stallreq_reg = RegInit(false.B)
    val linkmngt_stallcpt_reg = RegInit(false.B)

    io.fdi_pl_stallreq := fdi_lp_stallreq_reg
    io.linkmngt_stallcpt := linkmngt_stallcpt_reg

    val stall_handshake_state_reg = RegInit(StallHandshakeState.IDLE)

    stall_handshake_state_reg := stall_handshake_state_reg
    fdi_lp_stallreq_reg := false.B
    linkmngt_stallcpt_reg := linkmngt_stallcpt_reg

    switch(stall_handshake_state_reg){
        is(StallHandshakeState.IDLE){
            when(io.linkmngt_stallreq && ~io.fdi_lp_stallack){
                fdi_lp_stallreq_reg := true.B
                linkmngt_stallcpt_reg := false.B
                stall_handshake_state_reg := StallHandshakeState.REQSNT
            }.otherwise{
                fdi_lp_stallreq_reg := false.B
                linkmngt_stallcpt_reg := false.B
                stall_handshake_state_reg := stall_handshake_state_reg
            }
        }
        is(StallHandshakeState.REQSNT){
            when(io.fdi_lp_stallack){
                fdi_lp_stallreq_reg := false.B
                linkmngt_stallcpt_reg := false.B
                stall_handshake_state_reg := StallHandshakeState.REQFALL
            }.otherwise{
                fdi_lp_stallreq_reg := true.B
                linkmngt_stallcpt_reg := false.B
                stall_handshake_state_reg := stall_handshake_state_reg
            }
        }
        is(StallHandshakeState.REQFALL){
            when(~io.fdi_lp_stallack){
                fdi_lp_stallreq_reg := false.B
                linkmngt_stallcpt_reg := true.B
                stall_handshake_state_reg := StallHandshakeState.COMPLETE             
            }.otherwise{
                fdi_lp_stallreq_reg := false.B
                linkmngt_stallcpt_reg := false.B
                stall_handshake_state_reg := stall_handshake_state_reg                
            }
        }
        is(StallHandshakeState.COMPLETE){
            when(~io.linkmngt_stallreq){
                fdi_lp_stallreq_reg := false.B
                linkmngt_stallcpt_reg := false.B
                stall_handshake_state_reg := StallHandshakeState.IDLE            
            }.otherwise{
                fdi_lp_stallreq_reg := false.B
                linkmngt_stallcpt_reg := true.B
                stall_handshake_state_reg := stall_handshake_state_reg                
            }
        }
    }
}


class RDIStallHandler() extends Module{
    val io = IO(new RDIStallHandlerIO())

    val rdi_lp_stallack_reg = RegInit(false.B)
    val mainband_stallreq_reg = RegInit(false.B)
    val stall_handshake_state_reg = RegInit(StallHandshakeState.IDLE)

    io.rdi_lp_stallack := rdi_lp_stallack_reg
    io.mainband_stallreq := mainband_stallreq_reg

    

    switch(stall_handshake_state_reg){
        is(StallHandshakeState.IDLE){
            when(io.rdi_pl_stallreq){
                mainband_stallreq_reg := true.B
                rdi_lp_stallack_reg  := false.B
                stall_handshake_state_reg := StallHandshakeState.REQSNT
            }.otherwise{
                mainband_stallreq_reg := false.B
                rdi_lp_stallack_reg  := false.B
                stall_handshake_state_reg := stall_handshake_state_reg
            }
        }
        is(StallHandshakeState.REQSNT){
            when(io.mainband_stallcpt){
                mainband_stallreq_reg := true.B
                rdi_lp_stallack_reg  := true.B
                stall_handshake_state_reg := StallHandshakeState.REQFALL
            }.otherwise{
                mainband_stallreq_reg := true.B
                rdi_lp_stallack_reg  := false.B
                stall_handshake_state_reg := stall_handshake_state_reg
            }
        }
        is(StallHandshakeState.REQFALL){
            when(~io.rdi_pl_stallreq){
                mainband_stallreq_reg := false.B
                rdi_lp_stallack_reg  := false.B
                stall_handshake_state_reg := StallHandshakeState.IDLE           
            }.otherwise{
                mainband_stallreq_reg := true.B
                rdi_lp_stallack_reg  := true.B
                stall_handshake_state_reg := stall_handshake_state_reg                
            }
        }
    }
}
