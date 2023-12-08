package edu.berkeley.cs.ucie.digital
package protocol

import chisel3._
import chisel3.util._

import interface._

case class ProtocolParams(width: Int, dllpWidth: Int, sbWidth: Int)

class ProtocolLayer(val d2dParams: D2DAdapterParams, val protoParams: ProtocolParams) extends Module {
    val io = IO(new Bundle{
        val txFlit = Input()
        val rxFlit = Output()
        val fdi = new Fdi(d2dParams)
    })
}