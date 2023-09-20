package edu.berkeley.cs.ucie.digital

import chisel3._
import chisel3.util._

// The mainband pins exposed by a standard package UCIe module in one direction.
class MainbandIo(lanes: Int = 16) extends Bundle {
  val data = Bits(lanes.W)
  val valid = Bool()
  val track = Bool()
  val clkp = Clock()
  val clkn = Clock()
}

// The sideband pins exposed by a standard package UCIe module in one direction.
class SidebandIo extends Bundle {
  val data = Bool()
  val clk = Clock()
}

// The pins (mainband and sideband) exposed by a standard package UCIe module in one direction.
class UnidirectionalIo(lanes: Int = 16) extends Bundle {
  val mainband = new MainbandIo(lanes)
  val sideband = new SidebandIo()
}

// The pins (mainband and sideband) exposed by a standard package UCIe module in both directions.
class StandardPackageIo(lanes: Int = 16) extends Bundle {
  val tx = Output(new UnidirectionalIo(lanes))
  val rx = Input(new UnidirectionalIo(lanes))
}

// The sideband analog front-end (AFE) interface, from the perspective of the logical PHY layer.
//
// All signals in this interface are synchronous to the sideband clock (fixed at 800 MHz).
// As a result, the sideband's `serializerRatio` likely will be different from the mainband's `serializerRatio`.
class SidebandAfeIo(
    serializerRatio: Int = 1,
) extends Bundle {
  // Data to transmit on the sideband.
  // Output from the async FIFO.
  val txData = Decoupled(Bits(serializerRatio.W))
  // Data received on the sideband.
  // Input to the async FIFO.
  val rxData = Flipped(Decoupled(Bits(serializerRatio.W)))
  // Enable sideband receivers.
  val rxEn = Output(Bool())
}

// The mainband analog front-end (AFE) interface, from the perspective of the logical PHY layer.
//
// All signals in this interface are synchronous to the mainband AFE's digital clock,
// which is produced by taking a high speed clock from a PLL
// and dividing its frequency by `serializerRatio`.
//
// With half-rate clocking (1 data bit transmitted per UI; 1 UI = 0.5 clock cycles), the PLL clock
// may be 2, 4, 6, 8, 12, or 16 GHz. With a serializer ratio of 16, this results in a 0.125-1 GHz
// AFE digital clock.
class MainbandAfeIo(
    lanes: Int = 16,
    serializerRatio: Int = 16,
) extends Bundle {
  // Data to transmit on the mainband.
  // Output from the async FIFO.
  val txData = Decoupled(Vec(lanes, Bits(serializerRatio.W)))

  // Data received on the mainband.
  // Input to the async FIFO.
  val rxData = Flipped(Decoupled(Vec(lanes, Bits(serializerRatio.W))))

  /////////////////////
  // impedance control
  //
  // Setting txZpu = txZpd = 0 sets drivers to hi-z.
  /////////////////////

  // TX pull up impedance control.
  val txZpu = Output(Vec(lanes, UInt(4.W)))
  // TX pull down impedance control.
  val txZpd = Output(Vec(lanes, UInt(4.W)))
  // RX impedance control.
  val rxZ = Output(Vec(lanes, UInt(4.W)))

  /////////////////////
  // phase control
  /////////////////////

  // Global (per-module) phase control.
  val txGlobalPhaseSel = Output(UInt(4.W))
  // Per-lane phase control.
  val txLaneDeskew = Output(Vec(lanes, UInt(4.W)))
  // Per-lane phase control.
  val rxLaneDeskew = Output(Vec(lanes, UInt(4.W)))

  /////////////////////
  // frequency control
  /////////////////////
  val txFreqSel = Output(UInt(4.W))

  /////////////////////
  // receiver control
  /////////////////////
  // Mainband receiver enable.
  val rxEn = Output(Bool())
  // Per-lane vref/offset cancellation control.
  val rxVref = Output(Vec(lanes, UInt(4.W)))

  /////////////////////
  // clock control
  /////////////////////
  // Clock gating control.
  val txClockEn = Output(Bool())
  // Clock parking level.
  //
  // Per the UCIe spec, must alternate between high and low
  // on subsequent clock gating events. If the link is using
  // free running clock mode, this signal has no effect.
  val txClockPark = Output(Bool())
}
