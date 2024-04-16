package edu.berkeley.cs.ucie.digital
package tilelink

import chisel3._
import chisel3.util._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import org.chipsalliance.cde.config._
import freechips.rocketchip.util._
import freechips.rocketchip.prci._
import freechips.rocketchip.subsystem._

import protocol._
import interfaces._

// TODO: Sideband messaging
/** Main class to generate manager, client and register nodes on the tilelink diplomacy.
  * These needs to get connected to the chipyard system. The class converts tilelink 
  * packets to UCIe Raw 64B flit. It also instantiates the protocol layer which acts as 
  * an agnostic interface to generate FDI signalling.
  */
class UCITLFront(val tlParams: TileLinkParams, val protoParams: ProtocolLayerParams,
                 val fdiParams: FdiParams)
                (implicit p: Parameters) extends LazyModule {

  val device = new SimpleDevice("ucie-front", Seq("ucie,ucie0"))

  val beatBytes = tlParams.BEAT_BYTES

  // MMIO registers controlled by the sideband module
  // val regNode = LazyModule(new UCIConfigRF(beatBytes = tlParams.BEAT_BYTES, address = tlParams.CONFIG_ADDRESS))
  
  // Manager node to send and acquire traffic to partner die
  val managerNode: TLManagerNode = TLManagerNode(Seq(TLSlavePortParameters.v1(
    Seq(TLSlaveParameters.v1(
      address = Seq(AddressSet(tlParams.ADDRESS, tlParams.ADDR_RANGE)),
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
      sourceId = IdRange(0, 4),
      requestFifo = true,
      visibility = Seq(AddressSet(tlParams.ADDRESS, tlParams.ADDR_RANGE))
    )),
    channelBytes = TLChannelBeatBytes(beatBytes))))

  lazy val module = new UCITLFrontImp(this)
}

class UCITLFrontImp(outer: UCITLFront) extends LazyModuleImp(outer) {
  val io = IO(new Bundle {
    val sbus_clk = Input(Clock()) // System bus clock
    val sbus_reset = Input(Bool()) // System bus reset
    val lclk = Input(Clock()) // lclk is the FDI signalling clock
    val lreset = Input(Bool()) // should the UCIe modules have its own reset?
    val fdi = new Fdi(outer.fdiParams)
  })

  // Instantiate the agnostic protocol layer
  val protocol = Module(new ProtocolLayer(outer.fdiParams))
  io.fdi <> protocol.io.fdi

  val (in, managerEdge) = outer.managerNode.in(0)
  val (out, clientEdge) = outer.clientNode.out(0)

  // Async queue to handle the clock crossing between system bus and UCIe stack clock
  val inward = Module(new AsyncQueue(new TLBundleAUnionD(outer.tlParams), new AsyncQueueParams(depth = outer.tlParams.inwardQueueDepth, sync = 3, safe = true, narrow = false)))
  inward.io.enq_clock := io.sbus_clk
  inward.io.enq_reset := io.sbus_reset
  inward.io.deq_clock := io.lclk
  inward.io.deq_reset := io.lreset

  // Async queue to handle the clock crossing between UCIe stack clock and system bus
  val outward = Module(new AsyncQueue(new TLBundleAUnionD(outer.tlParams), new AsyncQueueParams(depth = outer.tlParams.outwardQueueDepth, sync = 3, safe = true, narrow = false)))
  outward.io.enq_clock := io.lclk
  outward.io.enq_reset := io.lreset
  outward.io.deq_clock := io.sbus_clk
  outward.io.deq_reset := io.sbus_reset

  // =======================
  // TL TX packets from System to the UCIe stack, push on the inward queue.
  // The TX packets can be A request from manager node or D response from
  // the client node. This needs to be arbitrated to be sent to partner die.
  // =======================
  val txArbiter = Module(new Arbiter(new TLBundleAUnionD(outer.tlParams), 2))
  val txATLPayload = Wire(new TLBundleAUnionD(outer.tlParams))
  val txDTLPayload = Wire(new TLBundleAUnionD(outer.tlParams))
  //val txTLPayload = Wire(new TLBundleAUnionD(outer.tlParams))

  val aHasData = managerEdge.hasData(in.a.bits)
  /*
  in.a.ready = (inward.io.enq.ready & ~protocol.io.fdi.lpStallAck & 
                (protocol.io.fdi.plStateStatus === PhyState.active))
  inward.io.enq.valid := in.a.fire
  */

  // A request to partner die logic
  in.a.ready := (txArbiter.io.in(0).ready & ~protocol.io.fdi.lpStallAck & 
                (protocol.io.fdi.plStateStatus === PhyState.active))
  txArbiter.io.in(0).valid := in.a.fire

  when(in.a.fire && aHasData) { // put tx towards partner die
    when (managerEdge.last(in.a)) { // wait for all the beats to arrive
      txATLPayload.opcode  := in.a.bits.opcode
      txATLPayload.param   := in.a.bits.param
      txATLPayload.size    := in.a.bits.size
      txATLPayload.source  := in.a.bits.source
      txATLPayload.sink    := 0.U
      txATLPayload.address := in.a.bits.address
      txATLPayload.mask    := in.a.bits.mask
      txATLPayload.data    := in.a.bits.data
      // txATLPayload.denied  := false.B
      txATLPayload.corrupt := false.B
    }
  }.elsewhen(in.a.fire && ~aHasData) { // get rx data from partner die
    when (managerEdge.last(in.a)) { // wait for all the beats to arrive
      txATLPayload.opcode  := in.a.bits.opcode
      txATLPayload.param   := in.a.bits.param
      txATLPayload.size    := in.a.bits.size
      txATLPayload.source  := in.a.bits.source
      txATLPayload.sink    := 0.U
      txATLPayload.address := in.a.bits.address
      txATLPayload.mask    := in.a.bits.mask
      txATLPayload.data    := 0.U
      // txATLPayload.denied  := false.B
      txATLPayload.corrupt := false.B      
    }
  }

  txArbiter.io.in(0).bits <> txATLPayload

  // D response to partner die's A request logic
  out.d.ready := (txArbiter.io.in(1).ready & ~protocol.io.fdi.lpStallAck & 
                (protocol.io.fdi.plStateStatus === PhyState.active))
  txArbiter.io.in(1).valid := out.d.fire

  when(out.d.fire) {
    when (managerEdge.last(out.d)) { // wait for all the beats to arrive
      txDTLPayload.opcode  := out.d.bits.opcode
      txDTLPayload.param   := out.d.bits.param
      txDTLPayload.size    := out.d.bits.size
      txDTLPayload.source  := out.d.bits.source
      txDTLPayload.sink    := out.d.bits.sink
      txDTLPayload.address := 0.U
      txDTLPayload.mask    := 0.U
      txDTLPayload.data    := out.d.bits.data
      // txDTLPayload.denied  := false.B
      txDTLPayload.corrupt := false.B
    }
  }

  txArbiter.io.in(1).bits <> txDTLPayload

  // queue the txTLPayload (A or D) on the inward queue from the arbiter
  txArbiter.io.out.ready := inward.io.enq.ready
  inward.io.enq.valid := txArbiter.io.out.valid
  inward.io.enq.bits <> txArbiter.io.out.bits
  //inward.io.enq.bits <> txTLPayload

  // ============== Translated TL packet coming out of the outward queue to the system ========
  // dequeue the rx TL packets and orchestrate on the client/manager node
  val isRequest = TLMessages.isA(outward.io.deq.bits.opcode)
  val isResponse = TLMessages.isD(outward.io.deq.bits.opcode)

  when(outward.io.deq.valid) {
    when(isRequest) {
      outward.io.deq.ready := out.a.ready // if A request send to client node
    }.elsewhen(isResponse) {
      outward.io.deq.ready := in.d.ready // if D response send to manager node
    }.otherwise {
      outward.io.deq.ready := false.B
    }
  }

  val tlBundleParams = new TLBundleParameters(addressBits = outer.tlParams.addressWidth,
                                              dataBits = outer.tlParams.dataWidth,
                                              sourceBits = outer.tlParams.sourceIDWidth,
                                              sinkBits = outer.tlParams.sinkIDWidth,
                                              sizeBits = outer.tlParams.sizeWidth, 
                                              echoFields = Nil, 
                                              requestFields = Nil,
                                              responseFields = Nil,
                                              hasBCE = false)

  // map the dequeued packets to the client and manager nodes
  val reqPacket = Wire(new TLBundleA(tlBundleParams))
  val respPacket = Wire(new TLBundleD(tlBundleParams))

  when(isRequest && outward.io.deq.fire) { // send the request A channel packet to client
    //this doesn't work bc reqPacket is a TLBundleA and outward queue data bits are TLBundleAUnionD
    //workaround: manually unpacking the fields here
    //TODO: is there a better way?
    //TODO: check if reqPacket and resPacket are properly consumed
    // reqPacket <> outward.io.deq.bits
    reqPacket.opcode  := outward.io.deq.bits.opcode
    reqPacket.param   := outward.io.deq.bits.param
    reqPacket.size    := outward.io.deq.bits.size
    reqPacket.source  := outward.io.deq.bits.source
    reqPacket.address := outward.io.deq.bits.address
    reqPacket.mask    := outward.io.deq.bits.mask
    reqPacket.data    := outward.io.deq.bits.data
    reqPacket.corrupt := outward.io.deq.bits.corrupt

    out.a.valid := true.B
  }.elsewhen(isResponse && outward.io.deq.fire) { // send the response D channel packet to manager
    // respPacket <> outward.io.deq.bits
    respPacket.opcode  := outward.io.deq.bits.opcode
    respPacket.param   := outward.io.deq.bits.param
    respPacket.size    := outward.io.deq.bits.size
    respPacket.source  := outward.io.deq.bits.source
    respPacket.sink    := outward.io.deq.bits.sink
    respPacket.data    := outward.io.deq.bits.data
    respPacket.corrupt := outward.io.deq.bits.corrupt

    in.d.valid := true.B
  }

  // ============ Below code should run on the UCIe clock? ==============
  
  // Dequeue the TX TL packets and translate to UCIe flit
  val uciTxPayload = Wire(new UCIRawPayloadFormat(outer.tlParams, outer.protoParams)) // User-defined UCIe flit for streaming

  inward.io.deq.ready := protocol.io.fdi.lpData.ready // if pl_trdy is asserted
  // specs implies that these needs to be asserted at the same time
  protocol.io.TLlpData_valid := inward.io.deq.fire & (~protocol.io.fdi.lpStallAck)
  protocol.io.TLlpData_irdy := inward.io.deq.fire & (~protocol.io.fdi.lpStallAck)
  protocol.io.TLlpData_bits := uciTxPayload.asUInt // assign uciTXPayload to the FDI lp data signal

  // Translation based on the uciPayload formatting from outgoing TL packet
  when(inward.io.deq.fire) {
    // form the cmd header with TL message type
    uciTxPayload.cmd.msgType := UCIProtoMsgTypes.TileLink
    uciTxPayload.cmd.hostID := 0.U
    uciTxPayload.cmd.partnerID := 0.U
    uciTxPayload.cmd.reservedCmd := 0.U
    // header 1
    uciTxPayload.header1.address := inward.io.deq.bits.address
    // header 2
    uciTxPayload.header2.opcode  := inward.io.deq.bits.opcode
    uciTxPayload.header2.param   := inward.io.deq.bits.param
    uciTxPayload.header2.size    := inward.io.deq.bits.size
    uciTxPayload.header2.source  := inward.io.deq.bits.source
    uciTxPayload.header2.sink    := inward.io.deq.bits.sink
    uciTxPayload.header2.mask    := inward.io.deq.bits.mask
    // uciTxPayload.header2.denied  := inward.io.deq.bits.denied
    uciTxPayload.header2.corrupt := inward.io.deq.bits.corrupt
    // data mapping
    // TODO: replace with FP coding
    uciTxPayload.data(0) := inward.io.deq.bits.data(63,0)
    uciTxPayload.data(1) := inward.io.deq.bits.data(127,64)
    uciTxPayload.data(2) := inward.io.deq.bits.data(191,128)
    uciTxPayload.data(3) := inward.io.deq.bits.data(255,192)
    // TODO: add ECC/checksum functionality, for now tieing to 0
    uciTxPayload.ecc := 0.U
  }
 
  // =======================
  // TL RX packets coming from the UCIe stack to the System, push on the outward queue
  // =======================
  val rxTLPayload = Wire(new TLBundleAUnionD(outer.tlParams))

  // protocol.io.fdi.lpData.irdy := outward.io.enq.ready
  val uciRxPayload = Wire(new UCIRawPayloadFormat(outer.tlParams, outer.protoParams)) // User-defined UCIe flit for streaming
  val rx_fire = protocol.io.fdi.lpData.irdy && protocol.io.TLplData_valid

  // map the uciRxPayload and the plData based on the uciPayload formatting
  // map the uciRxPayload to the rxTLPayload TLBundle
  when(rx_fire) {
    // ucie cmd
    val uciCmd = Wire(new UCICmdFormat(outer.protoParams))
    uciCmd.msgType   := UCIProtoMsgTypes(protocol.io.TLplData_bits(1,0))
    uciCmd.hostID    := protocol.io.TLplData_bits(9, 2)
    uciCmd.partnerID := protocol.io.TLplData_bits(17, 10)
    // TODO: CHECK BITS!!!
    uciCmd.reservedCmd := protocol.io.TLplData_bits(31, 18)
    uciRxPayload.cmd := uciCmd
    // uciRxPayload.cmd := protocol.io.TLplData_bits(31,0)
    // ucie header 1
    val uciHeader1 = Wire(new UCIHeader1Format(outer.tlParams))
    uciHeader1.address := protocol.io.TLplData_bits(95,32)
    uciRxPayload.header1 := uciHeader1
    // ucie header 2
    val uciHeader2 = Wire(new UCIHeader1Format(outer.tlParams))
    // TODO: assign bits!
    uciRxPayload.header2 := uciHeader2
    // ucie data payload
    uciRxPayload.data := protocol.io.TLplData_bits(223,160)
    uciRxPayload.data := protocol.io.TLplData_bits(287,224)
    uciRxPayload.data := protocol.io.TLplData_bits(351,288)
    uciRxPayload.data := protocol.io.TLplData_bits(415,352)
    uciRxPayload.data := protocol.io.TLplData_bits(479,416)
    // ucie ecc
    uciRxPayload.ecc := protocol.io.TLplData_bits(511,480)

    // map the uciRxPayload to the rxTLPayload
    rxTLPayload.address := uciRxPayload.header1.address
    
    rxTLPayload.opcode  := uciRxPayload.header2.opcode
    rxTLPayload.param   := uciRxPayload.header2.param
    rxTLPayload.size    := uciRxPayload.header2.size
    rxTLPayload.source  := uciRxPayload.header2.source
    rxTLPayload.sink    := uciRxPayload.header2.sink
    rxTLPayload.mask    := uciRxPayload.header2.mask
    // rxTLPayload.denied  := uciRxPayload.header2.denied
    rxTLPayload.corrupt := uciRxPayload.header2.corrupt
    
    rxTLPayload.data    := uciRxPayload.data
  }

  // Queue the translated RX TL packet to send to the system
  when(rx_fire) {
    outward.io.enq.bits := rxTLPayload
    outward.io.enq.valid := true.B
  }

}
