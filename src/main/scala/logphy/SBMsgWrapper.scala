package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._
import sideband.SidebandParams
import interfaces._

class SBMsgWrapperTrainIO(
) extends Bundle {
  val msgReq = Flipped(Decoupled(new MessageRequest))
  val msgReqStatus = Decoupled(new MessageRequestStatus)
}

class SBMsgWrapper(
    sbParams: SidebandParams,
) extends Module {
  val io = IO(new Bundle {
    val trainIO = new SBMsgWrapperTrainIO
    val laneIO = Flipped(new SidebandLaneIO(sbParams))
  })

  private object State extends ChiselEnum {
    val IDLE, EXCHANGE, WAIT_ACK = Value
  }

  // private object SubState extends ChiselEnum {
  //   val SEND_OR_RECEIVE_MESSAGE, SEND_OR_RECEIVE_DATA = Value
  // }

  private val currentState = RegInit(State.IDLE)
  // private val sendSubState = RegInit(SubState.SEND_OR_RECEIVE_MESSAGE)
  // private val receiveSubState = RegInit(SubState.SEND_OR_RECEIVE_MESSAGE)
  private val timeoutCounter = RegInit(0.U(64.W))

  private val nextState = WireInit(currentState)
  currentState := nextState
  private val sentMsg = RegInit(false.B)
  when(currentState =/= nextState) {
    // sendSubState := SubState.SEND_OR_RECEIVE_MESSAGE
    // receiveSubState := SubState.SEND_OR_RECEIVE_MESSAGE
    timeoutCounter := 0.U
    sentMsg := false.B
  }

  private val currentReq = RegInit(0.U((new MessageRequest).msg.getWidth.W))
  // private val currentReqHasData = RegInit(false.B)
  private val currentReqTimeoutMax = RegInit(0.U(64.W))
  private val currentStatus = RegInit(MessageRequestStatusType.ERR)

  private val dataOut = RegInit(0.U(64.W))
  io.trainIO.msgReqStatus.bits.data := dataOut
  io.trainIO.msgReqStatus.bits.status := currentStatus
  io.laneIO.rxData.nodeq()
  io.laneIO.txData.noenq()
  io.trainIO.msgReqStatus.noenq()
  io.trainIO.msgReq.nodeq()

  switch(currentState) {
    is(State.IDLE) {
      io.trainIO.msgReq.ready := true.B
      when(io.trainIO.msgReq.fire) {
        currentReq := io.trainIO.msgReq.bits.msg
        // currentReqHasData := io.trainIO.msgReq.bits.msgTypeHasData
        currentReqTimeoutMax := io.trainIO.msgReq.bits.timeoutCycles
        nextState := State.EXCHANGE
        // switch(io.trainIO.msgReq.bits.reqType) {
        //   is(MessageRequestType.MSG_REQ) {
        //     nextState := State.EXCHANGE
        //   }
        //   is(MessageRequestType.MSG_RESP) {
        //     nextState := State.EXCHANGE
        //   }
        //   is(MessageRequestType.MSG_EXCH) {
        //     nextState := State.EXCHANGE
        //   }
        // }
      }
    }
    is(State.EXCHANGE) {

      def messageIsEqual(m1: UInt, m2: UInt): Bool = {

        /** opcode */
        (m1(4, 0) === m2(4, 0)) &&
        /** subcode */
        (m1(21, 14) === m2(21, 14)) &&
        /** code */
        (m1(39, 32) === m2(39, 32))
      }

      /** send message over sideband */
      io.laneIO.txData.valid := true.B
      io.laneIO.txData.bits := currentReq
      val hasSentMsg = WireInit(io.laneIO.txData.fire || sentMsg)
      sentMsg := hasSentMsg

      /** if receive message, move on */
      io.laneIO.rxData.ready := true.B
      when(io.laneIO.rxData.fire) {
        when(
          messageIsEqual(
            io.laneIO.rxData.bits(64, 0),
            currentReq(64, 0),
          ) && hasSentMsg,
        ) {
          dataOut := io.laneIO.rxData.bits(127, 64)
          currentStatus := MessageRequestStatusType.SUCCESS
          nextState := State.WAIT_ACK
        }
      }

      // switch(sendSubState) {
      //   is(SubState.SEND_OR_RECEIVE_MESSAGE) {
      //     sidebandTxWidthCoupler64.io.in.valid := true.B
      //     sidebandTxWidthCoupler64.io.in.bits := currentReq(64, 0)
      //     when(sidebandTxWidthCoupler64.io.in.fire && currentReqHasData) {
      //       sendSubState := SubState.SEND_OR_RECEIVE_DATA
      //     }
      //   }
      //   is(SubState.SEND_OR_RECEIVE_DATA) {
      //     sidebandTxWidthCoupler64.io.in.valid := true.B
      //     sidebandTxWidthCoupler64.io.in.bits := currentReq(128, 64)
      //     when(sidebandTxWidthCoupler64.io.in.fire) {
      //       sendSubState := SubState.SEND_OR_RECEIVE_MESSAGE
      //     }
      //   }
      // }

      // switch(receiveSubState) {
      //   is(SubState.SEND_OR_RECEIVE_MESSAGE) {
      //     sidebandRxWidthCoupler64.io.out.ready := true.B
      //     when(sidebandRxWidthCoupler64.io.out.fire) {
      //       when(
      //         messageIsEqual(
      //           sidebandRxWidthCoupler64.io.out.bits,
      //           currentReq(64, 0),
      //         ),
      //       ) {
      //         when(currentReqHasData) {
      //           receiveSubState := SubState.SEND_OR_RECEIVE_DATA
      //         }.otherwise {
      //           nextState := State.WAIT_ACK_SUCCESS
      //         }
      //       }
      //     }
      //   }
      //   is(SubState.SEND_OR_RECEIVE_DATA) {
      //     sidebandRxWidthCoupler64.io.out.ready := true.B
      //     when(sidebandRxWidthCoupler64.io.out.fire) {
      //       dataOut := sidebandRxWidthCoupler64.io.out.bits
      //       nextState := State.WAIT_ACK_SUCCESS
      //     }
      //   }
      // }

      /** timeout logic */
      timeoutCounter := timeoutCounter + 1.U
      when(timeoutCounter === currentReqTimeoutMax) {
        nextState := State.WAIT_ACK
        currentStatus := MessageRequestStatusType.ERR
      }

    }
    is(State.WAIT_ACK) {
      io.trainIO.msgReqStatus.valid := true.B
      when(io.trainIO.msgReqStatus.fire) {
        nextState := State.IDLE
      }
    }
  }

}
