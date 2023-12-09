package edu.berkeley.cs.ucie.digital
package tilelink

import chisel3._
import chisel3.util._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import org.chipsalliance.cde.config.Parameters
import freechips.rocketchip.util._
import freechips.rocketchip.prci._
import freechips.rocketchip.subsystem._

import interfaces._

/** Main class to generate manager, client and register nodes on the tilelink diplomacy.
  * These needs to get connected to the chipyard system. The class converts tilelink 
  * packets to UCIe Raw 64B flit. It also instantiates the protocol layer which acts as 
  * an agnostic interface to generate FDI signalling.
  */
class UCITLFront(val tlParams: TileLinkParams)(implicit p: Parameters) extends LazyModule {

  val device = new SimpleDevice("ucie-front", Seq("ucie,ucie0"))

  val beatBytes = tlParams.beatBytes

  // MMIO registers controlled by the sideband module
  val regNode = LazyModule(new UCIConfig(beatBytes = tlParams.beatBytes, address = tlParams.configAddress))
  
  // Manager node to send and acquire traffic to partner die
  val managerNode: TLManagerNode = TLManagerNode(Seq(TLSlavePortParameters.v1(
    Seq(TLSlaveParameters.v1(
      address = Seq(AddressSet(tlParams.baseAddress, tlParams.addressRange)),
      resources = device.reg,
      regionType = RegionType.UNCACHED, // Should be changed to CACHED eventually
      executable = true,
      supportsGet = TransferSizes(1, beatBytes),
      supportsPutFull = TransferSizes(1, beatBytes),
      supportsPutPartial = TransferSizes(1, beatBytes),
      fifoId = Some(0))),
    beatBytes = beatBytes)))

  // Client node to reply to send and acquire traffic from partner die
  val clientNode: TLClientNode = TLClientNode(Seq(TLMasterPortParameters.v2(
    Seq(TLMasterParameters.v1(
      name = "ucie-client",
      sourceId = IdRange(0, 1),
      requestFifo = false,
      visibility = Seq(AddressSet(tlParams.baseAddress, tlParams.addressRange)),
      supportsGet = TransferSizes(1, beatBytes),
      supportsPutFull = TransferSizes(1, beatBytes),
      supportsPutPartial = TransferSizes(1, beatBytes),
    )),
    channelBytes = TLChannelBeatBytes(beatBytes))))

  lazy val module = UCITLFrontImp(this)
}

class UCITLFrontImp(outer: UCITLFront) extends LazyModuleImp(outer) {
  val io = IO(new Bundle {
    val sbus_clk = Input(Clock()) // System bus clock
    val sbus_reset = Input(Bool()) // System bus reset
    val lclk = Input(Clock()) // lclk is the FDI signalling clock
    val lreset = Input(Bool()) // should the UCIe modules have its own reset?
  })

  // Instantiate the agnostic protocol layer
  val protocol = Module(new ProtocolLayer())

  val (in, managerEdge) = managerNode.in(0)
  val (out, clientEdge) = clientNode.out(0)

  // Async queue to handle the clock crossing between system bus and UCIe stack clock
  val inward = Module(new AsyncQueue(new TLBundleAUnionD(params), new AsyncQueueParams(depth = params.inwardQueueDepth, sync = 3, safe = true, narrow = false)))
  inward.io.enq_clock := io.sbus_clk
  inward.io.enq_reset := io.sbus_reset
  inward.io.deq_clock := io.lclk
  inward.io.deq_reset := io.lreset

  // Async queue to handle the clock crossing between UCIe stack clock and system bus
  val outward = Module(new AsyncQueue(new TLBundleAUnionD(params), new AsyncQueueParams(depth = params.outwardQueueDepth, sync = 3, safe = true, narrow = false)))
  outward.io.enq_clock := io.lclk
  outward.io.enq_reset := io.lreset
  outward.io.deq_clock := io.sbus_clk
  outward.io.deq_reset := io.sbus_reset

  // =======================
  // TL packets from System to the UCIe stack, push on the inward queue
  // =======================
  val txTLPayload = Wire(new TLBundleAUnionD(tlParams))

  val aHasData = managerEdge.hasData(in.a.bits)
  in.a.ready = (inward.io.enq.ready & ~protocol.io.fdi.lpStallAck & 
                (protocol.io.fdi.plStateStatus === PhyState.active))
  inward.io.enq.valid := in.a.fire

  when(in.a.fire && aHasData) { // put tx towards partner die
    txTLPayload
  }.elsewhen(in.a.fire && ~aHasData) { // get rx data from partner die
    txTLPayload
  }

  // dequeue the TX TL packets and translate to UCIe flit
  val uciTxPayload = Wire(new UCIRawPayloadFormat()) // User-defined UCIe flit for streaming

  inward.io.deq.ready := protocol.io.fdi.lpData.ready // if pl_trdy is asserted
  // specs implies that these needs to be asserted at the same time
  protocol.io.fdi.lpData.valid := inward.io.deq.fire
  protocol.io.fdi.lpData.irdy := inward.io.deq.fire 
  protocol.io.fdi.lpData.bits := uciTxPayload // need to figure out how this would look

  // TODO: update this translation based on the uciPayload formatting
  when(inward.io.deq.fire) {
    uciTxPayload := inward.io.deq.bits
  }

  // =======================
  // TL packets coming from the UCIe stack to the System, push on the outward queue
  // =======================
  val rxTLPayload = Wire(new TLBundleAUnionD(tlParams))

  protocol.io.fdi.lpData.irdy := outward.io.enq.ready
  val uciRxPayload = Wire(new UCIRawPayloadFormat()) // User-defined UCIe flit for streaming
  val rx_fire = protocol.io.fdi.lpData.irdy && protocol.io.fdi.plData.valid

  // TODO: map the uciRxPayload and the plData based on the uciPayload formatting
  // TODO: map the rxTLPayload to the uciRxPayload correctly
  when(rx_fire) {
    uciRxPayload := protocol.io.fdi.plData.bits
    rxTLPayload := uciRxPayload
  }

  // Queue the translated RX TL packet to send to the system
  when(rx_fire) {
    outward.io.enq.bits := rxTLPayload
    outward.io.enq.valid := true.B
  }

  // dequeue the rx TL packets 
  
}
