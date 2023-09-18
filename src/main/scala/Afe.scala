package ucie

import chisel3._
import chisel3.util._

class StandardPackageIo(lanes: Int = 16) extends Bundle {
  // TX
  val txData = Output(Bits(lanes.W))
  val txValid = Output(Bool())
  val txTrack = Output(Bool())
  val txClkp = Output(Bool())
  val txClkn = Output(Bool())

  // RX
  val rxData = Input(Bits(lanes.W))
  val rxValid = Input(Bool())
  val rxTrack = Input(Bool())
  val rxClkp = Input(Bool())
  val rxClkn = Input(Bool())

  // Sideband
  val txDataSb = Output(Bool())
  val rxDataSb = Input(Bool())
  val txClkSb = Output(Bool())
  val rxClkSb = Input(Bool())
}

// The analog front-end (AFE) interface, from the perspective of the logical PHY layer.
class AfeIo(
    lanes: Int = 16,
    mbSerializerRatio: Int = 16,
    sbSerializerRatio: Int = 1,
) extends Bundle {
  // Top-level pins.
  val pins = new StandardPackageIo(lanes)

  // Data to transmit on the mainband.
  // Output from the async FIFO.
  val txMbData = Decoupled(Vec(lanes, UInt(mbSerializerRatio.W)))
  // Data to transmit on the sideband.
  // Output from the async FIFO.
  val txSbData = Decoupled(UInt(sbSerializerRatio.W))

  // Data received on the mainband.
  // Input to the async FIFO.
  val rxMbData = Flipped(Decoupled(Vec(lanes, UInt(mbSerializerRatio.W))))
  val rxSbData = Flipped(Decoupled(UInt(sbSerializerRatio.W)))

  /////////////////////
  // impedance control
  //
  // Setting txZpu = txZpd = 0 sets drivers to hi-z.
  /////////////////////

  // TX pull up impedance control.
  val txZpu = Input(Vec(lanes, UInt(4.W)))
  // TX pull down impedance control.
  val txZpd = Input(Vec(lanes, UInt(4.W)))
  // RX impedance control.
  val rxZ = Input(Vec(lanes, UInt(4.W)))

  /////////////////////
  // phase control
  /////////////////////

  // global (per-module) phase control
  val txGlobalPhaseSel = Input(UInt(4.W))
  // per-lane phase control
  val txLaneDeskew = Input(Vec(lanes, UInt(4.W)))
  // per-lane phase control
  val rxLaneDeskew = Input(Vec(lanes, UInt(4.W)))

  /////////////////////
  // frequency control
  /////////////////////
  val txFreqSel = Input(UInt(4.W))

  /////////////////////
  // receiver control
  /////////////////////
  // mainband receiver enable
  val rxEn = Input(Bool())
  // sideband receiver enable
  val rxSbEn = Input(Bool())
  // per-lane vref/offset cancellation control
  val rxVref = Input(Vec(lanes, UInt(4.W)))

  /////////////////////
  // clock control
  /////////////////////
  // TODO: these may need to go through an async FIFO
  val txClockEn = Input(Bool())
  val txClockPark = Input(Bool())
}
