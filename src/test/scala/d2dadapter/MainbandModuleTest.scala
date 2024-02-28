package ucie.d2dadapter

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class MainbandModuleTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "MainbandModule"
    it should "pass mainband data from fdi to rdi" in {
        val params = new D2DAdapterParams()
        test(new MainbandModule(params)) { c => 
            // prepare random data generator
            println("Test started")
            val seed: Int = 0
            val rand = new scala.util.Random(seed)

            val data_gold: Array[Long] = Array.fill(params.NBYTES)(0)
            // init
            c.io.fdi_lp_irdy.poke(false.B)
            c.io.fdi_lp_valid.poke(false.B)
            c.io.parity_insert.poke(false.B)
            c.io.rdi_pl_trdy.poke(false.B)
            c.io.d2d_state.poke(InterfaceStatus.ACTIVE)
            c.io.mainband_stallreq.poke(false.B)
            c.clock.step(1)
            // ensure no absurd data goes to rdi
            for(i <- 0 until 10){
                c.io.rdi_lp_valid.expect(false.B)
                c.io.rdi_lp_irdy.expect(false.B)
                c.clock.step(1)
            }

            // fdi send a data to mainband
            println("Send data")
            // start sending data
            for(j <- 0 until params.NBYTES){
                val data = rand.nextLong(256)
                c.io.fdi_lp_data(j).poke(data.U)
                data_gold(j) = data
            }
            c.io.fdi_lp_irdy.poke(true.B)
            c.io.fdi_lp_valid.poke(true.B)

            while(c.io.fdi_pl_trdy.peek().litValue != true.B.litValue){
                c.io.rdi_lp_valid.expect(false.B)
                c.io.rdi_lp_irdy.expect(false.B)
                c.clock.step(1)
            }
            // data taken by mainband module // check when the data is to be sent
            while(c.io.rdi_lp_valid.peek().litValue != true.B.litValue || c.io.rdi_lp_irdy.peek().litValue != true.B.litValue ){
                c.clock.step(1)
            }    
            // check if the data match the original one
            c.io.rdi_pl_trdy.poke(true.B)
            for(j <- 0 until params.NBYTES){
                c.io.rdi_lp_data(j).expect(data_gold(j).U)
            }
        }
    }

    it should "pass parity data from to rdi" in {
        val params = new D2DAdapterParams()
        test(new MainbandModule(params)) { c => 
            // prepare random data generator
            println("Test started")
            val seed: Int = 0
            val rand = new scala.util.Random(seed)

            val data_gold: Array[Long] = Array.fill(params.NBYTES)(0)
            // init
            c.io.fdi_lp_irdy.poke(false.B)
            c.io.fdi_lp_valid.poke(false.B)
            c.io.parity_check.poke(false.B)
            c.io.d2d_state.poke(InterfaceStatus.ACTIVE)
            c.io.mainband_stallreq.poke(false.B)
            c.clock.step(1)
            // ensure no absurd data goes to rdi
            for(i <- 0 until 10){
                c.io.rdi_lp_valid.expect(false.B)
                c.io.rdi_lp_irdy.expect(false.B)
                c.clock.step(1)
            }

            // parity send data
            println("Send data")
            // start sending data
            for(j <- 0 until params.NBYTES){
                val data = rand.nextLong(256)
                c.io.parity_data(j).poke(data.U)
                data_gold(j) = data
            }
            c.io.parity_insert.poke(true.B)

            // data taken by mainband module // check when the data is to be sent
            while(c.io.rdi_lp_valid.peek().litValue != true.B.litValue || c.io.rdi_lp_irdy.peek().litValue != true.B.litValue ){
                c.clock.step(1)
            }    
            // check if the data match the original one
            c.io.rdi_pl_trdy.poke(true.B)
            for(j <- 0 until params.NBYTES){
                c.io.rdi_lp_data(j).expect(data_gold(j).U)
            }
        }
    }

    it should "pass mainband data from rdi to fdi" in {
        val params = new D2DAdapterParams()
        test(new MainbandModule(params)) { c => 
            // prepare random data generator
            println("Test started")
            val seed: Int = 0
            val rand = new scala.util.Random(seed)

            val data_gold: Array[Long] = Array.fill(params.NBYTES)(0)
            // init
            c.io.fdi_lp_irdy.poke(false.B)
            c.io.fdi_lp_valid.poke(false.B)
            c.io.parity_check.poke(false.B)
            c.io.d2d_state.poke(InterfaceStatus.ACTIVE)
            c.io.mainband_stallreq.poke(false.B)
            c.clock.step(1)
            // ensure no absurd data goes to fdi
            for(i <- 0 until 10){
                c.io.fdi_pl_valid.expect(false.B)
                c.clock.step(1)
            }

            // rdi send a data to mainband
            println("Send data")
            // start sending data
            for(j <- 0 until params.NBYTES){
                val data = rand.nextLong(256)
                c.io.rdi_pl_data(j).poke(data.U)
                data_gold(j) = data
            }
            c.io.rdi_pl_valid.poke(true.B)
            c.clock.step(1)
            // data taken by mainband module // check when the data is to be sent
            while(c.io.fdi_pl_valid.peek().litValue != true.B.litValue){
                c.clock.step(1)
            }    
            // check if the data match the original one
            for(j <- 0 until params.NBYTES){
                c.io.fdi_pl_data(j).expect(data_gold(j).U)
            }
        }
    }

    it should "not pass parity data from rdi to fdi" in {
        val params = new D2DAdapterParams()
        test(new MainbandModule(params)) { c => 
            // prepare random data generator
            println("Test started")
            val seed: Int = 0
            val rand = new scala.util.Random(seed)

            val data_gold: Array[Long] = Array.fill(params.NBYTES)(0)
            // init
            c.io.fdi_lp_irdy.poke(false.B)
            c.io.fdi_lp_valid.poke(false.B)
            c.io.parity_check.poke(false.B)
            c.io.d2d_state.poke(InterfaceStatus.ACTIVE)
            c.io.mainband_stallreq.poke(false.B)
            c.clock.step(1)
            // ensure no absurd data goes to fdi
            for(i <- 0 until 10){
                c.io.fdi_pl_valid.expect(false.B)
                c.clock.step(1)
            }

            // rdi send a data to mainband
            println("Send data")
            // start sending data
            for(j <- 0 until params.NBYTES){
                val data = rand.nextLong(256)
                c.io.rdi_pl_data(j).poke(data.U)
                data_gold(j) = data
            }
            c.io.parity_check.poke(true.B)
            c.io.rdi_pl_valid.poke(true.B)
            c.clock.step(1)
            for(i <- 0 until 10){
                c.io.fdi_pl_valid.expect(false.B)
                c.clock.step(1)
            }
            // send another data
            for(j <- 0 until params.NBYTES){
                val data = rand.nextLong(256)
                c.io.rdi_pl_data(j).poke(data.U)
                data_gold(j) = data
            }
            c.io.fdi_pl_valid.expect(false.B)
            c.io.parity_check.poke(false.B)
            c.io.rdi_pl_valid.poke(true.B)            
            // data taken by mainband module // check when the data is to be sent

            while(c.io.fdi_pl_valid.peek().litValue != true.B.litValue){
                c.clock.step(1)
            }    
            // check if the data match the original one
            for(j <- 0 until params.NBYTES){
                c.io.fdi_pl_data(j).expect(data_gold(j).U)
            }
        }
    }
}
