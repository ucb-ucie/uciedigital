package edu.berkeley.cs.ucie.digital
package e2e

import chisel3._
import chisel3.util._

import interfaces._
import sideband._
import protocol._
import d2dadapter._
import logphy._
import freechips.rocketchip.util.AsyncQueueParams

/**
  * UCITop is the main class which instantiates all the three
  * layers of the UCIe protocol stack
  * 
  */
class UCITop(val fdiParams: FdiParams, val rdiParams: RdiParams,
             val sbParams: SidebandParams, val myId: BigInt,
             val linkTrainingParams: LinkTrainingParams,
             val afeParams: AfeParams, 
             val laneAsyncQueueParams: AsyncQueueParams) extends Module {
    val io = IO(new Bundle{
        // IOs for connecting to the protocol layer
        val fdi = new Fdi(fdiParams)
        val TLlpData_valid = Input(Bool())
        val TLlpData_bits = Input(Bits((8 * fdiParams.width).W)) 
        val TLlpData_irdy = Input(Bool())
        val TLplData_bits = Output(Bits((8 * fdiParams.width).W))
        val TLplData_valid = Output(Bool())
        val TLready_to_rcv = Input(Bool())
        val fault = Input(Bool())
        val soft_reset = Input(Bool())
        // IOs for connecting to the AFE
        val mbAfe = new MainbandAfeIo(afeParams)
        val sbAfe = new SidebandAfeIo(afeParams)
    })

  // Instantiate the agnostic protocol layer
  val protocol = Module(new ProtocolLayer(fdiParams))
  // Instantiate the D2D adapter
  val d2dadapter = Module(new D2DAdapter(fdiParams, rdiParams, sbParams))
  // Instantiate the logPhy
  val logPhy = Module(new LogicalPhy(myId, linkTrainingParams, afeParams, rdiParams, fdiParams, sbParams, laneAsyncQueueParams))

  // Connect the protocol IOs to the top for connections to the tilelink interface
  io.fdi <> protocol.io.fdi
  protocol.io.TLlpData_valid := io.TLlpData_valid
  protocol.io.TLlpData_bits := io.TLlpData_bits
  protocol.io.TLlpData_irdy := io.TLlpData_irdy
  io.TLplData_bits := protocol.io.TLplData_bits
  io.TLplData_valid := protocol.io.TLplData_valid
  protocol.io.TLready_to_rcv := io.TLready_to_rcv
  protocol.io.fault := io.fault
  protocol.io.soft_reset := io.soft_reset

  // Connect the FDI interface of Protocol layer to D2D adapter
  protocol.io.fdi <> d2dadapter.io.fdi

  // Connect the RDI interface of D2D adapter to logPhy
  d2dadapter.io.rdi <> logPhy.io.rdi

  // Connect the AFE interface from logPhy to the top
  io.mbAfe <> logPhy.io.mbAfe
  io.sbAfe <> logPhy.io.sbAfe
}