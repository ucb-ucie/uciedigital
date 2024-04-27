package edu.berkeley.cs.ucie.digital
package protocol

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import scala.math._

import tilelink._

// Protocol parameters to convert TL packets to UCIe Flits
case class ProtocolLayerParams() {
  val ucieFlitWidth = 64 // flit width
  val ucieFlitSize = 4 // number of ucie flits
  val ucieNonEccWidth = 448 // width of the actual data bits
  val ucieEccWidth = 64 // width of the ECC bits
  val hostIDWidth = 8 // hostID of the initiator chiplet
  val partnerIDWidth = 8 // partnerID of the consumer chiplet
  val creditWidth = 4 // credit width per channel (Total = 4*5)
  val reservedCmdWidth = 24 // width of the reserved bits in cmd header
}

object UCIProtoMsgTypes extends ChiselEnum {
    val TLA = Value(0x0.U(4.W))
    val TLB = Value(0x1.U(4.W))
    val TLC = Value(0x2.U(4.W))
    val TLD = Value(0x3.U(4.W))
    val TLE = Value(0x4.U(4.W))
    val Reserved0 = Value(0x5.U(4.W))
    val Reserved1 = Value(0x6.U(4.W))
    val Reserved2 = Value(0x7.U(4.W))
    val Reserved3 = Value(0x8.U(4.W))
}

/**
  * MsgType defines the protocol and msg type, for now its only TL, could be extended to
  * AXI, other Protocols, debug, discovery, etc.
  * The reservedCmd part should be extended to add functionality like QoS, security, etc.
  * DISCLAMER: Make sure these adds up to 64 bits
  */ 
class UCICmdFormat(val proto: ProtocolLayerParams) extends Bundle {
    val msgType = Output(UCIProtoMsgTypes())
    val hostID = Output(UInt(proto.hostIDWidth.W))
    val partnerID = Output(UInt(proto.partnerIDWidth.W))
    val tlACredit = Output(UInt(proto.creditWidth.W))
    val tlBCredit = Output(UInt(proto.creditWidth.W))
    val tlCCredit = Output(UInt(proto.creditWidth.W))
    val tlDCredit = Output(UInt(proto.creditWidth.W))
    val tlECredit = Output(UInt(proto.creditWidth.W))
    val reservedCmd = Output(UInt(proto.reservedCmdWidth.W))
}

// Header 1 carries address only of 64 bits
class UCIHeader1Format(val tl: TileLinkParams) extends Bundle {
    val address = Output(UInt(tl.addressWidth.W))
}

// Header 2 carries other TL messgae stats of 61 bits
class UCIHeader2Format(val tl: TileLinkParams) extends Bundle {
    val opcode = Output(UInt(tl.opcodeWidth.W))
    val param = Output(UInt(tl.paramWidth.W))
    val size = Output(UInt(tl.sizeWidth.W))
    val source = Output(UInt(tl.sourceIDWidth.W))
    val sink = Output(UInt(tl.sinkIDWidth.W))
    val mask = Output(UInt(tl.maskWidth.W))
    val reservedh2 = Output(UInt(tl.reservedH2Width.W))
    // val denied = Output(UInt(tl.deniedWidth.W))
    // val corrupt = Output(UInt(tl.corruptWidth.W))
}

/** UCIe raw 64B format defined as a bundle. The user-defined packet fornat is:
  * CMD: command header consisting of details about the packets (32/64 bits)
  * Header1: Lower word of the TL header (address bits)
  * Header2: Upper word of the TL header (other TL config bits)
  * Data: data is a vector of UCIe data width, this carries the main data
  * ECC: checksum/ecc code for error check/correction (64 bits) TODO: Implement this
  * DISCLAMER: Make sure these adds up to 512 bits
  */ 
class UCIRawPayloadFormat(val tl: TileLinkParams, val proto: ProtocolLayerParams) extends Bundle {
    val cmd = new UCICmdFormat(proto)
    val header1 = new UCIHeader1Format(tl)
    val header2 = new UCIHeader2Format(tl)
    val data = Output(Vec(proto.ucieFlitSize, UInt(proto.ucieFlitWidth.W)))
    val ecc = Output(UInt(proto.ucieEccWidth.W))
}

// Sideband memory mapped registers

class d2dConfig() extends Bundle {
    // d2d Control Status Registers
    val d2d_cycles_1us = Output(UInt(32.W))
    val d2d_uncorrectable_error_csr = Output(UInt(32.W))
    val d2d_uncorrectable_error_mask_csr = Output(UInt(32.W))
    val d2d_uncorrectable_error_severity_csr = Output(UInt(32.W))
    val d2d_correctable_error_csr = Output(UInt(32.W))
    val d2d_correctable_error_mask_csr = Output(UInt(32.W))
    val d2d_header_log_1_csr = Output(UInt(64.W))
    val d2d_header_log_2_csr = Output(UInt(64.W))
    val d2d_error_and_link_testing_parity_log0 = Output(UInt(64.W))
    val d2d_error_and_link_testing_parity_log1 = Output(UInt(64.W))
    val d2d_error_and_link_testing_parity_log2 = Output(UInt(64.W))
    val d2d_error_and_link_testing_parity_log3 = Output(UInt(64.W))
    val advertised_adapter_capability = Output(UInt(64.W))
    val d2d_stack_num = Output(UInt(1.W))
    val d2d_state_can_reset = Output(UInt(1.W))
    val d2d_flush_and_reset = Output(UInt(1.W))
}

class sbConfig() extends Bundle {
    // SideBand Control Status Registers
    val sideband_mailbox_index_low = Input(UInt(32.W))
    val sideband_mailbox_index_high = Input(UInt(32.W))
    val sideband_mailbox_data_low = Input(UInt(32.W))
    val sideband_mailbox_data_high = Input(UInt(32.W))
    val sideband_mailbox_ready = Output(UInt(1.W))
    val sideband_mailbox_valid = Input(UInt(1.W))

    val sideband_mailbox_sw_to_node_index_low = Output(UInt(32.W))
    val sidebank_mailbox_sw_to_node_index_high = Output(UInt(32.W))
    val sideband_mailbox_sw_to_node_data_low = Output(UInt(32.W))
    val sideband_mailbox_sw_to_node_data_high = Output(UInt(32.W))
    val sideband_mailbox_sw_ready = Output(UInt(1.W))
    val sideband_mailbox_sw_valid = Output(UInt(1.W))
}