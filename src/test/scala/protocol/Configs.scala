package edu.berkeley.cs.ucie.digital
package protocol

import org.chipsalliance.cde.config.{Field, Parameters, Config}
import scala.collection.immutable.ListMap
import scala.math.{floor, log10, pow, max}
import freechips.rocketchip.util.AsyncQueueParams

class ProtoLBTesterConfig(p: ProtoLBTesterParams) extends Config((site, here, up) => {
  case ProtoLBTesterKey => p
})

class ProtoLBTestConfig00 extends ProtoLBTesterConfig(ProtoLBTesterParams(
    protoParams = edu.berkeley.cs.ucie.digital.protocol.ProtocolLayerParams(),
    tlParams    = edu.berkeley.cs.ucie.digital.tilelink.TileLinkParams(address = 0x20000, addressRange = 0xfff, configAddress = 0x4000, inwardQueueDepth = 2, outwardQueueDepth = 2),
    fdiParams   = edu.berkeley.cs.ucie.digital.interfaces.FdiParams(width = 64, dllpWidth = 64, sbWidth = 32),
    rdiParams   = edu.berkeley.cs.ucie.digital.interfaces.RdiParams(width = 64, sbWidth = 32),
    sbParams    = edu.berkeley.cs.ucie.digital.sideband.SidebandParams(),
    myId        = 1,
    linkTrainingParams = edu.berkeley.cs.ucie.digital.logphy.LinkTrainingParams(),
    afeParams   = edu.berkeley.cs.ucie.digital.interfaces.AfeParams(),
    laneAsyncQueueParams = AsyncQueueParams()
))