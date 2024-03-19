package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._
import sideband.SidebandParams
import interfaces._

class PatternGeneratorIO extends Bundle {
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
    val patternGeneratorIO = new PatternGeneratorIO()

    /** for now, assume want to transmit on sideband IO only */
    val mainbandLaneIO = Flipped(new MainbandLaneIO(afeParams))
    val sidebandLaneIO = Flipped(new SidebandLaneIO(sbParams))
  })

  /** TODO: remove */
  io.mainbandLaneIO.txData.noenq()

  private val writeInProgress = RegInit(false.B)
  private val readInProgress = RegInit(false.B)
  private val inProgress = WireInit(writeInProgress || readInProgress)
  private val pattern = RegInit(TransmitPattern.CLOCK_64_LOW_32)
  private val sideband = RegInit(true.B)
  private val timeoutCycles = RegInit(0.U(32.W))
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

  /** clock gating is not implemented, so for now, just send 128 bits of regular
    * clock data
    */
  val clockPatternShiftReg = RegInit(
    "h_aaaa_aaaa_aaaa_aaaa_aaaa_aaaa_aaaa_aaaa".U,
  )
  val patternToTransmit = WireInit(0.U(sbParams.sbNodeMsgWidth.W))
  val patternDetectedCount = RegInit(0.U(log2Ceil(128 * 2 + 1).W))
  val patternWrittenCount = RegInit(0.U(log2Ceil(2 + 1).W))

  val patternWrittenCountMax = Seq(
    TransmitPattern.CLOCK_64_LOW_32 -> 2.U,
  )

  val patternDetectedCountMax = Seq(
    TransmitPattern.CLOCK_64_LOW_32 -> 128.U,
  )

  io.sidebandLaneIO.txData.valid := writeInProgress
  io.sidebandLaneIO.txData.bits := patternToTransmit
  io.sidebandLaneIO.rxData.ready := readInProgress

  when(io.patternGeneratorIO.transmitPatternStatus.fire) {
    statusValid := false.B
  }

  when(inProgress) {
    timeoutCycles := timeoutCycles - 1.U
    when(timeoutCycles === 0.U) {
      status := MessageRequestStatusType.ERR
      statusValid := true.B
      writeInProgress := false.B
      readInProgress := false.B
      patternWrittenCount := 0.U
      patternDetectedCount := 0.U
    }.elsewhen(
      (patternWrittenCount >= MuxLookup(pattern, 0.U)(
        patternWrittenCountMax,
      )) && (patternDetectedCount >= MuxLookup(pattern, 0.U)(
        patternDetectedCountMax,
      )),
    ) {
      statusValid := true.B
      status := MessageRequestStatusType.SUCCESS
      writeInProgress := false.B
      readInProgress := false.B
      patternWrittenCount := 0.U
      patternDetectedCount := 0.U
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
          /* clockPatternShiftReg := (clockPatternShiftReg >>
           * sbParams.sbNodeMsgWidth.U).asUInt & */
          //   (clockPatternShiftReg <<
          //     (clockPatternShiftReg.getWidth.U - sbParams.sbNodeMsgWidth.U))
          printf("pattern written count: %d\n", patternWrittenCount)
          patternWrittenCount := patternWrittenCount + 1.U
        }
      }
    }

  }

  when(readInProgress) {
    switch(pattern) {

      is(TransmitPattern.CLOCK_64_LOW_32) {

        assert(
          sbParams.sbNodeMsgWidth == 128,
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
