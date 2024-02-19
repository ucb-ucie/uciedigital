package edu.berkeley.cs.ucie.digital
package logphy

import chisel3._
import chisel3.util._
import interfaces._

class SBMsgWrapperTrainIO(
) extends Bundle {
  val msgReq = Flipped(Decoupled(new MessageRequest))
  val msgReqStatus = Decoupled(new MessageRequestStatus)
}

/** TODO: implementation */
class SBMsgWrapper(
    afeParams: AfeParams,
) extends Module {
  val io = IO(new Bundle {
    val trainIO = new SBMsgWrapperTrainIO
    val laneIO = new SidebandLaneIO(afeParams)
  })

  private object State extends ChiselEnum {
    val IDLE, EXCHANGE, REQ, RESP, WAIT_ACK_SUCCESS, WAIT_ACK_ERR = Value
  }

  private object SubState extends ChiselEnum {
    val SEND_OR_RECEIVE_MESSAGE, SEND_OR_RECEIVE_DATA = Value
  }

  private val currentState = RegInit(State.IDLE)
  private val sendSubState = RegInit(SubState.SEND_OR_RECEIVE_MESSAGE)
  private val receiveSubState = RegInit(SubState.SEND_OR_RECEIVE_MESSAGE)
  private val timeoutCounter = RegInit(0.U(64.W))

  private val nextState = Wire(currentState)
  currentState := nextState
  when(currentState =/= nextState) {
    sendSubState := SubState.SEND_OR_RECEIVE_MESSAGE
    receiveSubState := SubState.SEND_OR_RECEIVE_MESSAGE
    timeoutCounter := 0.U
  }

  private val sidebandRxWidthCoupler64 = new DataWidthCoupler(
    DataWidthCouplerParams(
      inWidth = io.laneIO.rxData.getWidth,
      outWidth = 64,
    ),
  )
  private val sidebandTxWidthCoupler64 = new DataWidthCoupler(
    DataWidthCouplerParams(
      inWidth = 64,
      outWidth = io.laneIO.txData.getWidth,
    ),
  )
  io.laneIO.rxData <> sidebandRxWidthCoupler64.io.in
  io.laneIO.txData <> sidebandTxWidthCoupler64.io.out

  private val currentReq = RegInit(0.U((new MessageRequest).msg.getWidth.W))
  private val currentReqHasData = RegInit(false.B)
  private val currentReqTimeoutMax = RegInit(0.U(64.W))

  private val dataOut = RegInit(0.U(64.W))
  io.trainIO.msgReqStatus.bits.data := dataOut
  io.trainIO.msgReqStatus.bits.status := Mux(
    currentState === State.WAIT_ACK_SUCCESS,
    MessageRequestStatusType.SUCCESS,
    MessageRequestStatusType.ERR,
  )

  switch(currentState) {
    is(State.IDLE) {
      io.trainIO.msgReq.ready := true.B
      when(io.trainIO.msgReq.fire) {
        currentReq := io.trainIO.msgReq.bits.msg
        currentReqHasData := io.trainIO.msgReq.bits.msgTypeHasData
        currentReqTimeoutMax := io.trainIO.msgReq.bits.timeoutCycles
        switch(io.trainIO.msgReq.bits.reqType) {
          is(MessageRequestType.MSG_REQ) {
            nextState := State.EXCHANGE
          }
          is(MessageRequestType.MSG_RESP) {
            nextState := State.EXCHANGE
          }
          is(MessageRequestType.MSG_EXCH) {
            nextState := State.EXCHANGE
          }
        }
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
      switch(sendSubState) {
        is(SubState.SEND_OR_RECEIVE_MESSAGE) {
          sidebandTxWidthCoupler64.io.in.valid := true.B
          sidebandTxWidthCoupler64.io.in.bits := currentReq(64, 0)
          when(sidebandTxWidthCoupler64.io.in.fire && currentReqHasData) {
            sendSubState := SubState.SEND_OR_RECEIVE_DATA
          }
        }
        is(SubState.SEND_OR_RECEIVE_DATA) {
          sidebandTxWidthCoupler64.io.in.valid := true.B
          sidebandTxWidthCoupler64.io.in.bits := currentReq(128, 64)
          when(sidebandTxWidthCoupler64.io.in.fire) {
            sendSubState := SubState.SEND_OR_RECEIVE_MESSAGE
          }
        }
      }

      /** if receive message, move on */
      switch(receiveSubState) {
        is(SubState.SEND_OR_RECEIVE_MESSAGE) {
          sidebandRxWidthCoupler64.io.out.ready := true.B
          when(sidebandRxWidthCoupler64.io.out.fire) {
            when(
              messageIsEqual(
                sidebandRxWidthCoupler64.io.out.bits,
                currentReq(64, 0),
              ),
            ) {
              when(currentReqHasData) {
                receiveSubState := SubState.SEND_OR_RECEIVE_DATA
              }.otherwise {
                nextState := State.WAIT_ACK_SUCCESS
              }
            }
          }
        }
        is(SubState.SEND_OR_RECEIVE_DATA) {
          sidebandRxWidthCoupler64.io.out.ready := true.B
          when(sidebandRxWidthCoupler64.io.out.fire) {
            dataOut := sidebandRxWidthCoupler64.io.out.bits
            nextState := State.WAIT_ACK_SUCCESS
          }
        }
      }

      /** timeout logic */
      timeoutCounter := timeoutCounter + 1
      when(timeoutCounter === currentReqTimeoutMax) {
        nextState := State.WAIT_ACK_ERR
      }

    }
    is(State.WAIT_ACK_SUCCESS) {
      io.trainIO.msgReqStatus.valid := true.B
      when(io.trainIO.msgReqStatus.fire) {
        nextState := State.IDLE
      }
    }
    is(State.WAIT_ACK_ERR) {
      io.trainIO.msgReqStatus.valid := true.B
      when(io.trainIO.msgReqStatus.fire) {
        nextState := State.IDLE
      }
    }
  }

}
