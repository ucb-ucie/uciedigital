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
import freechips.rocketchip.util._

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
  val clientNode: TLClientNode = TLClientNode(Seq(TLMasterPortParameters.v1(
    Seq(TLMasterParameters.v1(
      name = "ucie-client",
      sourceId = IdRange(0, 16),
      requestFifo = true,
      visibility = Seq(AddressSet(tlParams.ADDRESS, tlParams.ADDR_RANGE))
    )))))

  lazy val module = new UCITLFrontImp(this)
}

class UCITLFrontImp(outer: UCITLFront) extends LazyModuleImp(outer) {
  val io = IO(new Bundle {
    // val sbus_clk = Input(Clock()) // System bus clock
    // val sbus_reset = Input(Bool()) // System bus reset
    // val lclk = Input(Clock()) // lclk is the FDI signalling clock
    // val lreset = Input(Bool()) // should the UCIe modules have its own reset?
    val fdi = new Fdi(outer.fdiParams)
  })

  // Instantiate the agnostic protocol layer
  val protocol = Module(new ProtocolLayer(outer.fdiParams))
  io.fdi <> protocol.io.fdi

  val tlBundleParams = new TLBundleParameters(addressBits = outer.tlParams.addressWidth,
                                            dataBits = outer.tlParams.dataWidth,
                                            sourceBits = outer.tlParams.sourceIDWidth,
                                            sinkBits = outer.tlParams.sinkIDWidth,
                                            sizeBits = outer.tlParams.sizeWidth, 
                                            echoFields = Nil, 
                                            requestFields = Nil,
                                            responseFields = Nil,
                                            hasBCE = false)

  val client_tl = outer.clientNode.out(0)._1
  val client_edge = outer.clientNode.out(0)._2
  val manager_tl = outer.managerNode.in(0)._1
  val manager_edge = outer.managerNode.in(0)._2

  val clientParams = client_edge.bundle
  val managerParams = manager_edge.bundle
  val mergedParams = clientParams.union(managerParams).union(tlBundleParams)
  require(mergedParams.echoFields.isEmpty, "UCIe does not support TileLink with echo fields")
  require(mergedParams.requestFields.isEmpty, "UCIe does not support TileLink with request fields")
  require(mergedParams.responseFields.isEmpty, "UCIe does not support TileLink with response fields")
  require(mergedParams == tlBundleParams, s"UCIe is misconfigured, the combined inwards/outwards parameters cannot be serialized using the provided bundle params\n$mergedParams > $tlBundleParams")

  // Async queue to handle the clock crossing between system bus and UCIe stack clock
  val inwardA = Module(new Queue((new TLBundleA(mergedParams)), 2, pipe=true, flow=true))
  val inwardD = Module(new Queue((new TLBundleD(mergedParams)), 2, pipe=true, flow=true))
  //val inward = Module(new AsyncQueue(new TLBundleAUnionD(outer.tlParams), new AsyncQueueParams(depth = outer.tlParams.inwardQueueDepth, sync = 3, safe = true, narrow = false)))
  // inward.io.enq_clock := io.sbus_clk
  // inward.io.enq_reset := io.sbus_reset
  // inward.io.deq_clock := io.lclk
  // inward.io.deq_reset := io.lreset

  // Async queue to handle the clock crossing between UCIe stack clock and system bus
  //val outwardA = Module(new Queue((new TLBundleA(mergedParams)), 2, pipe=true, flow=true))
  //val outwardD = Module(new Queue((new TLBundleD(mergedParams)), 2, pipe=true, flow=true))
  val outwardA = Module(new CreditedToDecoupledMsg(new TLBundleA(mergedParams), 16, 4))
  val outwardD = Module(new CreditedToDecoupledMsg(new TLBundleD(mergedParams), 16, 4))
  //val outward = Module(new AsyncQueue(new TLBundleAUnionD(outer.tlParams), new AsyncQueueParams(depth = outer.tlParams.outwardQueueDepth, sync = 3, safe = true, narrow = false)))
  // outward.io.enq_clock := io.lclk
  // outward.io.enq_reset := io.lreset
  // outward.io.deq_clock := io.sbus_clk
  // outward.io.deq_reset := io.sbus_reset

  // =======================
  // TL TX packets from System to the UCIe stack, push on the inward queue.
  // The TX packets can be A request from manager node or D response from
  // the client node. This needs to be arbitrated to be sent to partner die.
  // =======================
  val txArbiter = Module(new Arbiter(new TLBundleAUnionD(outer.tlParams), 2))
  val txATLPayload = Wire(new TLBundleAUnionD(outer.tlParams))
  val txDTLPayload = Wire(new TLBundleAUnionD(outer.tlParams))
  //val txTLPayload = Wire(new TLBundleAUnionD(outer.tlParams))

  val aHasData = manager_edge.hasData(manager_tl.a.bits)
  val rx_fire = protocol.io.TLplData_valid
  val uciRxPayload = Wire(new UCIRawPayloadFormat(outer.tlParams, outer.protoParams)) // User-defined UCIe flit for streaming
  val uciTxPayload = Wire(new UCIRawPayloadFormat(outer.tlParams, outer.protoParams)) // User-defined UCIe flit for streaming

  // defaults
  uciRxPayload := 0.U.asTypeOf(new UCIRawPayloadFormat(outer.tlParams, outer.protoParams))
  uciTxPayload := 0.U.asTypeOf(new UCIRawPayloadFormat(outer.tlParams, outer.protoParams))
  txATLPayload := 0.U.asTypeOf(new TLBundleAUnionD(outer.tlParams))
  txDTLPayload := 0.U.asTypeOf(new TLBundleAUnionD(outer.tlParams))

  /*
  manager_tl.a.ready = (inward.io.enq.ready & ~protocol.io.fdi.lpStallAck & 
                (protocol.io.fdi.plStateStatus === PhyState.active))
  inward.io.enq.valid := manager_tl.a.fire
  */

  // A request to partner die logic
  // enqueue on the A channel queue
  manager_tl.a.ready := (inwardA.io.enq.ready & ~protocol.io.fdi.lpStallAck & 
                (protocol.io.fdi.plStateStatus === PhyState.active))
  inwardA.io.enq.valid := manager_tl.a.fire

  // when(manager_tl.a.fire && aHasData) { // put tx towards partner die
  //   when (manager_edge.last(manager_tl.a)) { // wait for all the beats to arrive
  //     inwardA.io.enq.bits.opcode  := manager_tl.a.bits.opcode
  //     inwardA.io.enq.bits.param   := manager_tl.a.bits.param
  //     inwardA.io.enq.bits.size    := manager_tl.a.bits.size
  //     inwardA.io.enq.bits.source  := manager_tl.a.bits.source
  //     //inwardA.io.enq.bits.sink    := 0.U
  //     inwardA.io.enq.bits.address := manager_tl.a.bits.address
  //     inwardA.io.enq.bits.mask    := manager_tl.a.bits.mask
  //     inwardA.io.enq.bits.data    := manager_tl.a.bits.data
  //     //inwardA.io.enq.bits.msgType := UCIProtoMsgTypes.TLA
  //     // inwardA.io.enq.bits.denied  := false.B
  //     // inwardA.io.enq.bits.corrupt := false.B
  //   }
  // }.elsewhen(manager_tl.a.fire && ~aHasData) { // get rx data from partner die
  //   when (manager_edge.last(manager_tl.a)) { // wait for all the beats to arrive
  //     inwardA.io.enq.bits.opcode  := manager_tl.a.bits.opcode
  //     inwardA.io.enq.bits.param   := manager_tl.a.bits.param
  //     inwardA.io.enq.bits.size    := manager_tl.a.bits.size
  //     inwardA.io.enq.bits.source  := manager_tl.a.bits.source
  //     //inwardA.io.enq.bits.sink    := 0.U
  //     inwardA.io.enq.bits.address := manager_tl.a.bits.address
  //     inwardA.io.enq.bits.mask    := manager_tl.a.bits.mask
  //     inwardA.io.enq.bits.data    := 0.U
  //     //inwardA.io.enq.bits.msgType := UCIProtoMsgTypes.TLA
  //     // inwardA.io.enq.bits.denied  := false.B
  //     // inwardA.io.enq.bits.corrupt := false.B      
  //   }
  // }
  //inwardA.io.enq.bits <> txATLPayload.asTypeOf(new TLBundleA(mergedParams))
  inwardA.io.enq.bits <> manager_tl.a.bits

  val creditedMsgA = Module(new DecoupledtoCreditedMsg(new TLBundleA(mergedParams), 16, 4))
  inwardA.io.deq.ready := creditedMsgA.io.in.ready
  creditedMsgA.io.in.valid := inwardA.io.deq.fire
  creditedMsgA.io.in.bits := inwardA.io.deq.bits
  creditedMsgA.io.credit.valid := rx_fire
  creditedMsgA.io.credit.bits := uciRxPayload.cmd.tlACredit

  // D response to partner die's A request logic
  client_tl.d.ready := (inwardD.io.enq.ready & ~protocol.io.fdi.lpStallAck & 
                (protocol.io.fdi.plStateStatus === PhyState.active))
  inwardD.io.enq.valid := client_tl.d.fire

  // when(client_tl.d.fire) {
  //   when (client_edge.last(client_tl.d)) { // wait for all the beats to arrive
  //     inwardD.io.enq.bits.opcode  := client_tl.d.bits.opcode
  //     inwardD.io.enq.bits.param   := client_tl.d.bits.param
  //     inwardD.io.enq.bits.size    := client_tl.d.bits.size
  //     inwardD.io.enq.bits.source  := client_tl.d.bits.source
  //     inwardD.io.enq.bits.sink    := client_tl.d.bits.sink
  //     //inwardD.io.enq.bits.address := 0.U
  //     //inwardD.io.enq.bits.mask    := 0.U
  //     inwardD.io.enq.bits.data    := client_tl.d.bits.data
  //     //inwardD.io.enq.bits.msgType := UCIProtoMsgTypes.TLD
  //     // inwardD.io.enq.bits.denied  := false.B
  //     // inwardD.io.enq.bits.corrupt := false.B
  //   }
  // }
  //inwardD.io.enq.bits <> txDTLPayload.asTypeOf(new TLBundleD(mergedParams))
  inwardD.io.enq.bits <> client_tl.d.bits

  val creditedMsgD = Module(new DecoupledtoCreditedMsg(new TLBundleD(mergedParams), 16, 4))
  inwardD.io.deq.ready := creditedMsgD.io.in.ready
  creditedMsgD.io.in.valid := inwardD.io.deq.fire
  creditedMsgD.io.in.bits := inwardD.io.deq.bits
  creditedMsgD.io.credit.valid := rx_fire
  creditedMsgD.io.credit.bits := uciRxPayload.cmd.tlDCredit

  // Arbitrate the A and D channels from the credited msgs
  creditedMsgA.io.out.ready := txArbiter.io.in(0).ready
  txArbiter.io.in(0).valid := creditedMsgA.io.out.fire
  //txArbiter.io.in(0).bits <> creditedMsgA.io.out.bits.asTypeOf(new TLBundleAUnionD(outer.tlParams))
  txArbiter.io.in(0).bits.opcode  := creditedMsgA.io.out.bits.opcode
  txArbiter.io.in(0).bits.param   := creditedMsgA.io.out.bits.param
  txArbiter.io.in(0).bits.size    := creditedMsgA.io.out.bits.size
  txArbiter.io.in(0).bits.source  := creditedMsgA.io.out.bits.source
  txArbiter.io.in(0).bits.sink    := 0.U
  txArbiter.io.in(0).bits.address := creditedMsgA.io.out.bits.address
  txArbiter.io.in(0).bits.mask    := creditedMsgA.io.out.bits.mask
  txArbiter.io.in(0).bits.data    := creditedMsgA.io.out.bits.data
  txArbiter.io.in(0).bits.msgType := UCIProtoMsgTypes.TLA

  creditedMsgD.io.out.ready := txArbiter.io.in(1).ready
  txArbiter.io.in(1).valid := creditedMsgD.io.out.fire
  //txArbiter.io.in(1).bits <> creditedMsgD.io.out.bits.asTypeOf(new TLBundleAUnionD(outer.tlParams))
  txArbiter.io.in(1).bits.opcode  := creditedMsgD.io.out.bits.opcode
  txArbiter.io.in(1).bits.param   := creditedMsgD.io.out.bits.param
  txArbiter.io.in(1).bits.size    := creditedMsgD.io.out.bits.size
  txArbiter.io.in(1).bits.source  := creditedMsgD.io.out.bits.source
  txArbiter.io.in(1).bits.sink    := creditedMsgD.io.out.bits.sink
  txArbiter.io.in(1).bits.address := 0.U
  txArbiter.io.in(1).bits.mask    := 0.U
  txArbiter.io.in(1).bits.data    := creditedMsgD.io.out.bits.data
  txArbiter.io.in(1).bits.msgType := UCIProtoMsgTypes.TLD

  // ============== Translated TL packet coming out of the outward queue to the system ========
  // dequeue the rx TL packets and orchestrate on the client/manager node
  //val isRequest = outwardA.io.deq.bits.msgType
  //val isResponse = outwardD.io.deq.bits.msgType

  outwardA.io.out.ready := client_tl.a.ready // if A request send to client node
  outwardD.io.out.ready := manager_tl.d.ready // if D response send to manager node

  client_tl.a.bits <> outwardA.io.out.bits
  client_tl.a.valid := outwardA.io.out.fire

  manager_tl.d.bits <> outwardD.io.out.bits
  manager_tl.d.valid := outwardD.io.out.fire

  // ============ Below code should run on the UCIe clock? ==============
  
  // Dequeue the TX TL packets and translate to UCIe flit
  txArbiter.io.out.ready := protocol.io.fdi.lpData.ready // if pl_trdy is asserted
  // specs implies that these needs to be asserted at the same time
  protocol.io.TLlpData_valid := txArbiter.io.out.fire & (~protocol.io.fdi.lpStallAck)
  protocol.io.TLlpData_irdy := txArbiter.io.out.fire & (~protocol.io.fdi.lpStallAck)
  protocol.io.TLlpData_bits := uciTxPayload.asUInt // assign uciTXPayload to the FDI lp data signa

  val creditA = (txArbiter.io.out.bits.msgType === UCIProtoMsgTypes.TLA)
  val creditB = (txArbiter.io.out.bits.msgType === UCIProtoMsgTypes.TLB)
  val creditC = (txArbiter.io.out.bits.msgType === UCIProtoMsgTypes.TLC)
  val creditD = (txArbiter.io.out.bits.msgType === UCIProtoMsgTypes.TLD)
  val creditE = (txArbiter.io.out.bits.msgType === UCIProtoMsgTypes.TLE)

  outwardA.io.credit.ready := txArbiter.io.out.fire
  outwardD.io.credit.ready := txArbiter.io.out.fire

  // Translation based on the uciPayload formatting from outgoing TL packet
  when(txArbiter.io.out.fire) {
    // form the cmd header with TL message type
    uciTxPayload.cmd.msgType := txArbiter.io.out.bits.msgType
    uciTxPayload.cmd.hostID := 0.U
    uciTxPayload.cmd.partnerID := 0.U
    uciTxPayload.cmd.tlACredit := outwardA.io.credit.bits & outwardA.io.credit.valid & creditA
    uciTxPayload.cmd.tlBCredit := 0.U //& outwardB.io.credit.valid & creditB
    uciTxPayload.cmd.tlCCredit := 0.U //& outwardC.io.credit.valid & creditC
    uciTxPayload.cmd.tlDCredit := outwardD.io.credit.bits & outwardD.io.credit.valid & creditD
    uciTxPayload.cmd.tlECredit := 0.U //& outwardE.io.credit.valid & creditE
    uciTxPayload.cmd.reservedCmd := 0.U
    // header 1
    uciTxPayload.header1.address := txArbiter.io.out.bits.address
    // header 2
    uciTxPayload.header2.opcode  := txArbiter.io.out.bits.opcode
    uciTxPayload.header2.param   := txArbiter.io.out.bits.param
    uciTxPayload.header2.size    := txArbiter.io.out.bits.size
    uciTxPayload.header2.source  := txArbiter.io.out.bits.source
    uciTxPayload.header2.sink    := txArbiter.io.out.bits.sink
    uciTxPayload.header2.mask    := txArbiter.io.out.bits.mask
    uciTxPayload.header2.reservedh2 := 0.U
    // uciTxPayload.header2.denied  := txArbiter.io.out.bits.denied
    // uciTxPayload.header2.corrupt := txArbiter.io.out.bits.corrupt
    // data mapping
    // TODO: replace with FP coding
    uciTxPayload.data(0) := txArbiter.io.out.bits.data(63,0)
    uciTxPayload.data(1) := txArbiter.io.out.bits.data(127,64)
    uciTxPayload.data(2) := txArbiter.io.out.bits.data(191,128)
    uciTxPayload.data(3) := txArbiter.io.out.bits.data(255,192)
    // TODO: add ECC/checksum functionality, for now tieing to 0
    uciTxPayload.ecc := 0.U
  }
 
  // =======================
  // TL RX packets coming from the UCIe stack to the System, push on the outward queue
  // =======================
  val rxTLPayload = Wire(new TLBundleAUnionD(outer.tlParams))
  rxTLPayload := 0.U.asTypeOf(new TLBundleAUnionD(outer.tlParams))
  dontTouch(rxTLPayload)
  dontTouch(uciRxPayload)
  // protocol.io.fdi.lpData.irdy := outward.io.enq.ready

  // map the uciRxPayload and the plData based on the uciPayload formatting
  // map the uciRxPayload to the rxTLPayload TLBundle
  when(rx_fire) {
    // ucie cmd
    uciRxPayload.cmd := protocol.io.TLplData_bits(511, 448).asTypeOf(new UCICmdFormat(outer.protoParams))
    // ucie header 1
    uciRxPayload.header1 := protocol.io.TLplData_bits(447,384).asTypeOf(new UCIHeader1Format(outer.tlParams))
    // ucie header 2
    uciRxPayload.header2 := protocol.io.TLplData_bits(383, 320).asTypeOf(new UCIHeader2Format(outer.tlParams))
    // ucie data payload
    uciRxPayload.data(0) := protocol.io.TLplData_bits(319,256)
    uciRxPayload.data(1) := protocol.io.TLplData_bits(255,192)
    uciRxPayload.data(2) := protocol.io.TLplData_bits(191,128)
    uciRxPayload.data(3) := protocol.io.TLplData_bits(127,64)
    // ucie ecc
    uciRxPayload.ecc := protocol.io.TLplData_bits(63,0)

    // map the uciRxPayload to the rxTLPayload
    rxTLPayload.address := uciRxPayload.header1.address
    rxTLPayload.opcode  := uciRxPayload.header2.opcode
    rxTLPayload.param   := uciRxPayload.header2.param
    rxTLPayload.size    := uciRxPayload.header2.size
    rxTLPayload.source  := uciRxPayload.header2.source
    rxTLPayload.sink    := uciRxPayload.header2.sink
    rxTLPayload.mask    := uciRxPayload.header2.mask
    // rxTLPayload.denied  := uciRxPayload.header2.denied
    // rxTLPayload.corrupt := uciRxPayload.header2.corrupt
    rxTLPayload.data    := Cat(uciRxPayload.data(0), uciRxPayload.data(1), uciRxPayload.data(2), uciRxPayload.data(3))
    rxTLPayload.msgType := uciRxPayload.cmd.msgType
  }

  outwardA.io.in.bits.address := rxTLPayload.address
  outwardA.io.in.bits.opcode  := rxTLPayload.opcode
  outwardA.io.in.bits.param   := rxTLPayload.param
  outwardA.io.in.bits.size    := rxTLPayload.size
  outwardA.io.in.bits.source  := rxTLPayload.source
  outwardA.io.in.bits.mask    := rxTLPayload.mask
  outwardA.io.in.bits.data    := rxTLPayload.data
  outwardA.io.in.bits.corrupt := false.B
  outwardA.io.in.valid        := false.B

  outwardD.io.in.bits.opcode  := rxTLPayload.opcode
  outwardD.io.in.bits.param   := rxTLPayload.param
  outwardD.io.in.bits.size    := rxTLPayload.size
  outwardD.io.in.bits.source  := rxTLPayload.source
  outwardD.io.in.bits.sink    := rxTLPayload.sink
  outwardD.io.in.bits.data    := rxTLPayload.data
  outwardD.io.in.bits.denied  := false.B
  outwardD.io.in.bits.corrupt := false.B
  outwardD.io.in.valid        := false.B

  // Queue the translated RX TL packet to send to the system
  when(rx_fire) {
    when(uciRxPayload.cmd.msgType === UCIProtoMsgTypes.TLA && outwardA.io.in.ready) {
      outwardA.io.in.valid        := true.B
    }.elsewhen(uciRxPayload.cmd.msgType === UCIProtoMsgTypes.TLD && outwardD.io.in.ready ) {
      outwardD.io.in.valid        := true.B
    }
  }
}