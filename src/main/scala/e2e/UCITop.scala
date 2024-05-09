package edu.berkeley.cs.ucie.digital
package e2e

import chisel3._
import chisel3.util._

import afe._
import interfaces._
import sideband._
import protocol._
import d2dadapter._
import logphy._
import freechips.rocketchip.util.AsyncQueueParams

/** UCITop is the main class which instantiates all the three layers of the UCIe
  * protocol stack
  */
class UCITop(
    val fdiParams: FdiParams,
    val rdiParams: RdiParams,
    val sbParams: SidebandParams,
    val linkTrainingParams: LinkTrainingParams,
    val afeParams: AfeParams,
    val laneAsyncQueueParams: AsyncQueueParams,
) extends Module {
  val io = IO(new Bundle {
    // IOs for connecting to the protocol layer
    // val fdi = new Fdi(fdiParams)
    val fdi_lpConfig = Valid(Bits(fdiParams.sbWidth.W))
    val fdi_lpConfigCredit = Input(Bool())
    val fdi_plConfig = Flipped(Valid(Bits(fdiParams.sbWidth.W)))
    val fdi_plConfigCredit = Output(Bool())
    val fdi_lpStallAck = Output(Bool())
    val TLplStateStatus = Output(PhyState())

    val TLlpData_valid = Input(Bool())
    val TLlpData_bits = Input(Bits((8 * fdiParams.width).W))
    val TLlpData_irdy = Input(Bool())
    val TLlpData_ready = Output(Bool())
    val TLplData_bits = Output(Bits((8 * fdiParams.width).W))
    val TLplData_valid = Output(Bool())
    val TLready_to_rcv = Input(Bool())
    val fault = Input(Bool())
    val soft_reset = Input(Bool())
    // IOs for connecting to the AFE
    // val mbAfe = new MainbandAfeIo(afeParams)
    val mbAfe_tx = Output(new MainbandIo(afeParams.mbLanes))
    val mbAfe_rx = Input(new MainbandIo(afeParams.mbLanes))
    val sbAfe = new SidebandAfeIo(afeParams)
  })

  // Instantiate the agnostic protocol layer
  val protocol = Module(new ProtocolLayer(fdiParams))
  // Instantiate the D2D adapter
  val d2dadapter = Module(new D2DAdapter(fdiParams, rdiParams, sbParams))
  // Instantiate the logPhy
  val logPhy = Module(
    new LogicalPhy(
      linkTrainingParams,
      afeParams,
      rdiParams,
      fdiParams,
      sbParams,
      laneAsyncQueueParams,
    ),
  )

  val dafe = Module(new MbAfe(afeParams, AsyncQueueParams()))

  // Connect the FDI interface of Protocol layer to D2D adapter
  protocol.io.fdi <> d2dadapter.io.fdi

  // Connect the RDI interface of D2D adapter to logPhy
  d2dadapter.io.rdi <> logPhy.io.rdi

  // Connect the AFE interface from logPhy to the top
  // io.mbAfe <> logPhy.io.mbAfe
  // logphy.io.mbAfe.
  logPhy.io.mbAfe.txData <> dafe.io.mbAfeIo.rxData
  logPhy.io.mbAfe.rxData <> dafe.io.mbAfeIo.txData
  // dafe.io.
  io.mbAfe_tx <> dafe.io.stdIo.tx.mainband
  io.mbAfe_rx <> dafe.io.stdIo.rx.mainband
  // io.mbAfe <> dafe.io.stdIo.tx.mainband
  io.sbAfe <> logPhy.io.sbAfe

  dafe.io.sbAfeIo.fifoParams.clk := clock
  dafe.io.sbAfeIo.rxData := 0.U
  dafe.io.mbAfeIo.fifoParams.clk := clock
  dafe.io.stdIo.rx.sideband.clk := clock
  dafe.io.mbAfeIo.fifoParams.reset := false.B
  logPhy.io.mbAfe.fifoParams.clk := clock
  dafe.io.sbAfeIo.rxClock := false.B
  logPhy.io.mbAfe.pllLock := true.B
  dafe.io.mbAfeIo.pllLock := true.B
  dafe.io.stdIo.rx.sideband.data := 0.U
  dafe.io.sbAfeIo.pllLock := false.B
  dafe.io.sbAfeIo.fifoParams.reset := false.B
  logPhy.io.mbAfe.fifoParams.reset := false.B

  /* Connect the protocol IOs to the top for connections to the tilelink
   * interface */
  // io.fdi <> protocol.io.fdi
  io.fdi_lpConfig <> protocol.io.fdi.lpConfig
  io.fdi_lpConfigCredit <> protocol.io.fdi.lpConfigCredit
  io.fdi_plConfig <> protocol.io.fdi.plConfig
  io.fdi_plConfigCredit <> protocol.io.fdi.plConfigCredit
  io.fdi_lpStallAck <> protocol.io.fdi.lpStallAck
  io.TLplStateStatus <> protocol.io.TLplStateStatus

  protocol.io.TLlpData_valid := io.TLlpData_valid
  protocol.io.TLlpData_bits := io.TLlpData_bits
  protocol.io.TLlpData_irdy := io.TLlpData_irdy
  io.TLlpData_ready := protocol.io.TLlpData_ready
  io.TLplData_bits := protocol.io.TLplData_bits
  io.TLplData_valid := protocol.io.TLplData_valid
  protocol.io.TLready_to_rcv := io.TLready_to_rcv
  protocol.io.fault := io.fault
  protocol.io.soft_reset := io.soft_reset

}
