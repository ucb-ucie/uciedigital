package ucie.sideband

import chisel3._
import chisel3.util._
import chisel3.experimental._

// import freechips.rocketchip.config.Parameters
import freechips.rocketchip.util._

class D2DSidebandChannel (val myID: BigInt = BigInt(1), val params: SidebandParams) extends Module {
    val io = IO(new D2DSidebandChannelIO(params))

    // Instantiate submodule
    val upper_node = Module(new SidebandNode(params))
    val switcher = Module(new sidebandSwitcher(myID, params))
    val lower_node = Module(new SidebandNode(params))

    // Connect outer signals
    io.to_upper_layer <> upper_node.io.outer
    io.to_lower_layer <> lower_node.io.outer

    // Connect two sidebandNodes and switcher
    upper_node.io.inner.layer_to_node <> switcher.io.outer.layer_to_node_above
    upper_node.io.inner.node_to_layer <> switcher.io.outer.node_to_layer_above
    lower_node.io.inner.layer_to_node <> switcher.io.outer.layer_to_node_below
    lower_node.io.inner.node_to_layer <> switcher.io.outer.node_to_layer_below

    // Connect inner signals
    io.inner <> switcher.io.inner
}

class PHYSidebandChannel (val myID: BigInt = BigInt(2), val params: SidebandParams) extends Module {
    val io = IO(new PHYSidebandChannelIO(params))

    // Instantiate submodule
    val upper_node = Module(new SidebandNode(params))
    val switcher = Module(new sidebandSwitcher(myID, params))
    val lower_node = Module(new SidebandLinkNode(params))

    // Connect outer signals
    io.to_upper_layer <> upper_node.io.outer
    io.to_lower_layer <> lower_node.io.outer

    // Connect two sidebandNodes and switcher
    upper_node.io.inner.layer_to_node <> switcher.io.outer.layer_to_node_above
    upper_node.io.inner.node_to_layer <> switcher.io.outer.node_to_layer_above
    lower_node.io.inner.layer_to_node <> switcher.io.outer.layer_to_node_below
    lower_node.io.inner.node_to_layer <> switcher.io.outer.node_to_layer_below

    // Connect inner signals
    io.inner <> switcher.io.inner
}