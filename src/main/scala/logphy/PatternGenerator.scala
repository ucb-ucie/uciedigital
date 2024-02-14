package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._
import interfaces._

class PatternGeneratorIO(
    afeParams: AfeParams,
) extends Bundle {
  val transmitInfo = Flipped(Decoupled(new Bundle {
    val pattern = TransmitPattern()
    val timeoutCycles = UInt(32.W)
    val sideband = Bool()
  })) // data to transmit & receive over SB
  val transmitPatternStatus = Decoupled(SBMsgExchangeStatus())
}

class PatternGenerator(
    afeParams: AfeParams,
) extends Module {
  val io = IO(new Bundle {
    val patternGeneratorIO = new PatternGeneratorIO(afeParams)

    /** for now, assume want to transmit on sideband IO only */
    val mainbandLaneIO = new MainbandLaneIO(afeParams)
    val sidebandLaneIO = new SidebandLaneIO(afeParams)
  })

  private val writeInProgress = RegInit(false.B)
  private val readInProgress = RegInit(false.B)
  private val inProgress = writeInProgress || readInProgress
  private val pattern = RegInit(TransmitPattern.CLOCK_64_LOW_32)
  private val sideband = RegInit(true.B)
  private val timeoutCycles = RegInit(0.U)
  private val status = RegInit(SBMsgExchangeStatus())
  private val statusValid = RegInit(false.B)
  io.patternGeneratorIO.transmitInfo.ready := (inProgress === false.B)
  io.patternGeneratorIO.transmitPatternStatus.valid := statusValid
  io.patternGeneratorIO.transmitPatternStatus.bits := status

  when(io.patternGeneratorIO.transmitInfo.fire) {
    writeInProgress := true.B
    readInProgress := true.B
    pattern := io.patternGeneratorIO.transmitInfo.bits.pattern
    sideband := io.patternGeneratorIO.transmitInfo.bits.sideband
    timeoutCycles := io.patternGeneratorIO.transmitInfo.bits.timeoutCycles
    statusValid := false.B
  }

  val clockPatternShiftReg = RegInit("h_aaaa_aaaa_aaaa_aaaa_0000_0000".U)
  val patternToTransmit = Wire(0.U(afeParams.sbSerializerRatio.W))
  val patternDetectedCount = RegInit(0.U)
  val patternWrittenCount = RegInit(0.U)

  val patternWrittenCountMax = Seq(
    TransmitPattern.CLOCK_64_LOW_32 -> 4.U,
  )

  val patternDetectedCountMax = Seq(
    TransmitPattern.CLOCK_64_LOW_32 -> 128.U,
  )

  val outWidth = 4
  private val sidebandInWidthCoupler = new DataWidthCoupler(
    /** collect size of largest pattern */
    DataWidthCouplerParams(
      inWidth = afeParams.sbSerializerRatio,
      outWidth = outWidth,
    ),
  )
  sidebandInWidthCoupler.io.in <> io.sidebandLaneIO.rxData
  io.sidebandLaneIO.txData.valid := writeInProgress
  io.sidebandLaneIO.txData.bits := patternToTransmit
  // io.sidebandLaneIO.rxData.ready := readInProgress
  sidebandInWidthCoupler.io.out.ready := readInProgress

  when(inProgress) {
    timeoutCycles := timeoutCycles - 1.U
    when(timeoutCycles === 0.U) {
      status := SBMsgExchangeStatus.ERR
      statusValid := true.B
      inProgress := false.B
    }.elsewhen(
      (patternWrittenCount === MuxLookup(pattern, 0.U)(
        patternWrittenCountMax,
      )) && (patternDetectedCount === MuxLookup(pattern, 0.U)(
        patternDetectedCountMax,
      )),
    ) {
      statusValid := true.B
      status := SBMsgExchangeStatus.SUCCESS
      inProgress := false.B
    }
  }

  when(writeInProgress) {
    switch(pattern) {

      /** Patterns may be different lengths, etc. so may be best to handle
        * separately, for now
        */
      is(TransmitPattern.CLOCK_64_LOW_32) {
        patternToTransmit := clockPatternShiftReg(
          afeParams.sbSerializerRatio - 1,
          0,
        )
        when(io.sidebandLaneIO.txData.fire) {
          clockPatternShiftReg := (clockPatternShiftReg >> afeParams.sbSerializerRatio.U).asUInt &
            (clockPatternShiftReg <<
              (clockPatternShiftReg.getWidth.U - afeParams.sbSerializerRatio.U))
          patternWrittenCount := patternWrittenCount + 1.U
        }
      }
    }

  }

  when(readInProgress) {
    switch(pattern) {

      is(TransmitPattern.CLOCK_64_LOW_32) {

        val patternToDetect = "h_a".U(outWidth.W)
        when(sidebandInWidthCoupler.io.out.fire) {

          /** detect clock UI pattern -- as long as the pattern is correctly
            * aligned, this is simple
            *
            * TODO: should I do more for pattern detection? right now for
            * pattern detecting 128 clock UI, I count the clock cycles in chunks
            * of 4 and add 4 if the pattern is 1010, but this wouldn't work if
            * it is misaligned for any reason
            */
          when(sidebandInWidthCoupler.io.out.bits === patternToDetect) {
            patternDetectedCount := patternDetectedCount + outWidth.U
          }
        }

      }
    }

  }

}
