package edu.berkeley.cs.ucie.digital
package sideband
import chisel3._
import chisel3.util._
import chisel3.experimental._
import freechips.rocketchip._
/* Automatically generated by parse_opcodes */
object SBM {
  def MEMR_32 = BitPat(
    "b???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????00000",
  )
  def MEMW_32 = BitPat(
    "b???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????00001",
  )
  def CONFR_32 = BitPat(
    "b???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????00100",
  )
  def CONFW_32 = BitPat(
    "b???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????00101",
  )
  def MEMR_64 = BitPat(
    "b???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????01000",
  )
  def MEMW_64 = BitPat(
    "b???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????01001",
  )
  def CONFR_64 = BitPat(
    "b???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????01100",
  )
  def CONFW_64 = BitPat(
    "b???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????01101",
  )
  def COMP_0 = BitPat(
    "b???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????10000",
  )
  def COMP_32 = BitPat(
    "b???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????10001",
  )
  def COMP_64 = BitPat(
    "b???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????11001",
  )
  def MSG_0 = BitPat(
    "b???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????10010",
  )
  def MSG_64 = BitPat(
    "b???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????11011",
  )
  def NOP_CRD = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00000000??????????00000000?????????10010",
  )
  def LINK_MGMT_RDI_REQ_ACTIVE = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00000001??????????00000001?????????10010",
  )
  def LINK_MGMT_RDI_REQ_L1 = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00000100??????????00000001?????????10010",
  )
  def LINK_MGMT_RDI_REQ_L2 = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00001000??????????00000001?????????10010",
  )
  def LINK_MGMT_RDI_REQ_LINK_RESET = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00001001??????????00000001?????????10010",
  )
  def LINK_MGMT_RDI_REQ_LINK_ERROR = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00001010??????????00000001?????????10010",
  )
  def LINK_MGMT_RDI_REQ_RETRAIN = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00001011??????????00000001?????????10010",
  )
  def LINK_MGMT_RDI_REQ_DISABLE = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00001100??????????00000001?????????10010",
  )
  def LINK_MGMT_RDI_RSP_ACTIVE = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000001??????????00000010?????????10010",
  )
  def LINK_MGMT_RDI_RSP_PM_NAK = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000010??????????00000010?????????10010",
  )
  def LINK_MGMT_RDI_RSP_L1 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000100??????????00000010?????????10010",
  )
  def LINK_MGMT_RDI_RSP_L2 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001000??????????00000010?????????10010",
  )
  def LINK_MGMT_RDI_RSP_LINK_RESET = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001001??????????00000010?????????10010",
  )
  def LINK_MGMT_RDI_RSP_LINK_ERROR = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001010??????????00000010?????????10010",
  )
  def LINK_MGMT_RDI_RSP_RETRAIN = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001011??????????00000010?????????10010",
  )
  def LINK_MGMT_RDI_RSP_DISABLE = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001100??????????00000010?????????10010",
  )
  def LINK_MGMT_ADAPTER0_REQ_ACTIVE = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000001??????????00000011?????????10010",
  )
  def LINK_MGMT_ADAPTER0_REQ_L1 = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00000100??????????00000011?????????10010",
  )
  def LINK_MGMT_ADAPTER0_REQ_L2 = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00001000??????????00000011?????????10010",
  )
  def LINK_MGMT_ADAPTER0_REQ_LINK_RESET = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00001001??????????00000011?????????10010",
  )
  def LINK_MGMT_ADAPTER0_REQ_DISABLE = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00001100??????????00000011?????????10010",
  )
  def LINK_MGMT_ADAPTER0_RSP_ACTIVE = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000001??????????00000100?????????10010",
  )
  def LINK_MGMT_ADAPTER0_RSP_PM_NAK = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000010??????????00000100?????????10010",
  )
  def LINK_MGMT_ADAPTER0_RSP_L1 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000100??????????00000100?????????10010",
  )
  def LINK_MGMT_ADAPTER0_RSP_L2 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001000??????????00000100?????????10010",
  )
  def LINK_MGMT_ADAPTER0_RSP_LINK_RESET = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001001??????????00000100?????????10010",
  )
  def LINK_MGMT_ADAPTER0_RSP_DISABLE = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001100??????????00000100?????????10010",
  )
  def LINK_MGMT_ADAPTER1_REQ_ACTIVE = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000001??????????00000101?????????10010",
  )
  def LINK_MGMT_ADAPTER1_REQ_L1 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000100??????????00000101?????????10010",
  )
  def LINK_MGMT_ADAPTER1_REQ_L2 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001000??????????00000101?????????10010",
  )
  def LINK_MGMT_ADAPTER1_REQ_LINK_RESET = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001001??????????00000101?????????10010",
  )
  def LINK_MGMT_ADAPTER1_REQ_DISABLE = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001100??????????00000101?????????10010",
  )
  def LINK_MGMT_ADAPTER1_RSP_ACTIVE = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000001??????????00000110?????????10010",
  )
  def LINK_MGMT_ADAPTER1_RSP_PM_NAK = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000010??????????00000110?????????10010",
  )
  def LINK_MGMT_ADAPTER1_RSP_L1 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000100??????????00000110?????????10010",
  )
  def LINK_MGMT_ADAPTER1_RSP_L2 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001000??????????00000110?????????10010",
  )
  def LINK_MGMT_ADAPTER1_RSP_LINK_RESET = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001001??????????00000110?????????10010",
  )
  def LINK_MGMT_ADAPTER1_RSP_DISABLE = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001100??????????00000110?????????10010",
  )
  def PARITY_FEATURE_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000000??????????00000111?????????10010",
  )
  def PARITY_FEATURE_ACK = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000000??????????00001000?????????10010",
  )
  def PARITY_FEATURE_NAK = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000001??????????00001000?????????10010",
  )
  def ERRMSG_CORRECTABLE = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00000000??????????00001001?????????10010",
  )
  def ERRMSG_NON_FATAL = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00000001??????????00001001?????????10010",
  )
  def ERRMSG_FATAL = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00000010??????????00001001?????????10010",
  )
  def START_TX_INITIATED_D2C_POINT_TEST_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000001??????????10001010?????????10010",
  )
  def LFSR_CLEAR_ERROR_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000010??????????10000101?????????10010",
  )
  def LFSR_CLEAR_ERROR_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000010??????????10001010?????????10010",
  )
  def TXINIT_D2C_RESULTS_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000011??????????10000101?????????10010",
  )
  def END_TX_INITIATED_D2C_POINT_TEST_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000100??????????10000101?????????10010",
  )
  def END_TX_INITIATED_D2C_POINT_TEST_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000100??????????10001010?????????10010",
  )
  def START_TX_INIT_D2C_EYE_SWEEP_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000101??????????10001010?????????10010",
  )
  def LFSR_CLEAR_ERROR_REQ_2 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000010??????????10000101?????????10010",
  )
  def LFSR_CLEAR_ERROR_RESP_2 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000010??????????10001010?????????10010",
  )
  def TXINIT_D2C_RESULTS_REQ_2 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000011??????????10000101?????????10010",
  )
  def END_TX_INIT_D2C_EYE_SWEEP_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000110??????????10000101?????????10010",
  )
  def END_TX_INIT_D2C_EYE_SWEEP_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000110??????????10001010?????????10010",
  )
  def START_RX_INIT_D2C_POINT_TEST_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000111??????????10001010?????????10010",
  )
  def LFSR_CLEAR_ERROR_REQ_3 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000010??????????10000101?????????10010",
  )
  def LFSR_CLEAR_ERROR_RESP_3 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000010??????????10001010?????????10010",
  )
  def TX_COUNT_DONE_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001000??????????10000101?????????10010",
  )
  def TX_COUNT_DONE_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001000??????????10001010?????????10010",
  )
  def END_RX_INIT_D2C_POINT_TEST_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001001??????????10001010?????????10010",
  )
  def START_RX_INIT_D2C_EYE_SWEEP_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001010??????????10001010?????????10010",
  )
  def LFSR_CLEAR_ERROR_REQ_4 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000010??????????10000101?????????10010",
  )
  def LFSR_CLEAR_ERROR_RESP_4 = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000010??????????10001010?????????10010",
  )
  def RXINIT_D2C_RESULTS_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001011??????????10000101?????????10010",
  )
  def END_RX_INIT_D2C_EYE_SWEEP_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001101??????????10000101?????????10010",
  )
  def END_RX_INIT_D2C_EYE_SWEEP_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001101??????????10001010?????????10010",
  )
  def SBINIT_OUT_OF_RESET = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000000??????????10010001?????????00000",
  )
  def SBINIT_DONE_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000001??????????10010101?????????10010",
  )
  def SBINIT_DONE_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000001??????????10011010?????????10010",
  )
  def MBINIT_CAL_DONE_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000010??????????10100101?????????10010",
  )
  def MBINIT_CAL_DONE_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000010??????????10101010?????????10010",
  )
  def MBINIT_REPAIRCLK_INIT_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000011??????????10100101?????????10010",
  )
  def MBINIT_REPAIRCLK_INIT_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000011??????????10101010?????????10010",
  )
  def MBINIT_REPAIRCLK_RESULT_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000100??????????10100101?????????10010",
  )
  def MBINIT_REPAIRCLK_APPLY_REPAIR_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000101??????????10101010?????????10010",
  )
  def MBINIT_REPAIRCLK_CHECK_REPAIR_INIT_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000110??????????10100101?????????10010",
  )
  def MBINIT_REPAIRCLK_CHECK_REPAIR_INIT_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000110??????????10101010?????????10010",
  )
  def MBINIT_REPAIRCLK_CHECK_RESULTS_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000111??????????10100101?????????10010",
  )
  def MBINIT_REPAIRCLK_DONE_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001000??????????10100101?????????10010",
  )
  def MBINIT_REPAIRCLK_DONE_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001000??????????10101010?????????10010",
  )
  def MBINIT_REPAIRVAL_INIT_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001001??????????10100101?????????10010",
  )
  def MBINIT_REPAIRVAL_INIT_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001001??????????10101010?????????10010",
  )
  def MBINIT_REPAIRVAL_RESULT_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001010??????????10100101?????????10010",
  )
  def MBINIT_REPAIRVAL_APPLY_REPAIR_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001011??????????10101010?????????10010",
  )
  def MBINIT_REPAIRVAL_DONE_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001100??????????10100101?????????10010",
  )
  def MBINIT_REPAIRVAL_DONE_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001100??????????10101010?????????10010",
  )
  def MBINIT_REVERSALMB_INIT_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001101??????????10100101?????????10010",
  )
  def MBINIT_REVERSALMB_INIT_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001101??????????10101010?????????10010",
  )
  def MBINIT_REVERSALMB_CLEAR_ERROR_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001110??????????10100101?????????10010",
  )
  def MBINIT_REVERSALMB_CLEAR_ERROR_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001110??????????10101010?????????10010",
  )
  def MBINIT_REVERSALMB_RESULT_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000001111??????????10100101?????????10010",
  )
  def MBINIT_REVERSALMB_RESULT_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????10101010??????????00001111?????????11011",
  )
  def MBINIT_REVERSALMB_DONE_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000010000??????????10100101?????????10010",
  )
  def MBINIT_RVERSALMB_DONE_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000010000??????????10101010?????????10010",
  )
  def MBINIT_REPAIRMB_START_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000010001??????????10100101?????????10010",
  )
  def MBINIT_REPAIRMB_START_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000010001??????????10101010?????????10010",
  )
  def MBINIT_REPAIRMB_APPLY_REPAIR_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000010010??????????10101010?????????10010",
  )
  def MBINIT_REPAIRMB_END_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000010011??????????10100101?????????10010",
  )
  def MBINIT_REPAIRMB_END_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000010011??????????10101010?????????10010",
  )
  def MBINIT_PARAM_CONFIG_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000010100101??????????00000000?????????11011",
  )
  def MBINIT_PARAM_CONFIG_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000010101010??????????00000000?????????11011",
  )
  def MBTRAIN_VALVREF_START_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00000000??????????10110101?????????00000",
  )
  def MBTRAIN_VALVREF_START_RESP = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00000000??????????10111010?????????00000",
  )
  def MBTRAIN_VALVREF_END_REQ = BitPat(
    "b????????????????????????????????????????????????????????????????????????????????????????00000001??????????10110101?????????00000",
  )
  def ADV_CAP = BitPat(
    "b????????????????????????????????????????????????????????????????????????000000000000000000000000??????????00000001?????????11011",
  )

  def isComplete(x: UInt) = x === COMP_0 | x === COMP_32 | x === COMP_64
  def isMessage(x: UInt) = x === MSG_0 | x === MSG_64
  def isRequest(x: UInt) = ~x(4)
}

// A factory function to create a message from a bitpat, source, destination, and data
object SBMessage_factory {
  def apply(
      base: BitPat,
      src: String,
      remote: Boolean = false,
      dst: String,
      data: UInt = 0.U(64.W),
      msgInfo: UInt = 0.U(16.W),
  ): UInt = {
    // take the bottom 64 bits of the base by modulo
    var msg: BigInt = base.value % (BigInt(1) << 64)
    val src_num: BigInt = src match {
      case "Protocol_0" => 0
      case "Protocol_1" => 4
      case "D2D"        => 1
      case "PHY"        => 2
      case _            => 0
    }
    var dst_num: BigInt = dst match {
      case "Protocol_0" => 0
      case "Protocol_1" => 4
      case "D2D"        => 1
      case "PHY"        => 2
      case _            => 0
    }
    dst_num += (if (remote) 4 else 0)
    msg += src_num << 29
    dst_num = dst_num << 30
    dst_num = dst_num << 26
    msg += dst_num
    println("SBMessage_factory: " + msg)
    val new_msg = Cat(data, (msg.U(64.W) | (msgInfo << (32 + 8).U)))
    println("SBMessage_factory: " + new_msg)
    new_msg
  }
  def apply(
      base: BitPat,
      src: String,
      remote: Boolean,
      dst: String,
      data: Int,
      msgInfo: Int,
  ): BigInt = {
    // take the bottom 64 bits of the base by modulo
    var msg: BigInt = base.value % (BigInt(1) << 64)
    val src_num: BigInt = src match {
      case "Protocol_0" => 0
      case "Protocol_1" => 4
      case "D2D"        => 1
      case "PHY"        => 2
      case _            => 0
    }
    var dst_num: BigInt = dst match {
      case "Protocol_0" => 0
      case "Protocol_1" => 4
      case "D2D"        => 1
      case "PHY"        => 2
      case _            => 0
    }
    dst_num += (if (remote) 4 else 0)
    msg += src_num << 29
    dst_num = dst_num << 30
    dst_num = dst_num << 26
    msg += dst_num
    println("SBMessage_factory: " + msg)
    msg |= (msgInfo & 0xffff) << (32 + 8)
    val new_msg = (data << 64) | msg
    println("SBMessage_factory: " + new_msg)
    new_msg
  }
}
