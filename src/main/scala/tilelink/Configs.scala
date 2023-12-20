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

class WithUCITLAdapter(params: UCITLParams) extends Config((site, here, up) => {
  case UCITLKey => Some(params)
})