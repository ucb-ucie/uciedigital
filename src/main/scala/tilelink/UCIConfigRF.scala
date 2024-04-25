package edu.berkeley.cs.ucie.digital
package tilelink

import chisel3._
import freechips.rocketchip.util._
import chisel3.util._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import org.chipsalliance.cde.config.{Field, Config, Parameters}
import freechips.rocketchip.subsystem.{BaseSubsystem, CacheBlockBytes}
import freechips.rocketchip.regmapper.{HasRegMap, RegField}

import protocol._

class UCIConfigRF(val beatBytes: Int, val address: BigInt)(implicit p: Parameters)
    extends LazyModule {
  val device = new SimpleDevice(s"ucie-regs", Nil)
  val node = TLRegisterNode(
    Seq(AddressSet(address, 4096 - 1)),
    device,
    "reg/control",
    beatBytes = beatBytes,
  )

  lazy val module = new Impl
  class Impl extends LazyModuleImp(this) {
    val io = IO(new Bundle{
      val d2d_csrs = new d2dConfig
      val sb_csrs = new sbConfig
    })

    // d2d Control Status Registers
    val d2d_cycles_1us = RegInit(0.U(32.W))
    val d2d_uncorrectable_error_csr = RegInit(0.U(32.W))
    val d2d_uncorrectable_error_mask_csr = RegInit(63.U(32.W))
    val d2d_uncorrectable_error_severity_csr = RegInit(63.U(32.W))
    val d2d_correctable_error_csr = RegInit(0.U(32.W))
    val d2d_correctable_error_mask_csr = RegInit(63.U(32.W))
    val d2d_header_log_1_csr = RegInit(0.U(64.W))
    val d2d_header_log_2_csr = RegInit(0.U(64.W))
    val d2d_error_and_link_testing_parity_log0 = RegInit(0.U(64.W))
    val d2d_error_and_link_testing_parity_log1 = RegInit(0.U(64.W))
    val d2d_error_and_link_testing_parity_log2 = RegInit(0.U(64.W))
    val d2d_error_and_link_testing_parity_log3 = RegInit(0.U(64.W))
    val advertised_adapter_capability = RegInit(0.U(64.W))
    val d2d_stack_num = RegInit(0.U(1.W))
    val d2d_state_can_reset = RegInit(0.U(1.W))
    val d2d_flush_and_reset = RegInit(0.U(1.W))

    // SideBand Control Status Registers
    val sideband_mailbox_index_low = RegInit(0.U(32.W))
    val sideband_mailbox_index_high = RegInit(0.U(32.W))
    val sideband_mailbox_data_low = RegInit(0.U(32.W))
    val sideband_mailbox_data_high = RegInit(0.U(32.W))
    val sideband_mailbox_ready = RegInit(0.U(1.W))
    val sideband_mailbox_valid = RegInit(0.U(1.W))

    val sideband_mailbox_sw_to_node_index_low = RegInit(0.U(32.W))
    val sidebank_mailbox_sw_to_node_index_high = RegInit(0.U(32.W))
    val sideband_mailbox_sw_to_node_data_low = RegInit(0.U(32.W))
    val sideband_mailbox_sw_to_node_data_high = RegInit(0.U(32.W))
    val sideband_mailbox_sw_ready = RegInit(0.U(1.W))
    val sideband_mailbox_sw_valid = RegInit(0.U(1.W))
    
    node.regmap (
      0x00 -> Seq(RegField.r(32, d2d_cycles_1us)),
      0x04 -> Seq(RegField(32, d2d_uncorrectable_error_csr)),
      0x08 -> Seq(RegField(32, d2d_uncorrectable_error_mask_csr)),
      0x0C -> Seq(RegField(32, d2d_uncorrectable_error_severity_csr)),
      0x10 -> Seq(RegField(32, d2d_correctable_error_csr)),
      0x14 -> Seq(RegField(32, d2d_correctable_error_mask_csr)),
      0x18 -> Seq(RegField(64, d2d_header_log_1_csr)),
      0x20 -> Seq(RegField(64, d2d_header_log_2_csr)),
      0x28 -> Seq(RegField(64, d2d_error_and_link_testing_parity_log0)),
      0x30 -> Seq(RegField(64, d2d_error_and_link_testing_parity_log1)),
      0x38 -> Seq(RegField(64, d2d_error_and_link_testing_parity_log2)),
      0x40 -> Seq(RegField(64, d2d_error_and_link_testing_parity_log3)),
      0x48 -> Seq(RegField(64, advertised_adapter_capability)),
      0x50 -> Seq(RegField(32, sideband_mailbox_index_low)),
      0x54 -> Seq(RegField(32, sideband_mailbox_index_high)),
      0x58 -> Seq(RegField(32, sideband_mailbox_data_low)),
      0x5C -> Seq(RegField(32, sideband_mailbox_data_high)),
      0x60 -> Seq(RegField(1, sideband_mailbox_ready)),
      0x64 -> Seq(RegField(1, sideband_mailbox_valid)),
      0x68 -> Seq(RegField(32, sideband_mailbox_sw_to_node_index_low)),
      0x6C -> Seq(RegField(32, sidebank_mailbox_sw_to_node_index_high)),
      0x70 -> Seq(RegField(32, sideband_mailbox_sw_to_node_data_low)),
      0x74 -> Seq(RegField(32, sideband_mailbox_sw_to_node_data_high)),
      0x78 -> Seq(RegField(1, sideband_mailbox_sw_ready)),
      0x79 -> Seq(RegField(1, sideband_mailbox_sw_valid)),
      0x80 -> Seq(RegField(1, d2d_stack_num)),
      0x81 -> Seq(RegField(1, d2d_state_can_reset)),
      0x82 -> Seq(RegField(1, d2d_flush_and_reset))
      )

    io.d2d_csrs.d2d_cycles_1us := d2d_cycles_1us
    io.d2d_csrs.d2d_uncorrectable_error_csr := d2d_uncorrectable_error_csr
    io.d2d_csrs.d2d_uncorrectable_error_mask_csr := d2d_uncorrectable_error_mask_csr 
    io.d2d_csrs.d2d_uncorrectable_error_severity_csr := d2d_uncorrectable_error_severity_csr
    io.d2d_csrs.d2d_correctable_error_csr := d2d_correctable_error_csr
    io.d2d_csrs.d2d_correctable_error_mask_csr := d2d_correctable_error_mask_csr
    io.d2d_csrs.d2d_header_log_1_csr := d2d_header_log_1_csr
    io.d2d_csrs.d2d_header_log_2_csr := d2d_header_log_2_csr
    io.d2d_csrs.d2d_error_and_link_testing_parity_log0 := d2d_error_and_link_testing_parity_log0
    io.d2d_csrs.d2d_error_and_link_testing_parity_log1 := d2d_error_and_link_testing_parity_log1
    io.d2d_csrs.d2d_error_and_link_testing_parity_log2 := d2d_error_and_link_testing_parity_log2
    io.d2d_csrs.d2d_error_and_link_testing_parity_log3 := d2d_error_and_link_testing_parity_log3
    io.d2d_csrs.advertised_adapter_capability := advertised_adapter_capability
    io.d2d_csrs.d2d_stack_num := d2d_stack_num
    io.d2d_csrs.d2d_state_can_reset := d2d_state_can_reset
    io.d2d_csrs.d2d_flush_and_reset := d2d_flush_and_reset

   io.sb_csrs.sideband_mailbox_index_low := sideband_mailbox_index_low
   io.sb_csrs.sideband_mailbox_index_high := sideband_mailbox_index_high
   io.sb_csrs.sideband_mailbox_data_low := sideband_mailbox_data_low
   io.sb_csrs.sideband_mailbox_data_high := sideband_mailbox_data_high
   io.sb_csrs.sideband_mailbox_ready := sideband_mailbox_ready
   io.sb_csrs.sideband_mailbox_valid := sideband_mailbox_valid
   io.sb_csrs.sideband_mailbox_sw_to_node_index_low := sideband_mailbox_sw_to_node_index_low
   io.sb_csrs.sidebank_mailbox_sw_to_node_index_high := sidebank_mailbox_sw_to_node_index_high
   io.sb_csrs.sideband_mailbox_sw_to_node_data_low := sideband_mailbox_sw_to_node_data_low
   io.sb_csrs.sideband_mailbox_sw_to_node_data_high := sideband_mailbox_sw_to_node_data_high
   io.sb_csrs.sideband_mailbox_sw_ready := sideband_mailbox_sw_ready
   io.sb_csrs.sideband_mailbox_sw_valid := sideband_mailbox_sw_valid
  }
}
