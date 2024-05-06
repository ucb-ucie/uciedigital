package edu.berkeley.cs.ucie.digital
package afe

import chisel3._
import chisel3.util._ 
import chisel3.stage.ChiselStage
import interfaces._
import chisel3.experimental.DataMirror
import freechips.rocketchip.util.{AsyncQueue, AsyncQueueParams}

// This module receives data from adapter and sends to analog
class TxMainband(afeParams: AfeParams, queueParams: AsyncQueueParams, BYTE: Int = 8) extends Module {
    val io = IO(new Bundle {
        // should use rx of mbafeIo
        // val mbAfeIo = new MainbandAfeIo(AfeParams())
        val rxMbAfe =  Flipped(Decoupled(Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W))))
        val txMbIo = Output(new MainbandIo())

        val clkp = Input(Clock())
        val clkn = Input(Clock())
        val track = Input(Bool())        
        // val output_clkp = Output(Clock())
        // Dummy signals for testing
    })
    val lanes = afeParams.mbLanes
    val width = afeParams.mbSerializerRatio

    // io.mbAfeIo.txData.bits := Seq.fill(lanes)(0.U)
    // io.mbAfeIo.txData.valid := false.B 
    
    // receive data
    // val rxMbAfeData = io.mbAfeIo.rxData
    val rxMbAfeData = io.rxMbAfe

    // Default fifo has problem: when deq starts, data is XXXX for at least 10 cycles, not sure why
    // Use custom async fifo, this one works
    // val txMbFifos = Seq.fill(lanes)(Module (new AsyncFifoStefan(depth, width)))
    val txMbFifos = Seq.fill(lanes)(Module (
        new AsyncQueue(
            Bits(afeParams.mbSerializerRatio.W), 
            queueParams)
        )
    )


    withClock(io.clkp) {
        val outValid = Wire(Bool())
        val outTrack = Wire(Bool())
        val txMbShiftRegs = Seq.fill(lanes)(RegInit(0.U(width.W)))
        val txMbUICounter = RegInit(0.U(log2Ceil(width).W))
        // val txMbUICounter_next = RegNext(txMbUICounter, (BYTE/2-1).U) // To synchronize mbio valid signal
        val txMbUICounter_next = RegNext(txMbUICounter, 0.U) // To synchronize mbio valid signal
        val hasData = Wire(Bool())
        val clockGateCounter = RegInit(0.U(log2Ceil(width).W))
        val fifoValid_next = RegNext(txMbFifos.map(_.io.deq.valid).reduce(_ && _))
               // val txMbUICounter_next = RegNext(txMbUICounter, (BYTE/2-1).U) // To synchronize mbio valid signal
 
        val shift = RegInit(false.B)
        val outValid_next = RegNext(outValid)
        hasData := ~(txMbFifos.map(_.io.deq.valid).reduce(_ && _) ^ fifoValid_next ) & fifoValid_next 
        when(outValid){
            clockGateCounter := 0.U
        }.elsewhen(~outValid && clockGateCounter < width.U) {
            clockGateCounter := clockGateCounter + 1.U
        }

        io.txMbIo.clkn := Mux((clockGateCounter >= width.U && ~outValid), false.B, io.clkn.asBool).asClock
        io.txMbIo.clkp := Mux((clockGateCounter >= width.U && ~outValid), false.B, io.clkp.asBool).asClock
        // io.output_clkp := Mux((clockGateCounter >= 8.U && ~outValid), 0.U, io.clkp.asUInt)
        // Assign each async fifo individually
        txMbFifos.zipWithIndex.foreach{ case (txMbFifo, i) =>
            // Enqueue end from adapter
            txMbFifo.io.enq_clock := clock //enq is from afe, use system clock
            txMbFifo.io.enq_reset := reset // use system reset
            txMbFifo.io.enq.bits  := rxMbAfeData.bits(i)
            txMbFifo.io.enq.valid := rxMbAfeData.valid

            // Dequeue end to analog
            txMbFifo.io.deq_clock := io.clkp
            txMbFifo.io.deq_reset := reset
            txMbFifo.io.deq.ready := false.B
            // Valid framing, up for first 4 ui, down for last 4 ui
            // outValid := false.B 
            outTrack := false.B
            when( txMbUICounter_next =/= txMbUICounter && (txMbUICounter_next % BYTE.U) <= (BYTE/2-1).U){
                outValid := true.B
            }.otherwise{
                outValid := false.B
            }
            when(hasData){
                when(txMbUICounter === 0.U) {
                    txMbFifo.io.deq.ready := true.B
                    txMbShiftRegs(i) := txMbFifo.io.deq.bits
                }.otherwise{
                    txMbShiftRegs(i) := txMbShiftRegs(i) << 1.U
                }
                txMbUICounter := txMbUICounter + 1.U
            }
            when(txMbUICounter === 0.U && ~hasData){
                txMbUICounter := 0.U
                shift := false.B
            }.otherwise{
                txMbUICounter := txMbUICounter + 1.U
                shift := true.B
            }
            when(shift){
                when(txMbUICounter === 0.U) {
                    txMbFifo.io.deq.ready := true.B
                    txMbShiftRegs(i) := txMbFifo.io.deq.bits
                }.otherwise{
                    txMbShiftRegs(i) := txMbShiftRegs(i) << 1.U
                }
            }
        }
        io.txMbIo.data := VecInit(txMbShiftRegs.map(_.head(1))).asUInt
        rxMbAfeData.ready := txMbFifos.map(_.io.enq.ready).reduce(_ && _) 

        io.txMbIo.track := outTrack
        io.txMbIo.valid := outValid
    }

    
    withClock(clock) {
        txMbFifos.zipWithIndex.foreach{ case (txMbFifo, i) =>
            // Enqueue end from adapter
            txMbFifo.io.enq_clock := clock //enq is from afe, use system clock
            txMbFifo.io.enq_reset := reset // use system reset
            txMbFifo.io.enq.bits  := rxMbAfeData.bits(i)
            txMbFifo.io.enq.valid := rxMbAfeData.valid
    
        }
    }
}

// This module accepts data from analog and send to adapter
class RxMainband(afeParams: AfeParams, queueParams: AsyncQueueParams, BYTE: Int = 8) extends Module {
    val io = IO(new Bundle {
        // should use rx of mbafeIo
        // val mbAfeIo = new MainbandAfeIo(AfeParams())
        val rxMbIo = Input(new MainbandIo())
        val txMbAfe = Decoupled(Vec(afeParams.mbLanes, Bits(afeParams.mbSerializerRatio.W)))
        // Dummy signals for testing
        val clkn_out = Output(Clock())
        
    })

    private val width = afeParams.mbSerializerRatio
    private val lanes = afeParams.mbLanes
    // Since sending data to adapter,
    // This module Should drive mbAfeIo tx data
    // io.mbAfeIo.rxData.ready := false.B 
    io.clkn_out := io.rxMbIo.clkn
    // io.mbAfeIo.rxData.bits := Seq.fill(lanes)(0.U) 
    // val txMbAfeData = io.mbAfeIo.txData
    val txMbAfeData = io.txMbAfe

    // This module receives data from analog, and store into async buffer
    // val rxMbFifos = Seq.fill(lanes)(Module (new AsyncFifoStefan(depth, width)))

    val rxMbFifos = Seq.fill(lanes)(Module (
        new AsyncQueue(
            Bits(afeParams.mbSerializerRatio.W), 
            queueParams)
        )
    )

        // Enqueue end from analog
    withClock(io.rxMbIo.clkp) {
        val mbIoValid_pipe_0 = RegNext(io.rxMbIo.valid)
        val mbIoValid_pipe_1 = RegNext(mbIoValid_pipe_0)
        val mbIoValid_pipe_2 = RegNext(mbIoValid_pipe_1)
        val mbIoValid_next = RegNext(mbIoValid_pipe_2)

        // Shiftregs to deserialize and store into the async buffer
        val rxMbShiftRegs = Seq.fill(lanes)(RegInit(0.U(width.W)))
        val rxMbShiftRegs_next = Seq.fill(lanes)(RegInit(0.U(width.W)))
        val rxMbShiftRegs_xor = Seq.fill(lanes)(WireInit(0.U(width.W)))

        val rxMbUICounter = RegInit(0.U(log2Ceil(width).W))
        
        val rxMbUICounter_next = RegNext(rxMbUICounter)

        // val rxMbIoData_next = RegNext(io.rxMbIo.data)
        val rxMbIoData_next = RegInit(0.U(width.W))
        val fifo_enq_valid_next = RegNext(rxMbUICounter_next === (width-1).U && rxMbUICounter === 0.U)
        val internal_valid = (mbIoValid_next ^ io.rxMbIo.valid) | (mbIoValid_next & io.rxMbIo.valid)
         
        rxMbFifos.zipWithIndex.foreach{ case(rxMbFifo, i) =>

            rxMbFifo.io.enq_clock := io.rxMbIo.clkp 
            rxMbFifo.io.enq_reset := reset
            rxMbFifo.io.enq.valid := false.B
            // For clear testing visuals, should always connect to signal path for minimal delay
            rxMbFifo.io.enq.bits := 0.U
            // rxMbFifo.io.enq.bits := Cat(rxMbShiftRegs.reverse)
            // There's a little overlap of assert high of io.rxMbIo.valid and last stage pipeline
            // 
            when(internal_valid){
                rxMbIoData_next := io.rxMbIo.data 
                when(rxMbUICounter === 0.U) {
                    for(i <- 0 until lanes) {
                        rxMbShiftRegs(i) := 0.U | io.rxMbIo.data (i)
                    }
                    rxMbShiftRegs_next(i) := rxMbShiftRegs(i)

                }.otherwise {
                    for(i <- 0 until lanes) {
                        rxMbShiftRegs(i) := rxMbShiftRegs(i) << 1.U | io.rxMbIo.data (i)
                    } 
                    rxMbShiftRegs_next(i) := 0.U

                }
                rxMbUICounter := rxMbUICounter + 1.U
            }
            rxMbShiftRegs_xor(i) := rxMbShiftRegs(i) ^ rxMbShiftRegs_next(i)
            when((rxMbUICounter_next === (width-1).U && rxMbUICounter === 0.U) 
                ^ fifo_enq_valid_next 
                ) {
                rxMbFifo.io.enq.valid := true.B
                rxMbFifo.io.enq.bits := Cat(rxMbShiftRegs_xor.reverse)
            }
        }
    }
    withClock(clock) {
        
        rxMbFifos.zipWithIndex.foreach{ case(rxMbFifo, i) =>

                // Dequeue end to drive 
            rxMbFifo.io.deq_clock := clock
            rxMbFifo.io.deq_reset := reset
            txMbAfeData.bits(i) := rxMbFifo.io.deq.bits
            txMbAfeData.valid := rxMbFifo.io.deq.valid 
            rxMbFifo.io.deq.ready := txMbAfeData.ready
        }
    }
}

// class RxSideband(depth:Int, width: Int = 1, afeParams: AfeParams = AfeParams()) extends Module {
//     val io = IO(new Bundle{
//         // val sbAfeIo = new SidebandAfeIo(AfeParams())
//         val txSbAfe = Decoupled(Bits(afeParams.sbSerializerRatio.W))
//         val clk_800 = Input(Clock())
//         val rxSbIo = Input(new SidebandIo())
//     })
//     private val bw = afeParams.sbSerializerRatio
//     // private val bw = 8
//     val fifo = Module(new AsyncFifoStefan(depth, bw)) 
//     // val txMbFifos = Seq.fill(lanes)(Module (new AsyncFifoStefan(depth, BYTE)))
//     io.txSbAfe.valid := fifo.io.deq.valid 
//     io.txSbAfe.bits := fifo.io.deq.bits
//     fifo.io.deq.ready := io.txSbAfe.ready
//     fifo.io.deq_clock := clock
//     fifo.io.deq_reset := reset
    
//     withClock(io.clk_800){
//         val shiftReg = RegInit(0.U(bw.W))
//         val shiftReg_pipe_1 = RegNext(shiftReg)
//         val shiftReg_xor = WireInit(0.U(bw.W))

//         val rxCounter = RegInit(0.U(log2Ceil(bw).W))
//         val rxCounter_pipe_1 = RegNext(rxCounter)
//         val rxCounter_pipe_2 = RegNext(rxCounter_pipe_1)
//         val fifo_enq_valid_pipe_1 = RegNext(rxCounter_pipe_2 === (bw-1).U && rxCounter_pipe_1 === 0.U) 

//         val data_pipe_1 = RegNext(io.rxSbIo.data)
//         val data_pipe_2 = RegNext(data_pipe_1)
//         val clock_pipe_1 = RegNext(io.rxSbIo.clk)
//         val clock_pipe_2 = RegNext(clock_pipe_1)
//         val enable = WireInit(false.B)
//         val enable_counter = RegInit(0.U(2.W))

//         enable := clock_pipe_1.asBool ^ clock_pipe_2.asBool
//         shiftReg_xor := shiftReg ^ shiftReg_pipe_1

//         when(enable){
//             enable_counter := 2.U
//         }.elsewhen(enable_counter > 0.U){
//             enable_counter := enable_counter - 1.U
//         }

//         fifo.io.enq.valid := false.B 
//         fifo.io.enq.bits := 0.U
//         fifo.io.enq_clock := io.clk_800
//         fifo.io.enq_reset := reset
//         when(enable_counter.orR){
//             when(rxCounter_pipe_2 === (bw-1).U){
//                 shiftReg := 0.U | (data_pipe_2 << (bw-1).U)
//                 shiftReg_pipe_1 := shiftReg
//             }.otherwise{
//                 shiftReg := (shiftReg >> 1.U) | (data_pipe_2 << (bw-1).U)
//                 shiftReg_pipe_1 := 0.U
//             }
//             rxCounter := rxCounter + 1.U
//         }
//         when((rxCounter_pipe_2 === (bw-1).U && rxCounter_pipe_1 === 0.U)
//         ^ fifo_enq_valid_pipe_1) {
//                 fifo.io.enq.valid := true.B
//                 fifo.io.enq.bits := shiftReg_xor 
//         }

//     }
// }

// class TxSideband(depth:Int, width: Int = 1, afeParams: AfeParams=AfeParams()) extends Module {
//     val io = IO(new Bundle {
//         // sbAfeIo bw 64 bit
//         // val sbAfeIo = new SidebandAfeIo(AfeParams())
//         val rxSbAfe = Flipped(Decoupled(Bits(afeParams.sbSerializerRatio.W)))
//         val clk_800 = Input(Clock())
//         val txSbIo = Output(new SidebandIo())
//     })
//     private val bw = afeParams.sbSerializerRatio
//     val fifo = Module(new AsyncFifoStefan(depth, bw))

//     io.txSbIo.clk := io.clk_800

//     fifo.io.enq.valid := io.rxSbAfe.valid
//     fifo.io.enq.bits := io.rxSbAfe.bits
//     io.rxSbAfe.ready := fifo.io.enq.ready
//     fifo.io.enq_clock := clock
//     fifo.io.enq_reset := reset
    
//     withClock(io.clk_800){
//         val txCounter = RegInit(0.U(log2Ceil(64).W))
//         val txIng = RegInit(false.B) // 0 is idle for 32 UI, 1 is transmitting 64 UI
//         val shiftReg = RegInit(0.U(bw.W))

//         fifo.io.deq.ready := false.B // always set to true for io.deq
//         fifo.io.deq_clock := io.clk_800 
//         fifo.io.deq_reset := reset 
//         // when(fifo.io.deq.ready){
//         when(txCounter === 31.U && txIng === false.B){
//             shiftReg := fifo.io.deq.bits
//             fifo.io.deq.ready := true.B 
//             txIng := true.B
//             txCounter := 0.U
//         }.elsewhen(txCounter === 63.U && txIng === true.B){
//             txIng := false.B
//             txCounter := 0.U
//         }.otherwise{
//             txCounter := txCounter + 1.U
//         }
//         when(txIng === true.B){
//             shiftReg := shiftReg >> 1.U
//         }
//         io.txSbIo.data := shiftReg(0)
//         io.txSbIo.clk := Mux(txIng, io.clk_800.asBool, false.B).asClock
//     }
// }

class PhyTest extends Module {
    val io = IO(new Bundle {
        val tx_user = new MainbandAfeIo(AfeParams())
        val rx_user = new MainbandAfeIo(AfeParams())
        val clkp = Input(Clock())
        val clkn = Input(Clock())
        // val startDeq = Input(Bool())
        // val startEnq = Input(Bool())
        val clkn_out = Output(Clock())
    })


    val sender = Module(new TxMainband(AfeParams(), AsyncQueueParams()))
    val receiver = Module(new RxMainband(AfeParams(), AsyncQueueParams()))
    sender.io.rxMbAfe <> io.tx_user.rxData 
    sender.io.txMbIo  <> receiver.io.rxMbIo 
    sender.io.clkp := io.clkp 
    sender.io.clkn := io.clkn 
    sender.io.track := 0.U 

    // sender.io.startDeq := io.startDeq 

    receiver.io.txMbAfe <> io.rx_user.txData 
    // receiver.io.startEnq := io.startEnq
    io.clkn_out := receiver.io.clkn_out

    io.tx_user.txData.bits := Seq.fill(16)(0.U) 
    io.tx_user.txData.valid := false.B 
    io.rx_user.rxData.ready := false.B

    io.tx_user.txFreqSel := SpeedMode.speed16 
    io.tx_user.rxEn := false.B 
    io.rx_user.txFreqSel := SpeedMode.speed16  
    io.rx_user.rxEn := false.B
}

class MbAfe (afeParams: AfeParams, queueParams: AsyncQueueParams) extends Module {
    val io = IO(new Bundle {
        val mbAfeIo = new MainbandAfeIo(AfeParams())
        val sbAfeIo = new SidebandAfeIo(AfeParams())
        val stdIo = new StandardPackageIo()
        // The following differential clock comes from pll
        val clkp = Input(Clock())
        val clkn = Input(Clock())
        val clk_800 = Input(Clock())
    })


// This module accepts data from analog and send to adapter
// class RxMainband(afeParams: AfeParams, queueParams: AsyncQueueParams, BYTE: Int = 8) extends Module {
    val txMainband = Module(new TxMainband(afeParams, queueParams))
    val rxMainband = Module(new RxMainband(afeParams, queueParams))
    // val txSideband = Module(new TxSideband(depth))
    // val rxSideband = Module(new RxSideband(depth))

    // txMainband
    txMainband.io.rxMbAfe <> io.mbAfeIo.rxData 
    io.stdIo.tx.mainband := txMainband.io.txMbIo
    txMainband.io.clkp := io.clkp 
    txMainband.io.clkn := io.clkn
    txMainband.io.track := false.B 
    
    rxMainband.io.txMbAfe <> io.mbAfeIo.txData
    rxMainband.io.rxMbIo := io.stdIo.rx.mainband 
    io.stdIo.tx.sideband.data := 0.U
    io.sbAfeIo.rxEn := false.B
    io.mbAfeIo.txFreqSel := SpeedMode.speed16 
    io.sbAfeIo.txData := 0.U
    io.mbAfeIo.rxEn := false.B
    io.sbAfeIo.txClock := false.B
    io.stdIo.tx.sideband.clk := clock
}

// To execute do:
// runMain edu.berkeley.cs.ucie.digital.afe.TxMainbandVerilog 
object TxMainbandVerilog extends App {
    (new ChiselStage).emitSystemVerilog(new TxMainband(AfeParams(), AsyncQueueParams())
    )
}

// To execute do:
// runMain edu.berkeley.cs.ucie.digital.afe.TxMainbandVerilog 
object RxMainbandVerilog extends App {
    (new ChiselStage).emitSystemVerilog(new RxMainband(AfeParams(), AsyncQueueParams())
    )
}

object PhyTestVerilog extends App {
    (new ChiselStage).emitSystemVerilog(new PhyTest())
}

// object TxSidebandVerilog extends App {
//     (new ChiselStage).emitSystemVerilog(new TxSideband(16))
// }


// object RxSidebandVerilog extends App {
//     (new ChiselStage).emitSystemVerilog(new RxSideband(16))
// }


object MbAfeVerilog extends App {
    (new ChiselStage).emitSystemVerilog(new MbAfe(AfeParams(), AsyncQueueParams()))
}