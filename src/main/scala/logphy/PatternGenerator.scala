package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._
import sideband.SidebandParams
import interfaces._

class PatternGeneratorIO(
    afeParams: AfeParams,
) extends Bundle {
  val transmitInfo = Flipped(Decoupled(new Bundle {
    val pattern = TransmitPattern()
    val timeoutCycles = UInt(32.W)
    val sideband = Bool()
  })) // data to transmit & receive over SB
  val transmitPatternStatus = Decoupled(MessageRequestStatusType())
}

class PatternGenerator(
    afeParams: AfeParams,
    sbParams: SidebandParams,
) extends Module {
  val io = IO(new Bundle {
    val patternGeneratorIO = new PatternGeneratorIO(afeParams)

    /** for now, assume want to transmit on sideband IO only */
    val mainbandLaneIO = new MainbandLaneIO(afeParams)
    val sidebandLaneIO = new SidebandLaneIO(sbParams)
  })

  private val writeInProgress = RegInit(false.B)
  private val readInProgress = RegInit(false.B)
  private val inProgress = writeInProgress || readInProgress
  private val pattern = RegInit(TransmitPattern.CLOCK_64_LOW_32)
  private val sideband = RegInit(true.B)
  private val timeoutCycles = RegInit(0.U)
  private val status = RegInit(MessageRequestStatusType.SUCCESS)
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

  // val clockPatternShiftReg = RegInit("h_aaaa_aaaa_aaaa_aaaa_0000_0000".U)
  /** clock gating is not implemented, so for now, just send 128 bits of regular
    * clock data
    */
  val clockPatternShiftReg = RegInit(
    "h_aaaa_aaaa_aaaa_aaaa_aaaa_aaaa_aaaa_aaaa".U,
  )
  val patternToTransmit = Wire(0.U(sbParams.sbNodeMsgWidth.W))
  val patternDetectedCount = RegInit(0.U)
  val patternWrittenCount = RegInit(0.U)

  val patternWrittenCountMax = Seq(
    TransmitPattern.CLOCK_64_LOW_32 -> 4.U,
  )

  val patternDetectedCountMax = Seq(
    TransmitPattern.CLOCK_64_LOW_32 -> 128.U,
  )

  // val outWidth = 4
  // private val sidebandInWidthCoupler = new DataWidthCoupler(
  //   /** collect size of largest pattern */
  //   DataWidthCouplerParams(
  //     inWidth = afeParams.sbSerializerRatio,
  //     outWidth = outWidth,
  //   ),
  // )
  // sidebandInWidthCoupler.io.in <> io.sidebandLaneIO.rxData
  io.sidebandLaneIO.txData.valid := writeInProgress
  io.sidebandLaneIO.txData.bits := patternToTransmit
  io.sidebandLaneIO.rxData.ready := readInProgress
  // sidebandInWidthCoupler.io.out.ready := readInProgress

  when(inProgress) {
    timeoutCycles := timeoutCycles - 1.U
    when(timeoutCycles === 0.U) {
      status := MessageRequestStatusType.ERR
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
      status := MessageRequestStatusType.SUCCESS
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
          sbParams.sbNodeMsgWidth - 1,
          0,
        )
        when(io.sidebandLaneIO.txData.fire) {
          clockPatternShiftReg := (clockPatternShiftReg >> sbParams.sbNodeMsgWidth.U).asUInt &
            (clockPatternShiftReg <<
              (clockPatternShiftReg.getWidth.U - sbParams.sbNodeMsgWidth.U))
          patternWrittenCount := patternWrittenCount + 1.U
        }
      }
    }

  }

  when(readInProgress) {
    switch(pattern) {

      is(TransmitPattern.CLOCK_64_LOW_32) {

        assert(
          sbParams.sbNodeMsgWidth.W == 128,
          "comparing with 128 bit clock pattern",
        )
        val patternToDetect = "h_aaaa_aaaa_aaaa_aaaa_aaaa_aaaa_aaaa_aaaa".U(
          sbParams.sbNodeMsgWidth.W,
        )
        when(io.sidebandLaneIO.rxData.fire) {

          /** detect clock UI pattern -- as long as the pattern is correctly
            * aligned, this is simple
            */
          when(io.sidebandLaneIO.rxData.bits === patternToDetect) {
            patternDetectedCount := patternDetectedCount + sbParams.sbNodeMsgWidth.U
          }
        }

      }
    }

  }

}
