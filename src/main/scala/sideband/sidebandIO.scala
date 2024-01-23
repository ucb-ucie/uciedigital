package ucie.sideband

import chisel3._
import chisel3.util._
import chisel3.experimental._

// import freechips.rocketchip.config.Parameters
import freechips.rocketchip.util._

class D2DSidebandChannelIO (val params: SidebandParams) extends Bundle {
    // connect to another sideband node in the layer above (protocol layer)
    val to_upper_layer = new Bundle {
        val tx = new Bundle {
            val bits = Output(UInt(params.NC_width.W))
            val valid = Output(Bool())
            val credit = Input(Bool())
        }
        val rx = new Bundle {
            val bits = Input(UInt(params.NC_width.W))
            val valid = Input(Bool())
            val credit = Output(Bool())
        }
    }
    // connect to another sideband node in the layer below (physical layer)
    val to_lower_layer = new Bundle {
        val tx = new Bundle {
            val bits = Output(UInt(params.NC_width.W))
            val valid = Output(Bool())
            val credit = Input(Bool())
        }
        val rx = new Bundle {
            val bits = Input(UInt(params.NC_width.W))
            val valid = Input(Bool())
            val credit = Output(Bool())
        }
    }
    // d2d layer drive these
    val inner = Flipped(new SidebandSwitcherbundle(params))
}

class PHYSidebandChannelIO (val params: SidebandParams) extends Bundle {
    // connect to another sideband node in the layer above (d2d layer)
    val to_upper_layer = new Bundle {
        val tx = new Bundle {
            val bits = Output(UInt(params.NC_width.W))
            val valid = Output(Bool())
            val credit = Input(Bool())
        }
        val rx = new Bundle {
            val bits = Input(UInt(params.NC_width.W))
            val valid = Input(Bool())
            val credit = Output(Bool())
        }
    }
    // connect to another sideband node in the layer below (link layer)
    val to_lower_layer = new Bundle {
        val tx = new Bundle {
            val bits = Output(UInt(params.NC_width.W))
            val clock = Output(Bool())
        }
        val rx = new Bundle {
            val bits = Input(UInt(params.NC_width.W))
            val clock = Input(Bool())
        }
    }
    // phy layer drive these
    val inner = Flipped(new SidebandSwitcherbundle(params))
}

// IO for the RDI and FDI sideband
class SidebandNodeIO (val params: SidebandParams) extends Bundle {
    // layers drive these
    val inner = new Bundle {
        val layer_to_node = Flipped(Decoupled(UInt(params.MSG_width.W)))
        // This signal overrides the tx.ready and takes up the priority reserved queue slot
        // Should only be asserted high for access completion packets
        val node_to_layer = Decoupled(UInt(params.MSG_width.W))
    }
    // connect these to another sideband node
    val outer = new Bundle {
        val tx = new Bundle {
            val bits = Output(UInt(params.NC_width.W))
            val valid = Output(Bool())
            val credit = Input(Bool())
        }
        val rx = new Bundle {
            val bits = Input(UInt(params.NC_width.W))
            val valid = Input(Bool())
            val credit = Output(Bool())
        }
    }
}

class SidebandNodeOuterIO (val params: SidebandParams) extends Bundle {
    val tx = new Bundle {
            val bits = Output(UInt(params.NC_width.W))
            val valid = Output(Bool())
            val credit = Input(Bool())
        }
        val rx = new Bundle {
            val bits = Input(UInt(params.NC_width.W))
            val valid = Input(Bool())
            val credit = Output(Bool())
        }
}

class SidebandLinkNodeOuterIO (val params: SidebandParams) extends Bundle {
    val tx = new Bundle {
        val bits = Output(UInt(params.NC_width.W))
        val clock = Output(Bool())
    }
    val rx = new Bundle {
        val bits = Input(UInt(params.NC_width.W))
        val clock = Input(Bool())
    }
}

// IO for the remote sideband
class SidebandLinkIO (val params: SidebandParams) extends Bundle {
    // layers drive these
    val inner = new Bundle {
        val layer_to_node = Flipped(Decoupled(UInt(params.MSG_width.W)))
        // This signal overrides the tx.ready and takes up the priority reserved queue slot
        // Should only be asserted high for access completion packets
        val node_to_layer = Decoupled(UInt(params.MSG_width.W))
    }
    // connect these to another sideband node
    val outer = new Bundle {
        val tx = new Bundle {
            val bits = Output(UInt(params.NC_width.W))
            val clock = Output(Bool())
        }
        val rx = new Bundle {
            val bits = Input(UInt(params.NC_width.W))
            val clock = Input(Bool())
        }
    }
}

class SidebandSwitcherIO (val params: SidebandParams) extends Bundle {
    //layer drive these
    val inner = Flipped(new SidebandSwitcherbundle(params))
    // Sideband node drive these
    val outer = new SidebandSwitcherbundle(params)
}

class SidebandSwitcherbundle (val params: SidebandParams) extends Bundle {
    val node_to_layer_above = Flipped(Decoupled(UInt(params.MSG_width.W)))
    val layer_to_node_above = Decoupled(UInt(params.MSG_width.W))
    val node_to_layer_below = Flipped(Decoupled(UInt(params.MSG_width.W)))
    val layer_to_node_below = Decoupled(UInt(params.MSG_width.W))
}