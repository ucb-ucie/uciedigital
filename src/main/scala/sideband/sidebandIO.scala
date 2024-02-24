package edu.berkeley.cs.ucie.digital
package sideband

import chisel3._
import chisel3.util._

import interfaces._

class D2DSidebandChannelIO(
    val sbParams: SidebandParams,
    val fdiParams: FdiParams,
) extends Bundle {
  // connect to another sideband node in the layer above (protocol layer)
  val to_upper_layer = new Bundle {
    val tx = new Bundle {
      val bits = Output(UInt(fdiParams.sbWidth.W))
      val valid = Output(Bool())
      val credit = Input(Bool())
    }
    val rx = new Bundle {
      val bits = Input(UInt(fdiParams.sbWidth.W))
      val valid = Input(Bool())
      val credit = Output(Bool())
    }
  }
  // connect to another sideband node in the layer below (physical layer)
  val to_lower_layer = new Bundle {
    val tx = new Bundle {
      val bits = Output(UInt(fdiParams.sbWidth.W))
      val valid = Output(Bool())
      val credit = Input(Bool())
    }
    val rx = new Bundle {
      val bits = Input(UInt(fdiParams.sbWidth.W))
      val valid = Input(Bool())
      val credit = Output(Bool())
    }
  }
  // d2d layer drive these
  val inner = Flipped(new SidebandSwitcherbundle(sbParams))
}

class PHYSidebandChannelIO(
    val sbParams: SidebandParams,
    val fdiParams: FdiParams,
) extends Bundle {
  // connect to another sideband node in the layer above (d2d layer)
  val to_upper_layer = new Bundle {
    val tx = new Bundle {
      val bits = Output(UInt(fdiParams.sbWidth.W))
      val valid = Output(Bool())
      val credit = Input(Bool())
    }
    val rx = new Bundle {
      val bits = Input(UInt(fdiParams.sbWidth.W))
      val valid = Input(Bool())
      val credit = Output(Bool())
    }
  }
  // connect to another sideband node in the layer below (link layer)
  val to_lower_layer = new Bundle {
    val tx = new Bundle {
      val bits = Output(UInt(fdiParams.sbWidth.W))
      val clock = Output(Bool())
    }
    val rx = new Bundle {
      val bits = Input(UInt(fdiParams.sbWidth.W))
      val clock = Input(Bool())
    }
  }
  // phy layer drive these
  val inner = Flipped(new SidebandSwitcherbundle(sbParams))
}

// IO for the RDI and FDI sideband
class SidebandNodeIO(val sbParams: SidebandParams, val fdiParams: FdiParams)
    extends Bundle {
  // layers drive these
  val inner = new Bundle {
    val layer_to_node = Flipped(Decoupled(UInt(sbParams.sbNodeMsgWidth.W)))
    /* This signal overrides the tx.ready and takes up the priority reserved
     * queue slot */
    // Should only be asserted high for access completion packets
    val node_to_layer = Decoupled(UInt(sbParams.sbNodeMsgWidth.W))
  }
  // connect these to another sideband node
  val outer = new Bundle {
    val tx = new Bundle {
      val bits = Output(UInt(fdiParams.sbWidth.W))
      val valid = Output(Bool())
      val credit = Input(Bool())
    }
    val rx = new Bundle {
      val bits = Input(UInt(fdiParams.sbWidth.W))
      val valid = Input(Bool())
      val credit = Output(Bool())
    }
  }
}

class SidebandNodeOuterIO(
    val sbParams: SidebandParams,
    val fdiParams: FdiParams,
) extends Bundle {
  val tx = new Bundle {
    val bits = Output(UInt(fdiParams.sbWidth.W))
    val valid = Output(Bool())
    val credit = Input(Bool())
  }
  val rx = new Bundle {
    val bits = Input(UInt(fdiParams.sbWidth.W))
    val valid = Input(Bool())
    val credit = Output(Bool())
  }
}

class SidebandLinkNodeOuterIO(
    val sbParams: SidebandParams,
    val fdiParams: FdiParams,
) extends Bundle {
  val tx = new Bundle {
    val bits = Output(UInt(fdiParams.sbWidth.W))
    val clock = Output(Bool())
  }
  val rx = new Bundle {
    val bits = Input(UInt(fdiParams.sbWidth.W))
    val clock = Input(Bool())
  }
}

// IO for the remote sideband
class SidebandLinkIO(val sbParams: SidebandParams, val fdiParams: FdiParams)
    extends Bundle {
  // layers drive these
  val inner = new Bundle {
    val layer_to_node = Flipped(Decoupled(UInt(sbParams.sbNodeMsgWidth.W)))
    /* This signal overrides the tx.ready and takes up the priority reserved
     * queue slot */
    // Should only be asserted high for access completion packets
    val node_to_layer = Decoupled(UInt(sbParams.sbNodeMsgWidth.W))
  }
  // connect these to another sideband node
  val outer = new Bundle {
    val tx = new Bundle {
      val bits = Output(UInt(fdiParams.sbWidth.W))
      val clock = Output(Bool())
    }
    val rx = new Bundle {
      val bits = Input(UInt(fdiParams.sbWidth.W))
      val clock = Input(Bool())
    }
  }
}

class SidebandSwitcherIO(val sbParams: SidebandParams) extends Bundle {
  // layer drive these
  val inner = Flipped(new SidebandSwitcherbundle(sbParams))
  // Sideband node drive these
  val outer = new SidebandSwitcherbundle(sbParams)
}

class SidebandSwitcherbundle(val sbParams: SidebandParams) extends Bundle {
  val node_to_layer_above = Flipped(Decoupled(UInt(sbParams.sbNodeMsgWidth.W)))
  val layer_to_node_above = Decoupled(UInt(sbParams.sbNodeMsgWidth.W))
  val node_to_layer_below = Flipped(Decoupled(UInt(sbParams.sbNodeMsgWidth.W)))
  val layer_to_node_below = Decoupled(UInt(sbParams.sbNodeMsgWidth.W))
}
