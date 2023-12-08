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

import ucie.d2dadapter._

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

  val protocol = Module(new ProtocolLayer())

  val (in, managerEdge) = managerNode.in(0)
  val (out, clientEdge) = clientNode.out(0)


}
