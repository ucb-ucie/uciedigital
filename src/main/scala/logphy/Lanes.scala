package edu.berkeley.cs.ucie.digital
package logphy

import interfaces._
import chisel3._
import chisel3.util._

case class LaneParams(
)

class MainbandLaneIO(
    afeParams: AfeParams,
) extends Bundle {

  /** Data to transmit on the mainband.
    *
    * Output from the async FIFO.
    *
    * @group data
    */
  val txData = Decoupled(
    Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W)),
  )

  val txValid = Decoupled(
    Bool(),
  )

  /** Data received on the mainband.
    *
    * Input to the async FIFO.
    *
    * @group data
    */
  val rxData = Flipped(
    Decoupled(Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W))),
  )
}

class Lanes(
    params: AfeParams,
) {
  val io = IO(new Bundle() {
    val mainbandIo = new MainbandLaneIO(params)
    val sidebandIo = new SBLaneIO(params)

  })

  for (i <- 0 until params.mbLanes) {
  }

}
