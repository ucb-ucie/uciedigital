package edu.berkeley.cs.ucie.digital

import org.chipsalliance.cde.config.{Field, Parameters, Config}
import scala.collection.immutable.ListMap
import scala.math.{floor, log10, pow, max}

class ProtoLBTesterConfig(p: ProtoLBTesterParams) extends Config((site, here, up) => {
  case ProtoLBTesterKey => p
})

class ProtoLBTestConfig00 extends ProtoLBTesterConfig(ProtoLBTesterParams())