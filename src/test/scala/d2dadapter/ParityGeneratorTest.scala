package edu.berkeley.cs.ucie.digital
package d2dadapter

import chisel3._
//import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import interfaces._

class ParityGeneratorTest extends AnyFlatSpec with ChiselScalatestTester {
    val fdiParams = new FdiParams(width = 8, dllpWidth = 8, sbWidth = 32)
    behavior of "ParityGenerator"
    it should "generate correct parity data" in {
        
        test(new ParityGenerator(fdiParams)) { c => 
            // prepare random data generator
            println("Test started")
            val seed: Int = 0
            val rand = new scala.util.Random(seed)
 
            val parityDataGold: Array[Long] = Array.fill(ParityAmount.PARITY_DATA_NBYTE_1)(0)
            // init
            c.io.parity_tx_enable.poke(true.B)
            c.io.parity_n.poke(ParityN.ONE)
            c.io.rdi_state.poke(PhyState.active)
            c.io.parity_rdy.poke(false.B)
            c.io.snd_data_vld.poke(false.B)
            c.io.rcv_data_vld.poke(false.B)
            c.clock.step(1)
            println("Send data")
            // start sending data
            for( i <- 0 until ParityAmount.DATA_NBYTE_1 / fdiParams.width){
                c.io.parity_insert.expect(false.B)
                for(j <- 0 until fdiParams.width){
                    val data = rand.nextLong(256)
                    c.io.snd_data(j).poke(data.U)
                    println("i" + i + " j " + j)
                    parityDataGold((i * fdiParams.width + j) % ParityAmount.PARITY_DATA_NBYTE_1) = parityDataGold((i * fdiParams.width + j) % ParityAmount.PARITY_DATA_NBYTE_1) ^ data
                }
                c.io.snd_data_vld.poke(false.B)
                c.io.parity_insert.expect(false.B)
                c.clock.step(2)
                c.io.snd_data_vld.poke(true.B)
                c.io.parity_insert.expect(false.B)
                c.clock.step(1)
                c.io.snd_data_vld.poke(false.B)
                c.clock.step(2)
            }
            println("Start Insert Parity")
            c.io.parity_insert.expect(true.B)
            for( i <- 0 until ParityAmount.PARITY_DATA_NBYTE_1 / fdiParams.width){
                c.clock.step(2)
                c.io.parity_insert.expect(true.B)
                c.clock.step(2)
                c.io.parity_insert.expect(true.B)
                for(j <- 0 until fdiParams.width){
                    // get the parity
                    var parity: Long = 0
                    for(c <- 0 until 64){
                        parity = parity ^ ((parityDataGold(i * fdiParams.width + j % ParityAmount.PARITY_DATA_NBYTE_1) & (1 << c)) >>> c)
                    }
                    c.io.parity_data(j).expect(parity.U)
                }
                c.io.parity_rdy.poke(true.B)
                c.clock.step(1)
                c.io.parity_rdy.poke(false.B)
                c.clock.step(1)
            }
        }
    }

    it should "get correct parity result" in {
        
        test(new ParityGenerator(fdiParams)) { c => 
            // prepare random data generator
            println("Test started")
            val seed: Int = 0
            val rand = new scala.util.Random(seed)
 


            val parityDataGold: Array[Long] = Array.fill(ParityAmount.PARITY_DATA_NBYTE_1)(0)
            // init
            c.io.parity_rx_enable.poke(true.B)
            c.io.parity_n.poke(ParityN.ONE)
            c.io.rdi_state.poke(PhyState.active)
            c.io.parity_rdy.poke(false.B)
            c.io.snd_data_vld.poke(false.B)
            c.io.rcv_data_vld.poke(false.B)
            c.clock.step(1)
            // start sending data
            println("Send data")
            for( i <- 0 until ParityAmount.DATA_NBYTE_1 / fdiParams.width){
                c.io.parity_check.expect(false.B)
                for(j <- 0 until fdiParams.width){
                    val data = rand.nextLong(256)
                    c.io.rcv_data(j).poke(data.U)
                    println("i" + i + " j " + j)
                    parityDataGold((i * fdiParams.width + j) % ParityAmount.PARITY_DATA_NBYTE_1) = parityDataGold((i * fdiParams.width + j) % ParityAmount.PARITY_DATA_NBYTE_1) ^ data
                }
                c.io.rcv_data_vld.poke(false.B)
                c.io.parity_check.expect(false.B)
                c.clock.step(2)
                c.io.rcv_data_vld.poke(true.B)
                c.io.parity_check.expect(false.B)
                c.clock.step(1)
                c.io.rcv_data_vld.poke(false.B)
                c.clock.step(1)
            }
            c.io.parity_check.expect(true.B)
            for( i <- 0 until ParityAmount.PARITY_DATA_NBYTE_1 / fdiParams.width){
                c.clock.step(2)
                c.io.parity_check.expect(true.B)
                c.clock.step(2)
                c.io.parity_check.expect(true.B)
                for(j <- 0 until fdiParams.width){
                    // get the parity
                    var parity: Long = 0
                    for(c <- 0 until 64){
                        parity = parity ^ ((parityDataGold(i * fdiParams.width + j % ParityAmount.PARITY_DATA_NBYTE_1) & (1 << c)) >>> c)
                    }
                    c.io.rcv_data(j).poke(parity.U)
                }
                c.io.rcv_data_vld.poke(true.B)
                c.clock.step(1)
                c.io.rcv_data_vld.poke(false.B)
                c.clock.step(1)
            }
            c.io.parity_check.expect(false.B)
            for( i <- 0 until ParityAmount.PARITY_DATA_NBYTE_1){
                c.io.parity_check_result(i).expect(false.B)
            }
            
        }
    }
}
