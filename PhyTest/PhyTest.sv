`timescale 1ns/100ps

module AsyncResetSynchronizerPrimitiveShiftReg_d3_i0(
  input   clock,
  input   reset,
  input   io_d, // @[src/main/scala/util/ShiftReg.scala 36:14]
  output  io_q // @[src/main/scala/util/ShiftReg.scala 36:14]
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
`endif // RANDOMIZE_REG_INIT
  reg  sync_0; // @[src/main/scala/util/SynchronizerReg.scala 51:87]
  reg  sync_1; // @[src/main/scala/util/SynchronizerReg.scala 51:87]
  reg  sync_2; // @[src/main/scala/util/SynchronizerReg.scala 51:87]
  assign io_q = sync_0; // @[src/main/scala/util/SynchronizerReg.scala 59:8]
  always @(posedge clock or posedge reset) begin
    if (reset) begin // @[src/main/scala/util/SynchronizerReg.scala 51:87]
      sync_0 <= 1'h0; // @[src/main/scala/util/SynchronizerReg.scala 51:87]
    end else begin
      sync_0 <= sync_1; // @[src/main/scala/util/SynchronizerReg.scala 57:10]
    end
  end
  always @(posedge clock or posedge reset) begin
    if (reset) begin // @[src/main/scala/util/SynchronizerReg.scala 51:87]
      sync_1 <= 1'h0; // @[src/main/scala/util/SynchronizerReg.scala 51:87]
    end else begin
      sync_1 <= sync_2; // @[src/main/scala/util/SynchronizerReg.scala 57:10]
    end
  end
  always @(posedge clock or posedge reset) begin
    if (reset) begin // @[src/main/scala/util/SynchronizerReg.scala 54:22]
      sync_2 <= 1'h0;
    end else begin
      sync_2 <= io_d;
    end
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  sync_0 = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  sync_1 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  sync_2 = _RAND_2[0:0];
`endif // RANDOMIZE_REG_INIT
  if (reset) begin
    sync_0 = 1'h0;
  end
  if (reset) begin
    sync_1 = 1'h0;
  end
  if (reset) begin
    sync_2 = 1'h0;
  end
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module AsyncResetSynchronizerShiftReg_w4_d3_i0(
  input        clock,
  input        reset,
  input  [3:0] io_d, // @[src/main/scala/util/ShiftReg.scala 36:14]
  output [3:0] io_q // @[src/main/scala/util/ShiftReg.scala 36:14]
);
  wire  output_chain_clock; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_reset; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_io_d; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_io_q; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_1_clock; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_1_reset; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_1_io_d; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_1_io_q; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_2_clock; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_2_reset; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_2_io_d; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_2_io_q; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_3_clock; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_3_reset; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_3_io_d; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_3_io_q; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_1 = output_chain_1_io_q; // @[src/main/scala/util/ShiftReg.scala 48:{24,24}]
  wire  output_0 = output_chain_io_q; // @[src/main/scala/util/ShiftReg.scala 48:{24,24}]
  wire [1:0] io_q_lo = {output_1,output_0}; // @[src/main/scala/util/SynchronizerReg.scala 90:14]
  wire  output_3 = output_chain_3_io_q; // @[src/main/scala/util/ShiftReg.scala 48:{24,24}]
  wire  output_2 = output_chain_2_io_q; // @[src/main/scala/util/ShiftReg.scala 48:{24,24}]
  wire [1:0] io_q_hi = {output_3,output_2}; // @[src/main/scala/util/SynchronizerReg.scala 90:14]
  AsyncResetSynchronizerPrimitiveShiftReg_d3_i0 output_chain ( // @[src/main/scala/util/ShiftReg.scala 45:23]
    .clock(output_chain_clock),
    .reset(output_chain_reset),
    .io_d(output_chain_io_d),
    .io_q(output_chain_io_q)
  );
  AsyncResetSynchronizerPrimitiveShiftReg_d3_i0 output_chain_1 ( // @[src/main/scala/util/ShiftReg.scala 45:23]
    .clock(output_chain_1_clock),
    .reset(output_chain_1_reset),
    .io_d(output_chain_1_io_d),
    .io_q(output_chain_1_io_q)
  );
  AsyncResetSynchronizerPrimitiveShiftReg_d3_i0 output_chain_2 ( // @[src/main/scala/util/ShiftReg.scala 45:23]
    .clock(output_chain_2_clock),
    .reset(output_chain_2_reset),
    .io_d(output_chain_2_io_d),
    .io_q(output_chain_2_io_q)
  );
  AsyncResetSynchronizerPrimitiveShiftReg_d3_i0 output_chain_3 ( // @[src/main/scala/util/ShiftReg.scala 45:23]
    .clock(output_chain_3_clock),
    .reset(output_chain_3_reset),
    .io_d(output_chain_3_io_d),
    .io_q(output_chain_3_io_q)
  );
  assign io_q = {io_q_hi,io_q_lo}; // @[src/main/scala/util/SynchronizerReg.scala 90:14]
  assign output_chain_clock = clock;
  assign output_chain_reset = reset; // @[src/main/scala/util/SynchronizerReg.scala 86:21]
  assign output_chain_io_d = io_d[0]; // @[src/main/scala/util/SynchronizerReg.scala 87:41]
  assign output_chain_1_clock = clock;
  assign output_chain_1_reset = reset; // @[src/main/scala/util/SynchronizerReg.scala 86:21]
  assign output_chain_1_io_d = io_d[1]; // @[src/main/scala/util/SynchronizerReg.scala 87:41]
  assign output_chain_2_clock = clock;
  assign output_chain_2_reset = reset; // @[src/main/scala/util/SynchronizerReg.scala 86:21]
  assign output_chain_2_io_d = io_d[2]; // @[src/main/scala/util/SynchronizerReg.scala 87:41]
  assign output_chain_3_clock = clock;
  assign output_chain_3_reset = reset; // @[src/main/scala/util/SynchronizerReg.scala 86:21]
  assign output_chain_3_io_d = io_d[3]; // @[src/main/scala/util/SynchronizerReg.scala 87:41]
endmodule
module AsyncResetSynchronizerShiftReg_w1_d3_i0(
  input   clock,
  input   reset,
  input   io_d, // @[src/main/scala/util/ShiftReg.scala 36:14]
  output  io_q // @[src/main/scala/util/ShiftReg.scala 36:14]
);
  wire  output_chain_clock; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_reset; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_io_d; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  output_chain_io_q; // @[src/main/scala/util/ShiftReg.scala 45:23]
  AsyncResetSynchronizerPrimitiveShiftReg_d3_i0 output_chain ( // @[src/main/scala/util/ShiftReg.scala 45:23]
    .clock(output_chain_clock),
    .reset(output_chain_reset),
    .io_d(output_chain_io_d),
    .io_q(output_chain_io_q)
  );
  assign io_q = output_chain_io_q; // @[src/main/scala/util/ShiftReg.scala 48:{24,24}]
  assign output_chain_clock = clock;
  assign output_chain_reset = reset; // @[src/main/scala/util/SynchronizerReg.scala 86:21]
  assign output_chain_io_d = io_d; // @[src/main/scala/util/SynchronizerReg.scala 87:41]
endmodule
module AsyncValidSync(
  input   io_in, // @[src/main/scala/util/AsyncQueue.scala 59:14]
  output  io_out, // @[src/main/scala/util/AsyncQueue.scala 59:14]
  input   clock, // @[src/main/scala/util/AsyncQueue.scala 63:17]
  input   reset // @[src/main/scala/util/AsyncQueue.scala 64:17]
);
  wire  io_out_source_valid_0_clock; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  io_out_source_valid_0_reset; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  io_out_source_valid_0_io_d; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  io_out_source_valid_0_io_q; // @[src/main/scala/util/ShiftReg.scala 45:23]
  AsyncResetSynchronizerShiftReg_w1_d3_i0 io_out_source_valid_0 ( // @[src/main/scala/util/ShiftReg.scala 45:23]
    .clock(io_out_source_valid_0_clock),
    .reset(io_out_source_valid_0_reset),
    .io_d(io_out_source_valid_0_io_d),
    .io_q(io_out_source_valid_0_io_q)
  );
  assign io_out = io_out_source_valid_0_io_q; // @[src/main/scala/util/ShiftReg.scala 48:{24,24}]
  assign io_out_source_valid_0_clock = clock;
  assign io_out_source_valid_0_reset = reset;
  assign io_out_source_valid_0_io_d = io_in; // @[src/main/scala/util/ShiftReg.scala 47:16]
endmodule
module AsyncQueueSource(
  input         clock,
  input         reset,
  output        io_enq_ready, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  input         io_enq_valid, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  input  [15:0] io_enq_bits, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  output [15:0] io_async_mem_0, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  output [15:0] io_async_mem_1, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  output [15:0] io_async_mem_2, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  output [15:0] io_async_mem_3, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  output [15:0] io_async_mem_4, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  output [15:0] io_async_mem_5, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  output [15:0] io_async_mem_6, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  output [15:0] io_async_mem_7, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  input  [3:0]  io_async_ridx, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  output [3:0]  io_async_widx, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  input         io_async_safe_ridx_valid, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  output        io_async_safe_widx_valid, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  output        io_async_safe_source_reset_n, // @[src/main/scala/util/AsyncQueue.scala 71:14]
  input         io_async_safe_sink_reset_n // @[src/main/scala/util/AsyncQueue.scala 71:14]
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_10;
`endif // RANDOMIZE_REG_INIT
  wire  ridx_ridx_gray_clock; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  ridx_ridx_gray_reset; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire [3:0] ridx_ridx_gray_io_d; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire [3:0] ridx_ridx_gray_io_q; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  source_valid_0_io_in; // @[src/main/scala/util/AsyncQueue.scala 100:32]
  wire  source_valid_0_io_out; // @[src/main/scala/util/AsyncQueue.scala 100:32]
  wire  source_valid_0_clock; // @[src/main/scala/util/AsyncQueue.scala 100:32]
  wire  source_valid_0_reset; // @[src/main/scala/util/AsyncQueue.scala 100:32]
  wire  source_valid_1_io_in; // @[src/main/scala/util/AsyncQueue.scala 101:32]
  wire  source_valid_1_io_out; // @[src/main/scala/util/AsyncQueue.scala 101:32]
  wire  source_valid_1_clock; // @[src/main/scala/util/AsyncQueue.scala 101:32]
  wire  source_valid_1_reset; // @[src/main/scala/util/AsyncQueue.scala 101:32]
  wire  sink_extend_io_in; // @[src/main/scala/util/AsyncQueue.scala 103:30]
  wire  sink_extend_io_out; // @[src/main/scala/util/AsyncQueue.scala 103:30]
  wire  sink_extend_clock; // @[src/main/scala/util/AsyncQueue.scala 103:30]
  wire  sink_extend_reset; // @[src/main/scala/util/AsyncQueue.scala 103:30]
  wire  sink_valid_io_in; // @[src/main/scala/util/AsyncQueue.scala 104:30]
  wire  sink_valid_io_out; // @[src/main/scala/util/AsyncQueue.scala 104:30]
  wire  sink_valid_clock; // @[src/main/scala/util/AsyncQueue.scala 104:30]
  wire  sink_valid_reset; // @[src/main/scala/util/AsyncQueue.scala 104:30]
  reg [15:0] mem_0; // @[src/main/scala/util/AsyncQueue.scala 80:16]
  reg [15:0] mem_1; // @[src/main/scala/util/AsyncQueue.scala 80:16]
  reg [15:0] mem_2; // @[src/main/scala/util/AsyncQueue.scala 80:16]
  reg [15:0] mem_3; // @[src/main/scala/util/AsyncQueue.scala 80:16]
  reg [15:0] mem_4; // @[src/main/scala/util/AsyncQueue.scala 80:16]
  reg [15:0] mem_5; // @[src/main/scala/util/AsyncQueue.scala 80:16]
  reg [15:0] mem_6; // @[src/main/scala/util/AsyncQueue.scala 80:16]
  reg [15:0] mem_7; // @[src/main/scala/util/AsyncQueue.scala 80:16]
  wire  _widx_T_1 = io_enq_ready & io_enq_valid; // @[src/main/scala/chisel3/util/Decoupled.scala 52:35]
  wire  sink_ready = sink_valid_io_out; // @[src/main/scala/util/AsyncQueue.scala 120:16 79:28]
  wire  _widx_T_2 = ~sink_ready; // @[src/main/scala/util/AsyncQueue.scala 81:77]
  reg [3:0] widx_widx_bin; // @[src/main/scala/util/AsyncQueue.scala 52:25]
  wire [3:0] _GEN_16 = {{3'd0}, _widx_T_1}; // @[src/main/scala/util/AsyncQueue.scala 53:43]
  wire [3:0] _widx_incremented_T_1 = widx_widx_bin + _GEN_16; // @[src/main/scala/util/AsyncQueue.scala 53:43]
  wire [3:0] widx_incremented = _widx_T_2 ? 4'h0 : _widx_incremented_T_1; // @[src/main/scala/util/AsyncQueue.scala 53:23]
  wire [3:0] _GEN_17 = {{1'd0}, widx_incremented[3:1]}; // @[src/main/scala/util/AsyncQueue.scala 54:17]
  wire [3:0] widx = widx_incremented ^ _GEN_17; // @[src/main/scala/util/AsyncQueue.scala 54:17]
  wire [3:0] ridx = ridx_ridx_gray_io_q; // @[src/main/scala/util/ShiftReg.scala 48:{24,24}]
  wire [3:0] _ready_T = ridx ^ 4'hc; // @[src/main/scala/util/AsyncQueue.scala 83:44]
  wire [2:0] _index_T_2 = {io_async_widx[3], 2'h0}; // @[src/main/scala/util/AsyncQueue.scala 85:93]
  wire [2:0] index = io_async_widx[2:0] ^ _index_T_2; // @[src/main/scala/util/AsyncQueue.scala 85:64]
  reg  ready_reg; // @[src/main/scala/util/AsyncQueue.scala 88:56]
  reg [3:0] widx_gray; // @[src/main/scala/util/AsyncQueue.scala 91:55]
  AsyncResetSynchronizerShiftReg_w4_d3_i0 ridx_ridx_gray ( // @[src/main/scala/util/ShiftReg.scala 45:23]
    .clock(ridx_ridx_gray_clock),
    .reset(ridx_ridx_gray_reset),
    .io_d(ridx_ridx_gray_io_d),
    .io_q(ridx_ridx_gray_io_q)
  );
  AsyncValidSync source_valid_0 ( // @[src/main/scala/util/AsyncQueue.scala 100:32]
    .io_in(source_valid_0_io_in),
    .io_out(source_valid_0_io_out),
    .clock(source_valid_0_clock),
    .reset(source_valid_0_reset)
  );
  AsyncValidSync source_valid_1 ( // @[src/main/scala/util/AsyncQueue.scala 101:32]
    .io_in(source_valid_1_io_in),
    .io_out(source_valid_1_io_out),
    .clock(source_valid_1_clock),
    .reset(source_valid_1_reset)
  );
  AsyncValidSync sink_extend ( // @[src/main/scala/util/AsyncQueue.scala 103:30]
    .io_in(sink_extend_io_in),
    .io_out(sink_extend_io_out),
    .clock(sink_extend_clock),
    .reset(sink_extend_reset)
  );
  AsyncValidSync sink_valid ( // @[src/main/scala/util/AsyncQueue.scala 104:30]
    .io_in(sink_valid_io_in),
    .io_out(sink_valid_io_out),
    .clock(sink_valid_clock),
    .reset(sink_valid_reset)
  );
  assign io_enq_ready = ready_reg & sink_ready; // @[src/main/scala/util/AsyncQueue.scala 89:29]
  assign io_async_mem_0 = mem_0; // @[src/main/scala/util/AsyncQueue.scala 96:31]
  assign io_async_mem_1 = mem_1; // @[src/main/scala/util/AsyncQueue.scala 96:31]
  assign io_async_mem_2 = mem_2; // @[src/main/scala/util/AsyncQueue.scala 96:31]
  assign io_async_mem_3 = mem_3; // @[src/main/scala/util/AsyncQueue.scala 96:31]
  assign io_async_mem_4 = mem_4; // @[src/main/scala/util/AsyncQueue.scala 96:31]
  assign io_async_mem_5 = mem_5; // @[src/main/scala/util/AsyncQueue.scala 96:31]
  assign io_async_mem_6 = mem_6; // @[src/main/scala/util/AsyncQueue.scala 96:31]
  assign io_async_mem_7 = mem_7; // @[src/main/scala/util/AsyncQueue.scala 96:31]
  assign io_async_widx = widx_gray; // @[src/main/scala/util/AsyncQueue.scala 92:17]
  assign io_async_safe_widx_valid = source_valid_1_io_out; // @[src/main/scala/util/AsyncQueue.scala 117:20]
  assign io_async_safe_source_reset_n = ~reset; // @[src/main/scala/util/AsyncQueue.scala 121:27]
  assign ridx_ridx_gray_clock = clock;
  assign ridx_ridx_gray_reset = reset;
  assign ridx_ridx_gray_io_d = io_async_ridx; // @[src/main/scala/util/ShiftReg.scala 47:16]
  assign source_valid_0_io_in = 1'h1; // @[src/main/scala/util/AsyncQueue.scala 115:26]
  assign source_valid_0_clock = clock; // @[src/main/scala/util/AsyncQueue.scala 110:26]
  assign source_valid_0_reset = reset | ~io_async_safe_sink_reset_n; // @[src/main/scala/util/AsyncQueue.scala 105:65]
  assign source_valid_1_io_in = source_valid_0_io_out; // @[src/main/scala/util/AsyncQueue.scala 116:26]
  assign source_valid_1_clock = clock; // @[src/main/scala/util/AsyncQueue.scala 111:26]
  assign source_valid_1_reset = reset | ~io_async_safe_sink_reset_n; // @[src/main/scala/util/AsyncQueue.scala 106:65]
  assign sink_extend_io_in = io_async_safe_ridx_valid; // @[src/main/scala/util/AsyncQueue.scala 118:23]
  assign sink_extend_clock = clock; // @[src/main/scala/util/AsyncQueue.scala 112:26]
  assign sink_extend_reset = reset | ~io_async_safe_sink_reset_n; // @[src/main/scala/util/AsyncQueue.scala 107:65]
  assign sink_valid_io_in = sink_extend_io_out; // @[src/main/scala/util/AsyncQueue.scala 119:22]
  assign sink_valid_clock = clock; // @[src/main/scala/util/AsyncQueue.scala 113:26]
  assign sink_valid_reset = reset; // @[src/main/scala/util/AsyncQueue.scala 108:35]
  always @(posedge clock) begin
    if (_widx_T_1) begin // @[src/main/scala/util/AsyncQueue.scala 86:22]
      if (3'h0 == index) begin // @[src/main/scala/util/AsyncQueue.scala 86:35]
        mem_0 <= io_enq_bits; // @[src/main/scala/util/AsyncQueue.scala 86:35]
      end
    end
    if (_widx_T_1) begin // @[src/main/scala/util/AsyncQueue.scala 86:22]
      if (3'h1 == index) begin // @[src/main/scala/util/AsyncQueue.scala 86:35]
        mem_1 <= io_enq_bits; // @[src/main/scala/util/AsyncQueue.scala 86:35]
      end
    end
    if (_widx_T_1) begin // @[src/main/scala/util/AsyncQueue.scala 86:22]
      if (3'h2 == index) begin // @[src/main/scala/util/AsyncQueue.scala 86:35]
        mem_2 <= io_enq_bits; // @[src/main/scala/util/AsyncQueue.scala 86:35]
      end
    end
    if (_widx_T_1) begin // @[src/main/scala/util/AsyncQueue.scala 86:22]
      if (3'h3 == index) begin // @[src/main/scala/util/AsyncQueue.scala 86:35]
        mem_3 <= io_enq_bits; // @[src/main/scala/util/AsyncQueue.scala 86:35]
      end
    end
    if (_widx_T_1) begin // @[src/main/scala/util/AsyncQueue.scala 86:22]
      if (3'h4 == index) begin // @[src/main/scala/util/AsyncQueue.scala 86:35]
        mem_4 <= io_enq_bits; // @[src/main/scala/util/AsyncQueue.scala 86:35]
      end
    end
    if (_widx_T_1) begin // @[src/main/scala/util/AsyncQueue.scala 86:22]
      if (3'h5 == index) begin // @[src/main/scala/util/AsyncQueue.scala 86:35]
        mem_5 <= io_enq_bits; // @[src/main/scala/util/AsyncQueue.scala 86:35]
      end
    end
    if (_widx_T_1) begin // @[src/main/scala/util/AsyncQueue.scala 86:22]
      if (3'h6 == index) begin // @[src/main/scala/util/AsyncQueue.scala 86:35]
        mem_6 <= io_enq_bits; // @[src/main/scala/util/AsyncQueue.scala 86:35]
      end
    end
    if (_widx_T_1) begin // @[src/main/scala/util/AsyncQueue.scala 86:22]
      if (3'h7 == index) begin // @[src/main/scala/util/AsyncQueue.scala 86:35]
        mem_7 <= io_enq_bits; // @[src/main/scala/util/AsyncQueue.scala 86:35]
      end
    end
  end
  always @(posedge clock or posedge reset) begin
    if (reset) begin // @[src/main/scala/util/AsyncQueue.scala 53:23]
      widx_widx_bin <= 4'h0;
    end else if (_widx_T_2) begin
      widx_widx_bin <= 4'h0;
    end else begin
      widx_widx_bin <= _widx_incremented_T_1;
    end
  end
  always @(posedge clock or posedge reset) begin
    if (reset) begin // @[src/main/scala/util/AsyncQueue.scala 83:26]
      ready_reg <= 1'h0;
    end else begin
      ready_reg <= sink_ready & widx != _ready_T;
    end
  end
  always @(posedge clock or posedge reset) begin
    if (reset) begin // @[src/main/scala/util/AsyncQueue.scala 54:17]
      widx_gray <= 4'h0;
    end else begin
      widx_gray <= widx_incremented ^ _GEN_17;
    end
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  mem_0 = _RAND_0[15:0];
  _RAND_1 = {1{`RANDOM}};
  mem_1 = _RAND_1[15:0];
  _RAND_2 = {1{`RANDOM}};
  mem_2 = _RAND_2[15:0];
  _RAND_3 = {1{`RANDOM}};
  mem_3 = _RAND_3[15:0];
  _RAND_4 = {1{`RANDOM}};
  mem_4 = _RAND_4[15:0];
  _RAND_5 = {1{`RANDOM}};
  mem_5 = _RAND_5[15:0];
  _RAND_6 = {1{`RANDOM}};
  mem_6 = _RAND_6[15:0];
  _RAND_7 = {1{`RANDOM}};
  mem_7 = _RAND_7[15:0];
  _RAND_8 = {1{`RANDOM}};
  widx_widx_bin = _RAND_8[3:0];
  _RAND_9 = {1{`RANDOM}};
  ready_reg = _RAND_9[0:0];
  _RAND_10 = {1{`RANDOM}};
  widx_gray = _RAND_10[3:0];
`endif // RANDOMIZE_REG_INIT
  if (reset) begin
    widx_widx_bin = 4'h0;
  end
  if (reset) begin
    ready_reg = 1'h0;
  end
  if (reset) begin
    widx_gray = 4'h0;
  end
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module ClockCrossingReg_w16(
  input         clock,
  input  [15:0] io_d, // @[src/main/scala/util/SynchronizerReg.scala 195:14]
  output [15:0] io_q, // @[src/main/scala/util/SynchronizerReg.scala 195:14]
  input         io_en // @[src/main/scala/util/SynchronizerReg.scala 195:14]
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
`endif // RANDOMIZE_REG_INIT
  reg [15:0] cdc_reg; // @[src/main/scala/util/SynchronizerReg.scala 201:76]
  assign io_q = cdc_reg; // @[src/main/scala/util/SynchronizerReg.scala 202:8]
  always @(posedge clock) begin
    if (io_en) begin // @[src/main/scala/util/SynchronizerReg.scala 201:76]
      cdc_reg <= io_d; // @[src/main/scala/util/SynchronizerReg.scala 201:76]
    end
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  cdc_reg = _RAND_0[15:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module AsyncQueueSink(
  input         clock,
  input         reset,
  input         io_deq_ready, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  output        io_deq_valid, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  output [15:0] io_deq_bits, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  input  [15:0] io_async_mem_0, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  input  [15:0] io_async_mem_1, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  input  [15:0] io_async_mem_2, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  input  [15:0] io_async_mem_3, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  input  [15:0] io_async_mem_4, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  input  [15:0] io_async_mem_5, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  input  [15:0] io_async_mem_6, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  input  [15:0] io_async_mem_7, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  output [3:0]  io_async_ridx, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  input  [3:0]  io_async_widx, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  output        io_async_safe_ridx_valid, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  input         io_async_safe_widx_valid, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  input         io_async_safe_source_reset_n, // @[src/main/scala/util/AsyncQueue.scala 135:14]
  output        io_async_safe_sink_reset_n // @[src/main/scala/util/AsyncQueue.scala 135:14]
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
`endif // RANDOMIZE_REG_INIT
  wire  widx_widx_gray_clock; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  widx_widx_gray_reset; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire [3:0] widx_widx_gray_io_d; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire [3:0] widx_widx_gray_io_q; // @[src/main/scala/util/ShiftReg.scala 45:23]
  wire  io_deq_bits_deq_bits_reg_clock; // @[src/main/scala/util/SynchronizerReg.scala 207:25]
  wire [15:0] io_deq_bits_deq_bits_reg_io_d; // @[src/main/scala/util/SynchronizerReg.scala 207:25]
  wire [15:0] io_deq_bits_deq_bits_reg_io_q; // @[src/main/scala/util/SynchronizerReg.scala 207:25]
  wire  io_deq_bits_deq_bits_reg_io_en; // @[src/main/scala/util/SynchronizerReg.scala 207:25]
  wire  sink_valid_0_io_in; // @[src/main/scala/util/AsyncQueue.scala 168:33]
  wire  sink_valid_0_io_out; // @[src/main/scala/util/AsyncQueue.scala 168:33]
  wire  sink_valid_0_clock; // @[src/main/scala/util/AsyncQueue.scala 168:33]
  wire  sink_valid_0_reset; // @[src/main/scala/util/AsyncQueue.scala 168:33]
  wire  sink_valid_1_io_in; // @[src/main/scala/util/AsyncQueue.scala 169:33]
  wire  sink_valid_1_io_out; // @[src/main/scala/util/AsyncQueue.scala 169:33]
  wire  sink_valid_1_clock; // @[src/main/scala/util/AsyncQueue.scala 169:33]
  wire  sink_valid_1_reset; // @[src/main/scala/util/AsyncQueue.scala 169:33]
  wire  source_extend_io_in; // @[src/main/scala/util/AsyncQueue.scala 171:31]
  wire  source_extend_io_out; // @[src/main/scala/util/AsyncQueue.scala 171:31]
  wire  source_extend_clock; // @[src/main/scala/util/AsyncQueue.scala 171:31]
  wire  source_extend_reset; // @[src/main/scala/util/AsyncQueue.scala 171:31]
  wire  source_valid_io_in; // @[src/main/scala/util/AsyncQueue.scala 172:31]
  wire  source_valid_io_out; // @[src/main/scala/util/AsyncQueue.scala 172:31]
  wire  source_valid_clock; // @[src/main/scala/util/AsyncQueue.scala 172:31]
  wire  source_valid_reset; // @[src/main/scala/util/AsyncQueue.scala 172:31]
  wire  _ridx_T_1 = io_deq_ready & io_deq_valid; // @[src/main/scala/chisel3/util/Decoupled.scala 52:35]
  wire  source_ready = source_valid_io_out; // @[src/main/scala/util/AsyncQueue.scala 143:30 188:18]
  wire  _ridx_T_2 = ~source_ready; // @[src/main/scala/util/AsyncQueue.scala 144:77]
  reg [3:0] ridx_ridx_bin; // @[src/main/scala/util/AsyncQueue.scala 52:25]
  wire [3:0] _GEN_8 = {{3'd0}, _ridx_T_1}; // @[src/main/scala/util/AsyncQueue.scala 53:43]
  wire [3:0] _ridx_incremented_T_1 = ridx_ridx_bin + _GEN_8; // @[src/main/scala/util/AsyncQueue.scala 53:43]
  wire [3:0] ridx_incremented = _ridx_T_2 ? 4'h0 : _ridx_incremented_T_1; // @[src/main/scala/util/AsyncQueue.scala 53:23]
  wire [3:0] _GEN_9 = {{1'd0}, ridx_incremented[3:1]}; // @[src/main/scala/util/AsyncQueue.scala 54:17]
  wire [3:0] ridx = ridx_incremented ^ _GEN_9; // @[src/main/scala/util/AsyncQueue.scala 54:17]
  wire [3:0] widx = widx_widx_gray_io_q; // @[src/main/scala/util/ShiftReg.scala 48:{24,24}]
  wire [2:0] _index_T_2 = {ridx[3], 2'h0}; // @[src/main/scala/util/AsyncQueue.scala 152:75]
  wire [2:0] index = ridx[2:0] ^ _index_T_2; // @[src/main/scala/util/AsyncQueue.scala 152:55]
  wire [15:0] _GEN_1 = 3'h1 == index ? io_async_mem_1 : io_async_mem_0; // @[src/main/scala/util/SynchronizerReg.scala 209:{18,18}]
  wire [15:0] _GEN_2 = 3'h2 == index ? io_async_mem_2 : _GEN_1; // @[src/main/scala/util/SynchronizerReg.scala 209:{18,18}]
  wire [15:0] _GEN_3 = 3'h3 == index ? io_async_mem_3 : _GEN_2; // @[src/main/scala/util/SynchronizerReg.scala 209:{18,18}]
  wire [15:0] _GEN_4 = 3'h4 == index ? io_async_mem_4 : _GEN_3; // @[src/main/scala/util/SynchronizerReg.scala 209:{18,18}]
  wire [15:0] _GEN_5 = 3'h5 == index ? io_async_mem_5 : _GEN_4; // @[src/main/scala/util/SynchronizerReg.scala 209:{18,18}]
  wire [15:0] _GEN_6 = 3'h6 == index ? io_async_mem_6 : _GEN_5; // @[src/main/scala/util/SynchronizerReg.scala 209:{18,18}]
  reg  valid_reg; // @[src/main/scala/util/AsyncQueue.scala 161:56]
  reg [3:0] ridx_gray; // @[src/main/scala/util/AsyncQueue.scala 164:55]
  AsyncResetSynchronizerShiftReg_w4_d3_i0 widx_widx_gray ( // @[src/main/scala/util/ShiftReg.scala 45:23]
    .clock(widx_widx_gray_clock),
    .reset(widx_widx_gray_reset),
    .io_d(widx_widx_gray_io_d),
    .io_q(widx_widx_gray_io_q)
  );
  ClockCrossingReg_w16 io_deq_bits_deq_bits_reg ( // @[src/main/scala/util/SynchronizerReg.scala 207:25]
    .clock(io_deq_bits_deq_bits_reg_clock),
    .io_d(io_deq_bits_deq_bits_reg_io_d),
    .io_q(io_deq_bits_deq_bits_reg_io_q),
    .io_en(io_deq_bits_deq_bits_reg_io_en)
  );
  AsyncValidSync sink_valid_0 ( // @[src/main/scala/util/AsyncQueue.scala 168:33]
    .io_in(sink_valid_0_io_in),
    .io_out(sink_valid_0_io_out),
    .clock(sink_valid_0_clock),
    .reset(sink_valid_0_reset)
  );
  AsyncValidSync sink_valid_1 ( // @[src/main/scala/util/AsyncQueue.scala 169:33]
    .io_in(sink_valid_1_io_in),
    .io_out(sink_valid_1_io_out),
    .clock(sink_valid_1_clock),
    .reset(sink_valid_1_reset)
  );
  AsyncValidSync source_extend ( // @[src/main/scala/util/AsyncQueue.scala 171:31]
    .io_in(source_extend_io_in),
    .io_out(source_extend_io_out),
    .clock(source_extend_clock),
    .reset(source_extend_reset)
  );
  AsyncValidSync source_valid ( // @[src/main/scala/util/AsyncQueue.scala 172:31]
    .io_in(source_valid_io_in),
    .io_out(source_valid_io_out),
    .clock(source_valid_clock),
    .reset(source_valid_reset)
  );
  assign io_deq_valid = valid_reg & source_ready; // @[src/main/scala/util/AsyncQueue.scala 162:29]
  assign io_deq_bits = io_deq_bits_deq_bits_reg_io_q; // @[src/main/scala/util/SynchronizerReg.scala 211:{26,26}]
  assign io_async_ridx = ridx_gray; // @[src/main/scala/util/AsyncQueue.scala 165:17]
  assign io_async_safe_ridx_valid = sink_valid_1_io_out; // @[src/main/scala/util/AsyncQueue.scala 185:20]
  assign io_async_safe_sink_reset_n = ~reset; // @[src/main/scala/util/AsyncQueue.scala 189:25]
  assign widx_widx_gray_clock = clock;
  assign widx_widx_gray_reset = reset;
  assign widx_widx_gray_io_d = io_async_widx; // @[src/main/scala/util/ShiftReg.scala 47:16]
  assign io_deq_bits_deq_bits_reg_clock = clock;
  assign io_deq_bits_deq_bits_reg_io_d = 3'h7 == index ? io_async_mem_7 : _GEN_6; // @[src/main/scala/util/SynchronizerReg.scala 209:{18,18}]
  assign io_deq_bits_deq_bits_reg_io_en = source_ready & ridx != widx; // @[src/main/scala/util/AsyncQueue.scala 146:28]
  assign sink_valid_0_io_in = 1'h1; // @[src/main/scala/util/AsyncQueue.scala 183:24]
  assign sink_valid_0_clock = clock; // @[src/main/scala/util/AsyncQueue.scala 178:25]
  assign sink_valid_0_reset = reset | ~io_async_safe_source_reset_n; // @[src/main/scala/util/AsyncQueue.scala 173:66]
  assign sink_valid_1_io_in = sink_valid_0_io_out; // @[src/main/scala/util/AsyncQueue.scala 184:24]
  assign sink_valid_1_clock = clock; // @[src/main/scala/util/AsyncQueue.scala 179:25]
  assign sink_valid_1_reset = reset | ~io_async_safe_source_reset_n; // @[src/main/scala/util/AsyncQueue.scala 174:66]
  assign source_extend_io_in = io_async_safe_widx_valid; // @[src/main/scala/util/AsyncQueue.scala 186:25]
  assign source_extend_clock = clock; // @[src/main/scala/util/AsyncQueue.scala 180:25]
  assign source_extend_reset = reset | ~io_async_safe_source_reset_n; // @[src/main/scala/util/AsyncQueue.scala 175:66]
  assign source_valid_io_in = source_extend_io_out; // @[src/main/scala/util/AsyncQueue.scala 187:24]
  assign source_valid_clock = clock; // @[src/main/scala/util/AsyncQueue.scala 181:25]
  assign source_valid_reset = reset; // @[src/main/scala/util/AsyncQueue.scala 176:34]
  always @(posedge clock or posedge reset) begin
    if (reset) begin // @[src/main/scala/util/AsyncQueue.scala 53:23]
      ridx_ridx_bin <= 4'h0;
    end else if (_ridx_T_2) begin
      ridx_ridx_bin <= 4'h0;
    end else begin
      ridx_ridx_bin <= _ridx_incremented_T_1;
    end
  end
  always @(posedge clock or posedge reset) begin
    if (reset) begin // @[src/main/scala/util/AsyncQueue.scala 146:28]
      valid_reg <= 1'h0;
    end else begin
      valid_reg <= source_ready & ridx != widx;
    end
  end
  always @(posedge clock or posedge reset) begin
    if (reset) begin // @[src/main/scala/util/AsyncQueue.scala 54:17]
      ridx_gray <= 4'h0;
    end else begin
      ridx_gray <= ridx_incremented ^ _GEN_9;
    end
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  ridx_ridx_bin = _RAND_0[3:0];
  _RAND_1 = {1{`RANDOM}};
  valid_reg = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  ridx_gray = _RAND_2[3:0];
`endif // RANDOMIZE_REG_INIT
  if (reset) begin
    ridx_ridx_bin = 4'h0;
  end
  if (reset) begin
    valid_reg = 1'h0;
  end
  if (reset) begin
    ridx_gray = 4'h0;
  end
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module AsyncQueue(
  input         io_enq_clock, // @[src/main/scala/util/AsyncQueue.scala 223:14]
  input         io_enq_reset, // @[src/main/scala/util/AsyncQueue.scala 223:14]
  output        io_enq_ready, // @[src/main/scala/util/AsyncQueue.scala 223:14]
  input         io_enq_valid, // @[src/main/scala/util/AsyncQueue.scala 223:14]
  input  [15:0] io_enq_bits, // @[src/main/scala/util/AsyncQueue.scala 223:14]
  input         io_deq_clock, // @[src/main/scala/util/AsyncQueue.scala 223:14]
  input         io_deq_reset, // @[src/main/scala/util/AsyncQueue.scala 223:14]
  input         io_deq_ready, // @[src/main/scala/util/AsyncQueue.scala 223:14]
  output        io_deq_valid, // @[src/main/scala/util/AsyncQueue.scala 223:14]
  output [15:0] io_deq_bits // @[src/main/scala/util/AsyncQueue.scala 223:14]
);
  wire  source_clock; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire  source_reset; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire  source_io_enq_ready; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire  source_io_enq_valid; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire [15:0] source_io_enq_bits; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire [15:0] source_io_async_mem_0; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire [15:0] source_io_async_mem_1; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire [15:0] source_io_async_mem_2; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire [15:0] source_io_async_mem_3; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire [15:0] source_io_async_mem_4; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire [15:0] source_io_async_mem_5; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire [15:0] source_io_async_mem_6; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire [15:0] source_io_async_mem_7; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire [3:0] source_io_async_ridx; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire [3:0] source_io_async_widx; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire  source_io_async_safe_ridx_valid; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire  source_io_async_safe_widx_valid; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire  source_io_async_safe_source_reset_n; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire  source_io_async_safe_sink_reset_n; // @[src/main/scala/util/AsyncQueue.scala 224:70]
  wire  sink_clock; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire  sink_reset; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire  sink_io_deq_ready; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire  sink_io_deq_valid; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire [15:0] sink_io_deq_bits; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire [15:0] sink_io_async_mem_0; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire [15:0] sink_io_async_mem_1; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire [15:0] sink_io_async_mem_2; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire [15:0] sink_io_async_mem_3; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire [15:0] sink_io_async_mem_4; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire [15:0] sink_io_async_mem_5; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire [15:0] sink_io_async_mem_6; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire [15:0] sink_io_async_mem_7; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire [3:0] sink_io_async_ridx; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire [3:0] sink_io_async_widx; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire  sink_io_async_safe_ridx_valid; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire  sink_io_async_safe_widx_valid; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire  sink_io_async_safe_source_reset_n; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  wire  sink_io_async_safe_sink_reset_n; // @[src/main/scala/util/AsyncQueue.scala 225:70]
  AsyncQueueSource source ( // @[src/main/scala/util/AsyncQueue.scala 224:70]
    .clock(source_clock),
    .reset(source_reset),
    .io_enq_ready(source_io_enq_ready),
    .io_enq_valid(source_io_enq_valid),
    .io_enq_bits(source_io_enq_bits),
    .io_async_mem_0(source_io_async_mem_0),
    .io_async_mem_1(source_io_async_mem_1),
    .io_async_mem_2(source_io_async_mem_2),
    .io_async_mem_3(source_io_async_mem_3),
    .io_async_mem_4(source_io_async_mem_4),
    .io_async_mem_5(source_io_async_mem_5),
    .io_async_mem_6(source_io_async_mem_6),
    .io_async_mem_7(source_io_async_mem_7),
    .io_async_ridx(source_io_async_ridx),
    .io_async_widx(source_io_async_widx),
    .io_async_safe_ridx_valid(source_io_async_safe_ridx_valid),
    .io_async_safe_widx_valid(source_io_async_safe_widx_valid),
    .io_async_safe_source_reset_n(source_io_async_safe_source_reset_n),
    .io_async_safe_sink_reset_n(source_io_async_safe_sink_reset_n)
  );
  AsyncQueueSink sink ( // @[src/main/scala/util/AsyncQueue.scala 225:70]
    .clock(sink_clock),
    .reset(sink_reset),
    .io_deq_ready(sink_io_deq_ready),
    .io_deq_valid(sink_io_deq_valid),
    .io_deq_bits(sink_io_deq_bits),
    .io_async_mem_0(sink_io_async_mem_0),
    .io_async_mem_1(sink_io_async_mem_1),
    .io_async_mem_2(sink_io_async_mem_2),
    .io_async_mem_3(sink_io_async_mem_3),
    .io_async_mem_4(sink_io_async_mem_4),
    .io_async_mem_5(sink_io_async_mem_5),
    .io_async_mem_6(sink_io_async_mem_6),
    .io_async_mem_7(sink_io_async_mem_7),
    .io_async_ridx(sink_io_async_ridx),
    .io_async_widx(sink_io_async_widx),
    .io_async_safe_ridx_valid(sink_io_async_safe_ridx_valid),
    .io_async_safe_widx_valid(sink_io_async_safe_widx_valid),
    .io_async_safe_source_reset_n(sink_io_async_safe_source_reset_n),
    .io_async_safe_sink_reset_n(sink_io_async_safe_sink_reset_n)
  );
  assign io_enq_ready = source_io_enq_ready; // @[src/main/scala/util/AsyncQueue.scala 227:17]
  assign io_deq_valid = sink_io_deq_valid; // @[src/main/scala/util/AsyncQueue.scala 228:10]
  assign io_deq_bits = sink_io_deq_bits; // @[src/main/scala/util/AsyncQueue.scala 228:10]
  assign source_clock = io_enq_clock;
  assign source_reset = io_enq_reset;
  assign source_io_enq_valid = io_enq_valid; // @[src/main/scala/util/AsyncQueue.scala 227:17]
  assign source_io_enq_bits = io_enq_bits; // @[src/main/scala/util/AsyncQueue.scala 227:17]
  assign source_io_async_ridx = sink_io_async_ridx; // @[src/main/scala/util/AsyncQueue.scala 229:17]
  assign source_io_async_safe_ridx_valid = sink_io_async_safe_ridx_valid; // @[src/main/scala/util/AsyncQueue.scala 229:17]
  assign source_io_async_safe_sink_reset_n = sink_io_async_safe_sink_reset_n; // @[src/main/scala/util/AsyncQueue.scala 229:17]
  assign sink_clock = io_deq_clock;
  assign sink_reset = io_deq_reset;
  assign sink_io_deq_ready = io_deq_ready; // @[src/main/scala/util/AsyncQueue.scala 228:10]
  assign sink_io_async_mem_0 = source_io_async_mem_0; // @[src/main/scala/util/AsyncQueue.scala 229:17]
  assign sink_io_async_mem_1 = source_io_async_mem_1; // @[src/main/scala/util/AsyncQueue.scala 229:17]
  assign sink_io_async_mem_2 = source_io_async_mem_2; // @[src/main/scala/util/AsyncQueue.scala 229:17]
  assign sink_io_async_mem_3 = source_io_async_mem_3; // @[src/main/scala/util/AsyncQueue.scala 229:17]
  assign sink_io_async_mem_4 = source_io_async_mem_4; // @[src/main/scala/util/AsyncQueue.scala 229:17]
  assign sink_io_async_mem_5 = source_io_async_mem_5; // @[src/main/scala/util/AsyncQueue.scala 229:17]
  assign sink_io_async_mem_6 = source_io_async_mem_6; // @[src/main/scala/util/AsyncQueue.scala 229:17]
  assign sink_io_async_mem_7 = source_io_async_mem_7; // @[src/main/scala/util/AsyncQueue.scala 229:17]
  assign sink_io_async_widx = source_io_async_widx; // @[src/main/scala/util/AsyncQueue.scala 229:17]
  assign sink_io_async_safe_widx_valid = source_io_async_safe_widx_valid; // @[src/main/scala/util/AsyncQueue.scala 229:17]
  assign sink_io_async_safe_source_reset_n = source_io_async_safe_source_reset_n; // @[src/main/scala/util/AsyncQueue.scala 229:17]
endmodule
module TxMainband(
  input         clock,
  input         reset,
  output        io_rxMbAfe_ready, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input         io_rxMbAfe_valid, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_0, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_1, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_2, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_3, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_4, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_5, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_6, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_7, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_8, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_9, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_10, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_11, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_12, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_13, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_14, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input  [15:0] io_rxMbAfe_bits_15, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  output [15:0] io_txMbIo_data, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  output        io_txMbIo_valid, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  output        io_txMbIo_clkp, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  output        io_txMbIo_clkn, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input         io_clkp, // @[src/main/scala/mbafe/MbAfe.scala 13:16]
  input         io_clkn // @[src/main/scala/mbafe/MbAfe.scala 13:16]
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_10;
  reg [31:0] _RAND_11;
  reg [31:0] _RAND_12;
  reg [31:0] _RAND_13;
  reg [31:0] _RAND_14;
  reg [31:0] _RAND_15;
  reg [31:0] _RAND_16;
  reg [31:0] _RAND_17;
  reg [31:0] _RAND_18;
  reg [31:0] _RAND_19;
`endif // RANDOMIZE_REG_INIT
  wire  txMbFifos_0_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_0_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_0_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_0_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_0_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_0_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_0_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_0_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_0_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_0_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_1_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_1_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_1_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_1_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_1_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_1_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_1_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_1_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_1_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_1_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_2_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_2_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_2_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_2_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_2_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_2_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_2_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_2_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_2_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_2_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_3_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_3_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_3_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_3_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_3_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_3_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_3_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_3_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_3_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_3_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_4_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_4_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_4_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_4_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_4_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_4_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_4_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_4_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_4_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_4_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_5_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_5_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_5_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_5_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_5_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_5_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_5_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_5_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_5_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_5_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_6_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_6_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_6_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_6_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_6_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_6_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_6_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_6_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_6_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_6_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_7_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_7_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_7_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_7_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_7_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_7_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_7_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_7_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_7_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_7_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_8_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_8_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_8_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_8_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_8_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_8_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_8_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_8_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_8_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_8_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_9_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_9_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_9_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_9_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_9_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_9_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_9_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_9_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_9_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_9_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_10_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_10_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_10_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_10_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_10_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_10_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_10_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_10_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_10_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_10_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_11_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_11_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_11_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_11_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_11_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_11_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_11_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_11_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_11_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_11_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_12_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_12_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_12_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_12_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_12_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_12_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_12_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_12_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_12_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_12_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_13_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_13_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_13_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_13_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_13_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_13_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_13_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_13_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_13_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_13_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_14_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_14_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_14_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_14_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_14_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_14_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_14_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_14_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_14_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_14_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_15_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_15_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_15_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_15_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_15_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_15_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_15_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_15_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire  txMbFifos_15_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  wire [15:0] txMbFifos_15_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 38:44]
  reg [15:0] txMbShiftRegs_0; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_1; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_2; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_3; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_4; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_5; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_6; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_7; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_8; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_9; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_10; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_11; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_12; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_13; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_14; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [15:0] txMbShiftRegs_15; // @[src/main/scala/mbafe/MbAfe.scala 49:52]
  reg [3:0] txMbUICounter; // @[src/main/scala/mbafe/MbAfe.scala 50:36]
  reg [3:0] txMbUICounter_next; // @[src/main/scala/mbafe/MbAfe.scala 52:41]
  wire  _fifoValid_next_T_14 = txMbFifos_0_io_deq_valid & txMbFifos_1_io_deq_valid & txMbFifos_2_io_deq_valid &
    txMbFifos_3_io_deq_valid & txMbFifos_4_io_deq_valid & txMbFifos_5_io_deq_valid & txMbFifos_6_io_deq_valid &
    txMbFifos_7_io_deq_valid & txMbFifos_8_io_deq_valid & txMbFifos_9_io_deq_valid & txMbFifos_10_io_deq_valid &
    txMbFifos_11_io_deq_valid & txMbFifos_12_io_deq_valid & txMbFifos_13_io_deq_valid & txMbFifos_14_io_deq_valid &
    txMbFifos_15_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 55:77]
  reg  fifoValid_next; // @[src/main/scala/mbafe/MbAfe.scala 55:37]
  reg  shift; // @[src/main/scala/mbafe/MbAfe.scala 58:28]
  wire  hasData = ~(_fifoValid_next_T_14 ^ fifoValid_next) & fifoValid_next; // @[src/main/scala/mbafe/MbAfe.scala 60:86]
  wire [3:0] _T_139 = txMbUICounter_next % 4'h8; // @[src/main/scala/mbafe/MbAfe.scala 85:79]
  wire  _T_7 = txMbUICounter == 4'h0; // @[src/main/scala/mbafe/MbAfe.scala 91:36]
  wire [16:0] _txMbShiftRegs_0_T = {txMbShiftRegs_0, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_4 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_0_io_deq_bits} : _txMbShiftRegs_0_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [3:0] _txMbUICounter_T_1 = txMbUICounter + 4'h1; // @[src/main/scala/mbafe/MbAfe.scala 97:48]
  wire  _GEN_5 = hasData & _T_7; // @[src/main/scala/mbafe/MbAfe.scala 90:26 81:35]
  wire [16:0] _GEN_6 = hasData ? _GEN_4 : {{1'd0}, txMbShiftRegs_0}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire  _GEN_10 = _T_7 | _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 107:45 108:43]
  wire [16:0] _GEN_13 = shift ? _GEN_4 : _GEN_6; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_1_T = {txMbShiftRegs_1, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_16 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_1_io_deq_bits} : _txMbShiftRegs_1_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_18 = hasData ? _GEN_16 : {{1'd0}, txMbShiftRegs_1}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_25 = shift ? _GEN_16 : _GEN_18; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_2_T = {txMbShiftRegs_2, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_28 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_2_io_deq_bits} : _txMbShiftRegs_2_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_30 = hasData ? _GEN_28 : {{1'd0}, txMbShiftRegs_2}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_37 = shift ? _GEN_28 : _GEN_30; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_3_T = {txMbShiftRegs_3, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_40 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_3_io_deq_bits} : _txMbShiftRegs_3_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_42 = hasData ? _GEN_40 : {{1'd0}, txMbShiftRegs_3}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_49 = shift ? _GEN_40 : _GEN_42; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_4_T = {txMbShiftRegs_4, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_52 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_4_io_deq_bits} : _txMbShiftRegs_4_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_54 = hasData ? _GEN_52 : {{1'd0}, txMbShiftRegs_4}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_61 = shift ? _GEN_52 : _GEN_54; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_5_T = {txMbShiftRegs_5, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_64 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_5_io_deq_bits} : _txMbShiftRegs_5_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_66 = hasData ? _GEN_64 : {{1'd0}, txMbShiftRegs_5}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_73 = shift ? _GEN_64 : _GEN_66; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_6_T = {txMbShiftRegs_6, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_76 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_6_io_deq_bits} : _txMbShiftRegs_6_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_78 = hasData ? _GEN_76 : {{1'd0}, txMbShiftRegs_6}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_85 = shift ? _GEN_76 : _GEN_78; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_7_T = {txMbShiftRegs_7, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_88 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_7_io_deq_bits} : _txMbShiftRegs_7_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_90 = hasData ? _GEN_88 : {{1'd0}, txMbShiftRegs_7}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_97 = shift ? _GEN_88 : _GEN_90; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_8_T = {txMbShiftRegs_8, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_100 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_8_io_deq_bits} : _txMbShiftRegs_8_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_102 = hasData ? _GEN_100 : {{1'd0}, txMbShiftRegs_8}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_109 = shift ? _GEN_100 : _GEN_102; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_9_T = {txMbShiftRegs_9, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_112 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_9_io_deq_bits} : _txMbShiftRegs_9_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_114 = hasData ? _GEN_112 : {{1'd0}, txMbShiftRegs_9}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_121 = shift ? _GEN_112 : _GEN_114; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_10_T = {txMbShiftRegs_10, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_124 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_10_io_deq_bits} : _txMbShiftRegs_10_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_126 = hasData ? _GEN_124 : {{1'd0}, txMbShiftRegs_10}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_133 = shift ? _GEN_124 : _GEN_126; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_11_T = {txMbShiftRegs_11, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_136 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_11_io_deq_bits} : _txMbShiftRegs_11_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_138 = hasData ? _GEN_136 : {{1'd0}, txMbShiftRegs_11}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_145 = shift ? _GEN_136 : _GEN_138; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_12_T = {txMbShiftRegs_12, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_148 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_12_io_deq_bits} : _txMbShiftRegs_12_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_150 = hasData ? _GEN_148 : {{1'd0}, txMbShiftRegs_12}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_157 = shift ? _GEN_148 : _GEN_150; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_13_T = {txMbShiftRegs_13, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_160 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_13_io_deq_bits} : _txMbShiftRegs_13_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_162 = hasData ? _GEN_160 : {{1'd0}, txMbShiftRegs_13}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_169 = shift ? _GEN_160 : _GEN_162; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_14_T = {txMbShiftRegs_14, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_172 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_14_io_deq_bits} : _txMbShiftRegs_14_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_174 = hasData ? _GEN_172 : {{1'd0}, txMbShiftRegs_14}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_181 = shift ? _GEN_172 : _GEN_174; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [16:0] _txMbShiftRegs_15_T = {txMbShiftRegs_15, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 95:58]
  wire [16:0] _GEN_184 = txMbUICounter == 4'h0 ? {{1'd0}, txMbFifos_15_io_deq_bits} : _txMbShiftRegs_15_T; // @[src/main/scala/mbafe/MbAfe.scala 91:45 93:38 95:38]
  wire [16:0] _GEN_186 = hasData ? _GEN_184 : {{1'd0}, txMbShiftRegs_15}; // @[src/main/scala/mbafe/MbAfe.scala 90:26 49:52]
  wire [16:0] _GEN_193 = shift ? _GEN_184 : _GEN_186; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  wire [7:0] io_txMbIo_data_lo = {txMbShiftRegs_7[15],txMbShiftRegs_6[15],txMbShiftRegs_5[15],txMbShiftRegs_4[15],
    txMbShiftRegs_3[15],txMbShiftRegs_2[15],txMbShiftRegs_1[15],txMbShiftRegs_0[15]}; // @[src/main/scala/mbafe/MbAfe.scala 115:65]
  wire [7:0] io_txMbIo_data_hi = {txMbShiftRegs_15[15],txMbShiftRegs_14[15],txMbShiftRegs_13[15],txMbShiftRegs_12[15],
    txMbShiftRegs_11[15],txMbShiftRegs_10[15],txMbShiftRegs_9[15],txMbShiftRegs_8[15]}; // @[src/main/scala/mbafe/MbAfe.scala 115:65]
  wire [16:0] _GEN_194 = reset ? 17'h0 : _GEN_13; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_195 = reset ? 17'h0 : _GEN_25; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_196 = reset ? 17'h0 : _GEN_37; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_197 = reset ? 17'h0 : _GEN_49; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_198 = reset ? 17'h0 : _GEN_61; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_199 = reset ? 17'h0 : _GEN_73; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_200 = reset ? 17'h0 : _GEN_85; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_201 = reset ? 17'h0 : _GEN_97; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_202 = reset ? 17'h0 : _GEN_109; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_203 = reset ? 17'h0 : _GEN_121; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_204 = reset ? 17'h0 : _GEN_133; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_205 = reset ? 17'h0 : _GEN_145; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_206 = reset ? 17'h0 : _GEN_157; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_207 = reset ? 17'h0 : _GEN_169; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_208 = reset ? 17'h0 : _GEN_181; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  wire [16:0] _GEN_209 = reset ? 17'h0 : _GEN_193; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
  AsyncQueue txMbFifos_0 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_0_io_enq_clock),
    .io_enq_reset(txMbFifos_0_io_enq_reset),
    .io_enq_ready(txMbFifos_0_io_enq_ready),
    .io_enq_valid(txMbFifos_0_io_enq_valid),
    .io_enq_bits(txMbFifos_0_io_enq_bits),
    .io_deq_clock(txMbFifos_0_io_deq_clock),
    .io_deq_reset(txMbFifos_0_io_deq_reset),
    .io_deq_ready(txMbFifos_0_io_deq_ready),
    .io_deq_valid(txMbFifos_0_io_deq_valid),
    .io_deq_bits(txMbFifos_0_io_deq_bits)
  );
  AsyncQueue txMbFifos_1 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_1_io_enq_clock),
    .io_enq_reset(txMbFifos_1_io_enq_reset),
    .io_enq_ready(txMbFifos_1_io_enq_ready),
    .io_enq_valid(txMbFifos_1_io_enq_valid),
    .io_enq_bits(txMbFifos_1_io_enq_bits),
    .io_deq_clock(txMbFifos_1_io_deq_clock),
    .io_deq_reset(txMbFifos_1_io_deq_reset),
    .io_deq_ready(txMbFifos_1_io_deq_ready),
    .io_deq_valid(txMbFifos_1_io_deq_valid),
    .io_deq_bits(txMbFifos_1_io_deq_bits)
  );
  AsyncQueue txMbFifos_2 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_2_io_enq_clock),
    .io_enq_reset(txMbFifos_2_io_enq_reset),
    .io_enq_ready(txMbFifos_2_io_enq_ready),
    .io_enq_valid(txMbFifos_2_io_enq_valid),
    .io_enq_bits(txMbFifos_2_io_enq_bits),
    .io_deq_clock(txMbFifos_2_io_deq_clock),
    .io_deq_reset(txMbFifos_2_io_deq_reset),
    .io_deq_ready(txMbFifos_2_io_deq_ready),
    .io_deq_valid(txMbFifos_2_io_deq_valid),
    .io_deq_bits(txMbFifos_2_io_deq_bits)
  );
  AsyncQueue txMbFifos_3 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_3_io_enq_clock),
    .io_enq_reset(txMbFifos_3_io_enq_reset),
    .io_enq_ready(txMbFifos_3_io_enq_ready),
    .io_enq_valid(txMbFifos_3_io_enq_valid),
    .io_enq_bits(txMbFifos_3_io_enq_bits),
    .io_deq_clock(txMbFifos_3_io_deq_clock),
    .io_deq_reset(txMbFifos_3_io_deq_reset),
    .io_deq_ready(txMbFifos_3_io_deq_ready),
    .io_deq_valid(txMbFifos_3_io_deq_valid),
    .io_deq_bits(txMbFifos_3_io_deq_bits)
  );
  AsyncQueue txMbFifos_4 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_4_io_enq_clock),
    .io_enq_reset(txMbFifos_4_io_enq_reset),
    .io_enq_ready(txMbFifos_4_io_enq_ready),
    .io_enq_valid(txMbFifos_4_io_enq_valid),
    .io_enq_bits(txMbFifos_4_io_enq_bits),
    .io_deq_clock(txMbFifos_4_io_deq_clock),
    .io_deq_reset(txMbFifos_4_io_deq_reset),
    .io_deq_ready(txMbFifos_4_io_deq_ready),
    .io_deq_valid(txMbFifos_4_io_deq_valid),
    .io_deq_bits(txMbFifos_4_io_deq_bits)
  );
  AsyncQueue txMbFifos_5 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_5_io_enq_clock),
    .io_enq_reset(txMbFifos_5_io_enq_reset),
    .io_enq_ready(txMbFifos_5_io_enq_ready),
    .io_enq_valid(txMbFifos_5_io_enq_valid),
    .io_enq_bits(txMbFifos_5_io_enq_bits),
    .io_deq_clock(txMbFifos_5_io_deq_clock),
    .io_deq_reset(txMbFifos_5_io_deq_reset),
    .io_deq_ready(txMbFifos_5_io_deq_ready),
    .io_deq_valid(txMbFifos_5_io_deq_valid),
    .io_deq_bits(txMbFifos_5_io_deq_bits)
  );
  AsyncQueue txMbFifos_6 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_6_io_enq_clock),
    .io_enq_reset(txMbFifos_6_io_enq_reset),
    .io_enq_ready(txMbFifos_6_io_enq_ready),
    .io_enq_valid(txMbFifos_6_io_enq_valid),
    .io_enq_bits(txMbFifos_6_io_enq_bits),
    .io_deq_clock(txMbFifos_6_io_deq_clock),
    .io_deq_reset(txMbFifos_6_io_deq_reset),
    .io_deq_ready(txMbFifos_6_io_deq_ready),
    .io_deq_valid(txMbFifos_6_io_deq_valid),
    .io_deq_bits(txMbFifos_6_io_deq_bits)
  );
  AsyncQueue txMbFifos_7 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_7_io_enq_clock),
    .io_enq_reset(txMbFifos_7_io_enq_reset),
    .io_enq_ready(txMbFifos_7_io_enq_ready),
    .io_enq_valid(txMbFifos_7_io_enq_valid),
    .io_enq_bits(txMbFifos_7_io_enq_bits),
    .io_deq_clock(txMbFifos_7_io_deq_clock),
    .io_deq_reset(txMbFifos_7_io_deq_reset),
    .io_deq_ready(txMbFifos_7_io_deq_ready),
    .io_deq_valid(txMbFifos_7_io_deq_valid),
    .io_deq_bits(txMbFifos_7_io_deq_bits)
  );
  AsyncQueue txMbFifos_8 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_8_io_enq_clock),
    .io_enq_reset(txMbFifos_8_io_enq_reset),
    .io_enq_ready(txMbFifos_8_io_enq_ready),
    .io_enq_valid(txMbFifos_8_io_enq_valid),
    .io_enq_bits(txMbFifos_8_io_enq_bits),
    .io_deq_clock(txMbFifos_8_io_deq_clock),
    .io_deq_reset(txMbFifos_8_io_deq_reset),
    .io_deq_ready(txMbFifos_8_io_deq_ready),
    .io_deq_valid(txMbFifos_8_io_deq_valid),
    .io_deq_bits(txMbFifos_8_io_deq_bits)
  );
  AsyncQueue txMbFifos_9 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_9_io_enq_clock),
    .io_enq_reset(txMbFifos_9_io_enq_reset),
    .io_enq_ready(txMbFifos_9_io_enq_ready),
    .io_enq_valid(txMbFifos_9_io_enq_valid),
    .io_enq_bits(txMbFifos_9_io_enq_bits),
    .io_deq_clock(txMbFifos_9_io_deq_clock),
    .io_deq_reset(txMbFifos_9_io_deq_reset),
    .io_deq_ready(txMbFifos_9_io_deq_ready),
    .io_deq_valid(txMbFifos_9_io_deq_valid),
    .io_deq_bits(txMbFifos_9_io_deq_bits)
  );
  AsyncQueue txMbFifos_10 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_10_io_enq_clock),
    .io_enq_reset(txMbFifos_10_io_enq_reset),
    .io_enq_ready(txMbFifos_10_io_enq_ready),
    .io_enq_valid(txMbFifos_10_io_enq_valid),
    .io_enq_bits(txMbFifos_10_io_enq_bits),
    .io_deq_clock(txMbFifos_10_io_deq_clock),
    .io_deq_reset(txMbFifos_10_io_deq_reset),
    .io_deq_ready(txMbFifos_10_io_deq_ready),
    .io_deq_valid(txMbFifos_10_io_deq_valid),
    .io_deq_bits(txMbFifos_10_io_deq_bits)
  );
  AsyncQueue txMbFifos_11 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_11_io_enq_clock),
    .io_enq_reset(txMbFifos_11_io_enq_reset),
    .io_enq_ready(txMbFifos_11_io_enq_ready),
    .io_enq_valid(txMbFifos_11_io_enq_valid),
    .io_enq_bits(txMbFifos_11_io_enq_bits),
    .io_deq_clock(txMbFifos_11_io_deq_clock),
    .io_deq_reset(txMbFifos_11_io_deq_reset),
    .io_deq_ready(txMbFifos_11_io_deq_ready),
    .io_deq_valid(txMbFifos_11_io_deq_valid),
    .io_deq_bits(txMbFifos_11_io_deq_bits)
  );
  AsyncQueue txMbFifos_12 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_12_io_enq_clock),
    .io_enq_reset(txMbFifos_12_io_enq_reset),
    .io_enq_ready(txMbFifos_12_io_enq_ready),
    .io_enq_valid(txMbFifos_12_io_enq_valid),
    .io_enq_bits(txMbFifos_12_io_enq_bits),
    .io_deq_clock(txMbFifos_12_io_deq_clock),
    .io_deq_reset(txMbFifos_12_io_deq_reset),
    .io_deq_ready(txMbFifos_12_io_deq_ready),
    .io_deq_valid(txMbFifos_12_io_deq_valid),
    .io_deq_bits(txMbFifos_12_io_deq_bits)
  );
  AsyncQueue txMbFifos_13 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_13_io_enq_clock),
    .io_enq_reset(txMbFifos_13_io_enq_reset),
    .io_enq_ready(txMbFifos_13_io_enq_ready),
    .io_enq_valid(txMbFifos_13_io_enq_valid),
    .io_enq_bits(txMbFifos_13_io_enq_bits),
    .io_deq_clock(txMbFifos_13_io_deq_clock),
    .io_deq_reset(txMbFifos_13_io_deq_reset),
    .io_deq_ready(txMbFifos_13_io_deq_ready),
    .io_deq_valid(txMbFifos_13_io_deq_valid),
    .io_deq_bits(txMbFifos_13_io_deq_bits)
  );
  AsyncQueue txMbFifos_14 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_14_io_enq_clock),
    .io_enq_reset(txMbFifos_14_io_enq_reset),
    .io_enq_ready(txMbFifos_14_io_enq_ready),
    .io_enq_valid(txMbFifos_14_io_enq_valid),
    .io_enq_bits(txMbFifos_14_io_enq_bits),
    .io_deq_clock(txMbFifos_14_io_deq_clock),
    .io_deq_reset(txMbFifos_14_io_deq_reset),
    .io_deq_ready(txMbFifos_14_io_deq_ready),
    .io_deq_valid(txMbFifos_14_io_deq_valid),
    .io_deq_bits(txMbFifos_14_io_deq_bits)
  );
  AsyncQueue txMbFifos_15 ( // @[src/main/scala/mbafe/MbAfe.scala 38:44]
    .io_enq_clock(txMbFifos_15_io_enq_clock),
    .io_enq_reset(txMbFifos_15_io_enq_reset),
    .io_enq_ready(txMbFifos_15_io_enq_ready),
    .io_enq_valid(txMbFifos_15_io_enq_valid),
    .io_enq_bits(txMbFifos_15_io_enq_bits),
    .io_deq_clock(txMbFifos_15_io_deq_clock),
    .io_deq_reset(txMbFifos_15_io_deq_reset),
    .io_deq_ready(txMbFifos_15_io_deq_ready),
    .io_deq_valid(txMbFifos_15_io_deq_valid),
    .io_deq_bits(txMbFifos_15_io_deq_bits)
  );
  assign io_rxMbAfe_ready = txMbFifos_0_io_enq_ready & txMbFifos_1_io_enq_ready & txMbFifos_2_io_enq_ready &
    txMbFifos_3_io_enq_ready & txMbFifos_4_io_enq_ready & txMbFifos_5_io_enq_ready & txMbFifos_6_io_enq_ready &
    txMbFifos_7_io_enq_ready & txMbFifos_8_io_enq_ready & txMbFifos_9_io_enq_ready & txMbFifos_10_io_enq_ready &
    txMbFifos_11_io_enq_ready & txMbFifos_12_io_enq_ready & txMbFifos_13_io_enq_ready & txMbFifos_14_io_enq_ready &
    txMbFifos_15_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 116:69]
  assign io_txMbIo_data = {io_txMbIo_data_hi,io_txMbIo_data_lo}; // @[src/main/scala/mbafe/MbAfe.scala 115:65]
  assign io_txMbIo_valid = txMbUICounter_next != txMbUICounter & _T_139 <= 4'h3; // @[src/main/scala/mbafe/MbAfe.scala 85:56]
  assign io_txMbIo_clkp = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 68:100]
  assign io_txMbIo_clkn = io_clkn; // @[src/main/scala/mbafe/MbAfe.scala 67:100]
  assign txMbFifos_0_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_0_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_0_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_0_io_enq_bits = io_rxMbAfe_bits_0; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_0_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_0_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_0_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_1_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_1_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_1_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_1_io_enq_bits = io_rxMbAfe_bits_1; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_1_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_1_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_1_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_2_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_2_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_2_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_2_io_enq_bits = io_rxMbAfe_bits_2; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_2_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_2_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_2_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_3_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_3_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_3_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_3_io_enq_bits = io_rxMbAfe_bits_3; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_3_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_3_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_3_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_4_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_4_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_4_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_4_io_enq_bits = io_rxMbAfe_bits_4; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_4_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_4_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_4_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_5_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_5_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_5_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_5_io_enq_bits = io_rxMbAfe_bits_5; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_5_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_5_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_5_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_6_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_6_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_6_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_6_io_enq_bits = io_rxMbAfe_bits_6; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_6_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_6_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_6_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_7_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_7_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_7_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_7_io_enq_bits = io_rxMbAfe_bits_7; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_7_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_7_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_7_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_8_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_8_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_8_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_8_io_enq_bits = io_rxMbAfe_bits_8; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_8_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_8_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_8_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_9_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_9_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_9_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_9_io_enq_bits = io_rxMbAfe_bits_9; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_9_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_9_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_9_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_10_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_10_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_10_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_10_io_enq_bits = io_rxMbAfe_bits_10; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_10_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_10_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_10_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_11_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_11_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_11_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_11_io_enq_bits = io_rxMbAfe_bits_11; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_11_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_11_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_11_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_12_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_12_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_12_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_12_io_enq_bits = io_rxMbAfe_bits_12; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_12_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_12_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_12_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_13_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_13_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_13_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_13_io_enq_bits = io_rxMbAfe_bits_13; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_13_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_13_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_13_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_14_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_14_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_14_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_14_io_enq_bits = io_rxMbAfe_bits_14; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_14_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_14_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_14_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  assign txMbFifos_15_io_enq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 126:35]
  assign txMbFifos_15_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 127:35]
  assign txMbFifos_15_io_enq_valid = io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 129:35]
  assign txMbFifos_15_io_enq_bits = io_rxMbAfe_bits_15; // @[src/main/scala/mbafe/MbAfe.scala 128:35]
  assign txMbFifos_15_io_deq_clock = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 79:35]
  assign txMbFifos_15_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 80:35]
  assign txMbFifos_15_io_deq_ready = shift ? _GEN_10 : _GEN_5; // @[src/main/scala/mbafe/MbAfe.scala 106:24]
  always @(posedge io_clkp or posedge io_clkn) begin
    txMbShiftRegs_0 <= _GEN_194[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_1 <= _GEN_195[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_2 <= _GEN_196[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_3 <= _GEN_197[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_4 <= _GEN_198[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_5 <= _GEN_199[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_6 <= _GEN_200[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_7 <= _GEN_201[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_8 <= _GEN_202[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_9 <= _GEN_203[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_10 <= _GEN_204[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_11 <= _GEN_205[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_12 <= _GEN_206[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_13 <= _GEN_207[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_14 <= _GEN_208[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    txMbShiftRegs_15 <= _GEN_209[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 49:{52,52}]
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 50:36]
      txMbUICounter <= 4'h0; // @[src/main/scala/mbafe/MbAfe.scala 50:36]
    end else if (_T_7 & ~hasData) begin // @[src/main/scala/mbafe/MbAfe.scala 99:52]
      txMbUICounter <= 4'h0; // @[src/main/scala/mbafe/MbAfe.scala 100:31]
    end else begin
      txMbUICounter <= _txMbUICounter_T_1; // @[src/main/scala/mbafe/MbAfe.scala 103:31]
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 52:41]
      txMbUICounter_next <= 4'h0; // @[src/main/scala/mbafe/MbAfe.scala 52:41]
    end else begin
      txMbUICounter_next <= txMbUICounter; // @[src/main/scala/mbafe/MbAfe.scala 52:41]
    end
    fifoValid_next <= txMbFifos_0_io_deq_valid & txMbFifos_1_io_deq_valid & txMbFifos_2_io_deq_valid &
      txMbFifos_3_io_deq_valid & txMbFifos_4_io_deq_valid & txMbFifos_5_io_deq_valid & txMbFifos_6_io_deq_valid &
      txMbFifos_7_io_deq_valid & txMbFifos_8_io_deq_valid & txMbFifos_9_io_deq_valid & txMbFifos_10_io_deq_valid &
      txMbFifos_11_io_deq_valid & txMbFifos_12_io_deq_valid & txMbFifos_13_io_deq_valid & txMbFifos_14_io_deq_valid &
      txMbFifos_15_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 55:77]
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 58:28]
      shift <= 1'h0; // @[src/main/scala/mbafe/MbAfe.scala 58:28]
    end else if (_T_7 & ~hasData) begin // @[src/main/scala/mbafe/MbAfe.scala 99:52]
      shift <= 1'h0; // @[src/main/scala/mbafe/MbAfe.scala 101:23]
    end else begin
      shift <= 1'h1; // @[src/main/scala/mbafe/MbAfe.scala 104:23]
    end
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  txMbShiftRegs_0 = _RAND_0[15:0];
  _RAND_1 = {1{`RANDOM}};
  txMbShiftRegs_1 = _RAND_1[15:0];
  _RAND_2 = {1{`RANDOM}};
  txMbShiftRegs_2 = _RAND_2[15:0];
  _RAND_3 = {1{`RANDOM}};
  txMbShiftRegs_3 = _RAND_3[15:0];
  _RAND_4 = {1{`RANDOM}};
  txMbShiftRegs_4 = _RAND_4[15:0];
  _RAND_5 = {1{`RANDOM}};
  txMbShiftRegs_5 = _RAND_5[15:0];
  _RAND_6 = {1{`RANDOM}};
  txMbShiftRegs_6 = _RAND_6[15:0];
  _RAND_7 = {1{`RANDOM}};
  txMbShiftRegs_7 = _RAND_7[15:0];
  _RAND_8 = {1{`RANDOM}};
  txMbShiftRegs_8 = _RAND_8[15:0];
  _RAND_9 = {1{`RANDOM}};
  txMbShiftRegs_9 = _RAND_9[15:0];
  _RAND_10 = {1{`RANDOM}};
  txMbShiftRegs_10 = _RAND_10[15:0];
  _RAND_11 = {1{`RANDOM}};
  txMbShiftRegs_11 = _RAND_11[15:0];
  _RAND_12 = {1{`RANDOM}};
  txMbShiftRegs_12 = _RAND_12[15:0];
  _RAND_13 = {1{`RANDOM}};
  txMbShiftRegs_13 = _RAND_13[15:0];
  _RAND_14 = {1{`RANDOM}};
  txMbShiftRegs_14 = _RAND_14[15:0];
  _RAND_15 = {1{`RANDOM}};
  txMbShiftRegs_15 = _RAND_15[15:0];
  _RAND_16 = {1{`RANDOM}};
  txMbUICounter = _RAND_16[3:0];
  _RAND_17 = {1{`RANDOM}};
  txMbUICounter_next = _RAND_17[3:0];
  _RAND_18 = {1{`RANDOM}};
  fifoValid_next = _RAND_18[0:0];
  _RAND_19 = {1{`RANDOM}};
  shift = _RAND_19[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module RxMainband(
  input         clock,
  input         reset,
  input  [15:0] io_rxMbIo_data, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  input         io_rxMbIo_valid, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  input         io_rxMbIo_clkp, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  input         io_rxMbIo_clkn, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  input         io_txMbAfe_ready, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output        io_txMbAfe_valid, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_0, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_1, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_2, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_3, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_4, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_5, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_6, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_7, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_8, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_9, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_10, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_11, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_12, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_13, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_14, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output [15:0] io_txMbAfe_bits_15, // @[src/main/scala/mbafe/MbAfe.scala 137:16]
  output        io_clkn_out // @[src/main/scala/mbafe/MbAfe.scala 137:16]
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_10;
  reg [31:0] _RAND_11;
  reg [31:0] _RAND_12;
  reg [31:0] _RAND_13;
  reg [31:0] _RAND_14;
  reg [31:0] _RAND_15;
  reg [31:0] _RAND_16;
  reg [31:0] _RAND_17;
  reg [31:0] _RAND_18;
  reg [31:0] _RAND_19;
  reg [31:0] _RAND_20;
  reg [31:0] _RAND_21;
  reg [31:0] _RAND_22;
  reg [31:0] _RAND_23;
  reg [31:0] _RAND_24;
  reg [31:0] _RAND_25;
  reg [31:0] _RAND_26;
  reg [31:0] _RAND_27;
  reg [31:0] _RAND_28;
  reg [31:0] _RAND_29;
  reg [31:0] _RAND_30;
  reg [31:0] _RAND_31;
  reg [31:0] _RAND_32;
  reg [31:0] _RAND_33;
  reg [31:0] _RAND_34;
  reg [31:0] _RAND_35;
  reg [31:0] _RAND_36;
  reg [31:0] _RAND_37;
  reg [31:0] _RAND_38;
`endif // RANDOMIZE_REG_INIT
  wire  rxMbFifos_0_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_0_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_0_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_0_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_0_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_0_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_0_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_0_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_0_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_0_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_1_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_1_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_1_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_1_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_1_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_1_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_1_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_1_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_1_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_1_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_2_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_2_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_2_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_2_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_2_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_2_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_2_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_2_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_2_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_2_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_3_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_3_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_3_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_3_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_3_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_3_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_3_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_3_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_3_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_3_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_4_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_4_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_4_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_4_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_4_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_4_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_4_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_4_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_4_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_4_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_5_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_5_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_5_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_5_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_5_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_5_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_5_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_5_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_5_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_5_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_6_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_6_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_6_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_6_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_6_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_6_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_6_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_6_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_6_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_6_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_7_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_7_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_7_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_7_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_7_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_7_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_7_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_7_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_7_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_7_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_8_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_8_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_8_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_8_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_8_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_8_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_8_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_8_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_8_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_8_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_9_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_9_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_9_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_9_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_9_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_9_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_9_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_9_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_9_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_9_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_10_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_10_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_10_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_10_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_10_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_10_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_10_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_10_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_10_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_10_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_11_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_11_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_11_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_11_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_11_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_11_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_11_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_11_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_11_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_11_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_12_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_12_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_12_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_12_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_12_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_12_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_12_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_12_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_12_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_12_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_13_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_13_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_13_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_13_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_13_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_13_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_13_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_13_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_13_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_13_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_14_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_14_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_14_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_14_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_14_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_14_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_14_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_14_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_14_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_14_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_15_io_enq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_15_io_enq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_15_io_enq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_15_io_enq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_15_io_enq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_15_io_deq_clock; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_15_io_deq_reset; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_15_io_deq_ready; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire  rxMbFifos_15_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  wire [15:0] rxMbFifos_15_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 160:44]
  reg  mbIoValid_pipe_0; // @[src/main/scala/mbafe/MbAfe.scala 169:39]
  reg  mbIoValid_pipe_1; // @[src/main/scala/mbafe/MbAfe.scala 170:39]
  reg  mbIoValid_pipe_2; // @[src/main/scala/mbafe/MbAfe.scala 171:39]
  reg  mbIoValid_next; // @[src/main/scala/mbafe/MbAfe.scala 172:37]
  reg [15:0] rxMbShiftRegs_0; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_1; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_2; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_3; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_4; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_5; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_6; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_7; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_8; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_9; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_10; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_11; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_12; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_13; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_14; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_15; // @[src/main/scala/mbafe/MbAfe.scala 175:52]
  reg [15:0] rxMbShiftRegs_next_0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_1; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_2; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_3; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_4; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_5; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_6; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_7; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_8; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_9; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_10; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_11; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_12; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_13; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_14; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [15:0] rxMbShiftRegs_next_15; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
  reg [3:0] rxMbUICounter; // @[src/main/scala/mbafe/MbAfe.scala 179:36]
  reg [3:0] rxMbUICounter_next; // @[src/main/scala/mbafe/MbAfe.scala 181:41]
  wire  _fifo_enq_valid_next_T_1 = rxMbUICounter == 4'h0; // @[src/main/scala/mbafe/MbAfe.scala 185:95]
  wire  _fifo_enq_valid_next_T_2 = rxMbUICounter_next == 4'hf & rxMbUICounter == 4'h0; // @[src/main/scala/mbafe/MbAfe.scala 185:78]
  reg  fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 185:42]
  wire  internal_valid = mbIoValid_next ^ io_rxMbIo_valid | mbIoValid_next & io_rxMbIo_valid; // @[src/main/scala/mbafe/MbAfe.scala 186:65]
  wire [16:0] _rxMbShiftRegs_0_T_2 = {rxMbShiftRegs_0, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_608 = {{16'd0}, io_rxMbIo_data[0]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_0_T_4 = _rxMbShiftRegs_0_T_2 | _GEN_608; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_1_T_2 = {rxMbShiftRegs_1, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_609 = {{16'd0}, io_rxMbIo_data[1]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_1_T_4 = _rxMbShiftRegs_1_T_2 | _GEN_609; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_2_T_2 = {rxMbShiftRegs_2, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_610 = {{16'd0}, io_rxMbIo_data[2]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_2_T_4 = _rxMbShiftRegs_2_T_2 | _GEN_610; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_3_T_2 = {rxMbShiftRegs_3, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_611 = {{16'd0}, io_rxMbIo_data[3]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_3_T_4 = _rxMbShiftRegs_3_T_2 | _GEN_611; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_4_T_2 = {rxMbShiftRegs_4, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_612 = {{16'd0}, io_rxMbIo_data[4]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_4_T_4 = _rxMbShiftRegs_4_T_2 | _GEN_612; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_5_T_2 = {rxMbShiftRegs_5, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_613 = {{16'd0}, io_rxMbIo_data[5]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_5_T_4 = _rxMbShiftRegs_5_T_2 | _GEN_613; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_6_T_2 = {rxMbShiftRegs_6, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_614 = {{16'd0}, io_rxMbIo_data[6]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_6_T_4 = _rxMbShiftRegs_6_T_2 | _GEN_614; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_7_T_2 = {rxMbShiftRegs_7, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_615 = {{16'd0}, io_rxMbIo_data[7]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_7_T_4 = _rxMbShiftRegs_7_T_2 | _GEN_615; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_8_T_2 = {rxMbShiftRegs_8, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_616 = {{16'd0}, io_rxMbIo_data[8]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_8_T_4 = _rxMbShiftRegs_8_T_2 | _GEN_616; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_9_T_2 = {rxMbShiftRegs_9, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_617 = {{16'd0}, io_rxMbIo_data[9]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_9_T_4 = _rxMbShiftRegs_9_T_2 | _GEN_617; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_10_T_2 = {rxMbShiftRegs_10, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_618 = {{16'd0}, io_rxMbIo_data[10]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_10_T_4 = _rxMbShiftRegs_10_T_2 | _GEN_618; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_11_T_2 = {rxMbShiftRegs_11, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_619 = {{16'd0}, io_rxMbIo_data[11]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_11_T_4 = _rxMbShiftRegs_11_T_2 | _GEN_619; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_12_T_2 = {rxMbShiftRegs_12, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_620 = {{16'd0}, io_rxMbIo_data[12]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_12_T_4 = _rxMbShiftRegs_12_T_2 | _GEN_620; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_13_T_2 = {rxMbShiftRegs_13, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_621 = {{16'd0}, io_rxMbIo_data[13]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_13_T_4 = _rxMbShiftRegs_13_T_2 | _GEN_621; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_14_T_2 = {rxMbShiftRegs_14, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_622 = {{16'd0}, io_rxMbIo_data[14]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_14_T_4 = _rxMbShiftRegs_14_T_2 | _GEN_622; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_15_T_2 = {rxMbShiftRegs_15, 1'h0}; // @[src/main/scala/mbafe/MbAfe.scala 208:62]
  wire [16:0] _GEN_623 = {{16'd0}, io_rxMbIo_data[15]}; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _rxMbShiftRegs_15_T_4 = _rxMbShiftRegs_15_T_2 | _GEN_623; // @[src/main/scala/mbafe/MbAfe.scala 208:69]
  wire [16:0] _GEN_0 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[0]} : _rxMbShiftRegs_0_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_1 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[1]} : _rxMbShiftRegs_1_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_2 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[2]} : _rxMbShiftRegs_2_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_3 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[3]} : _rxMbShiftRegs_3_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_4 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[4]} : _rxMbShiftRegs_4_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_5 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[5]} : _rxMbShiftRegs_5_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_6 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[6]} : _rxMbShiftRegs_6_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_7 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[7]} : _rxMbShiftRegs_7_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_8 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[8]} : _rxMbShiftRegs_8_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_9 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[9]} : _rxMbShiftRegs_9_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_10 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[10]} : _rxMbShiftRegs_10_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_11 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[11]} : _rxMbShiftRegs_11_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_12 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[12]} : _rxMbShiftRegs_12_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_13 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[13]} : _rxMbShiftRegs_13_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_14 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[14]} : _rxMbShiftRegs_14_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [16:0] _GEN_15 = _fifo_enq_valid_next_T_1 ? {{16'd0}, io_rxMbIo_data[15]} : _rxMbShiftRegs_15_T_4; // @[src/main/scala/mbafe/MbAfe.scala 200:45 202:42 208:42]
  wire [3:0] _rxMbUICounter_T_1 = rxMbUICounter + 4'h1; // @[src/main/scala/mbafe/MbAfe.scala 213:48]
  wire [16:0] _GEN_18 = internal_valid ? _GEN_0 : {{1'd0}, rxMbShiftRegs_0}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_19 = internal_valid ? _GEN_1 : {{1'd0}, rxMbShiftRegs_1}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_20 = internal_valid ? _GEN_2 : {{1'd0}, rxMbShiftRegs_2}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_21 = internal_valid ? _GEN_3 : {{1'd0}, rxMbShiftRegs_3}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_22 = internal_valid ? _GEN_4 : {{1'd0}, rxMbShiftRegs_4}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_23 = internal_valid ? _GEN_5 : {{1'd0}, rxMbShiftRegs_5}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_24 = internal_valid ? _GEN_6 : {{1'd0}, rxMbShiftRegs_6}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_25 = internal_valid ? _GEN_7 : {{1'd0}, rxMbShiftRegs_7}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_26 = internal_valid ? _GEN_8 : {{1'd0}, rxMbShiftRegs_8}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_27 = internal_valid ? _GEN_9 : {{1'd0}, rxMbShiftRegs_9}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_28 = internal_valid ? _GEN_10 : {{1'd0}, rxMbShiftRegs_10}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_29 = internal_valid ? _GEN_11 : {{1'd0}, rxMbShiftRegs_11}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_30 = internal_valid ? _GEN_12 : {{1'd0}, rxMbShiftRegs_12}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_31 = internal_valid ? _GEN_13 : {{1'd0}, rxMbShiftRegs_13}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_32 = internal_valid ? _GEN_14 : {{1'd0}, rxMbShiftRegs_14}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [16:0] _GEN_33 = internal_valid ? _GEN_15 : {{1'd0}, rxMbShiftRegs_15}; // @[src/main/scala/mbafe/MbAfe.scala 198:33 175:52]
  wire [3:0] _GEN_35 = internal_valid ? _rxMbUICounter_T_1 : rxMbUICounter; // @[src/main/scala/mbafe/MbAfe.scala 198:33 213:31 179:36]
  wire [15:0] rxMbShiftRegs_xor_0 = rxMbShiftRegs_0 ^ rxMbShiftRegs_next_0; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire  _T_4 = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  wire [15:0] rxMbShiftRegs_xor_1 = rxMbShiftRegs_1 ^ rxMbShiftRegs_next_1; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [15:0] rxMbShiftRegs_xor_3 = rxMbShiftRegs_3 ^ rxMbShiftRegs_next_3; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [15:0] rxMbShiftRegs_xor_2 = rxMbShiftRegs_2 ^ rxMbShiftRegs_next_2; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [15:0] rxMbShiftRegs_xor_5 = rxMbShiftRegs_5 ^ rxMbShiftRegs_next_5; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [15:0] rxMbShiftRegs_xor_4 = rxMbShiftRegs_4 ^ rxMbShiftRegs_next_4; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [15:0] rxMbShiftRegs_xor_7 = rxMbShiftRegs_7 ^ rxMbShiftRegs_next_7; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [15:0] rxMbShiftRegs_xor_6 = rxMbShiftRegs_6 ^ rxMbShiftRegs_next_6; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [127:0] rxMbFifos_0_io_enq_bits_lo = {rxMbShiftRegs_xor_7,rxMbShiftRegs_xor_6,rxMbShiftRegs_xor_5,
    rxMbShiftRegs_xor_4,rxMbShiftRegs_xor_3,rxMbShiftRegs_xor_2,rxMbShiftRegs_xor_1,rxMbShiftRegs_xor_0}; // @[src/main/scala/mbafe/MbAfe.scala 220:44]
  wire [15:0] rxMbShiftRegs_xor_9 = rxMbShiftRegs_9 ^ rxMbShiftRegs_next_9; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [15:0] rxMbShiftRegs_xor_8 = rxMbShiftRegs_8 ^ rxMbShiftRegs_next_8; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [15:0] rxMbShiftRegs_xor_11 = rxMbShiftRegs_11 ^ rxMbShiftRegs_next_11; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [15:0] rxMbShiftRegs_xor_10 = rxMbShiftRegs_10 ^ rxMbShiftRegs_next_10; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [15:0] rxMbShiftRegs_xor_13 = rxMbShiftRegs_13 ^ rxMbShiftRegs_next_13; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [15:0] rxMbShiftRegs_xor_12 = rxMbShiftRegs_12 ^ rxMbShiftRegs_next_12; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [15:0] rxMbShiftRegs_xor_15 = rxMbShiftRegs_15 ^ rxMbShiftRegs_next_15; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [15:0] rxMbShiftRegs_xor_14 = rxMbShiftRegs_14 ^ rxMbShiftRegs_next_14; // @[src/main/scala/mbafe/MbAfe.scala 215:54]
  wire [255:0] _rxMbFifos_0_io_enq_bits_T = {rxMbShiftRegs_xor_15,rxMbShiftRegs_xor_14,rxMbShiftRegs_xor_13,
    rxMbShiftRegs_xor_12,rxMbShiftRegs_xor_11,rxMbShiftRegs_xor_10,rxMbShiftRegs_xor_9,rxMbShiftRegs_xor_8,
    rxMbFifos_0_io_enq_bits_lo}; // @[src/main/scala/mbafe/MbAfe.scala 220:44]
  wire [255:0] _GEN_37 = _T_4 ? _rxMbFifos_0_io_enq_bits_T : 256'h0; // @[src/main/scala/mbafe/MbAfe.scala 218:19 194:34 220:38]
  wire [16:0] _GEN_56 = internal_valid ? _GEN_0 : _GEN_18; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_57 = internal_valid ? _GEN_1 : _GEN_19; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_58 = internal_valid ? _GEN_2 : _GEN_20; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_59 = internal_valid ? _GEN_3 : _GEN_21; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_60 = internal_valid ? _GEN_4 : _GEN_22; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_61 = internal_valid ? _GEN_5 : _GEN_23; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_62 = internal_valid ? _GEN_6 : _GEN_24; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_63 = internal_valid ? _GEN_7 : _GEN_25; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_64 = internal_valid ? _GEN_8 : _GEN_26; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_65 = internal_valid ? _GEN_9 : _GEN_27; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_66 = internal_valid ? _GEN_10 : _GEN_28; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_67 = internal_valid ? _GEN_11 : _GEN_29; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_68 = internal_valid ? _GEN_12 : _GEN_30; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_69 = internal_valid ? _GEN_13 : _GEN_31; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_70 = internal_valid ? _GEN_14 : _GEN_32; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_71 = internal_valid ? _GEN_15 : _GEN_33; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [3:0] _GEN_73 = internal_valid ? _rxMbUICounter_T_1 : _GEN_35; // @[src/main/scala/mbafe/MbAfe.scala 198:33 213:31]
  wire [16:0] _GEN_94 = internal_valid ? _GEN_0 : _GEN_56; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_95 = internal_valid ? _GEN_1 : _GEN_57; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_96 = internal_valid ? _GEN_2 : _GEN_58; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_97 = internal_valid ? _GEN_3 : _GEN_59; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_98 = internal_valid ? _GEN_4 : _GEN_60; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_99 = internal_valid ? _GEN_5 : _GEN_61; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_100 = internal_valid ? _GEN_6 : _GEN_62; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_101 = internal_valid ? _GEN_7 : _GEN_63; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_102 = internal_valid ? _GEN_8 : _GEN_64; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_103 = internal_valid ? _GEN_9 : _GEN_65; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_104 = internal_valid ? _GEN_10 : _GEN_66; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_105 = internal_valid ? _GEN_11 : _GEN_67; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_106 = internal_valid ? _GEN_12 : _GEN_68; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_107 = internal_valid ? _GEN_13 : _GEN_69; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_108 = internal_valid ? _GEN_14 : _GEN_70; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_109 = internal_valid ? _GEN_15 : _GEN_71; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [3:0] _GEN_111 = internal_valid ? _rxMbUICounter_T_1 : _GEN_73; // @[src/main/scala/mbafe/MbAfe.scala 198:33 213:31]
  wire [16:0] _GEN_132 = internal_valid ? _GEN_0 : _GEN_94; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_133 = internal_valid ? _GEN_1 : _GEN_95; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_134 = internal_valid ? _GEN_2 : _GEN_96; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_135 = internal_valid ? _GEN_3 : _GEN_97; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_136 = internal_valid ? _GEN_4 : _GEN_98; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_137 = internal_valid ? _GEN_5 : _GEN_99; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_138 = internal_valid ? _GEN_6 : _GEN_100; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_139 = internal_valid ? _GEN_7 : _GEN_101; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_140 = internal_valid ? _GEN_8 : _GEN_102; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_141 = internal_valid ? _GEN_9 : _GEN_103; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_142 = internal_valid ? _GEN_10 : _GEN_104; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_143 = internal_valid ? _GEN_11 : _GEN_105; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_144 = internal_valid ? _GEN_12 : _GEN_106; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_145 = internal_valid ? _GEN_13 : _GEN_107; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_146 = internal_valid ? _GEN_14 : _GEN_108; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_147 = internal_valid ? _GEN_15 : _GEN_109; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [3:0] _GEN_149 = internal_valid ? _rxMbUICounter_T_1 : _GEN_111; // @[src/main/scala/mbafe/MbAfe.scala 198:33 213:31]
  wire [16:0] _GEN_170 = internal_valid ? _GEN_0 : _GEN_132; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_171 = internal_valid ? _GEN_1 : _GEN_133; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_172 = internal_valid ? _GEN_2 : _GEN_134; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_173 = internal_valid ? _GEN_3 : _GEN_135; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_174 = internal_valid ? _GEN_4 : _GEN_136; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_175 = internal_valid ? _GEN_5 : _GEN_137; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_176 = internal_valid ? _GEN_6 : _GEN_138; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_177 = internal_valid ? _GEN_7 : _GEN_139; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_178 = internal_valid ? _GEN_8 : _GEN_140; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_179 = internal_valid ? _GEN_9 : _GEN_141; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_180 = internal_valid ? _GEN_10 : _GEN_142; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_181 = internal_valid ? _GEN_11 : _GEN_143; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_182 = internal_valid ? _GEN_12 : _GEN_144; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_183 = internal_valid ? _GEN_13 : _GEN_145; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_184 = internal_valid ? _GEN_14 : _GEN_146; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_185 = internal_valid ? _GEN_15 : _GEN_147; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [3:0] _GEN_187 = internal_valid ? _rxMbUICounter_T_1 : _GEN_149; // @[src/main/scala/mbafe/MbAfe.scala 198:33 213:31]
  wire [16:0] _GEN_208 = internal_valid ? _GEN_0 : _GEN_170; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_209 = internal_valid ? _GEN_1 : _GEN_171; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_210 = internal_valid ? _GEN_2 : _GEN_172; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_211 = internal_valid ? _GEN_3 : _GEN_173; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_212 = internal_valid ? _GEN_4 : _GEN_174; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_213 = internal_valid ? _GEN_5 : _GEN_175; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_214 = internal_valid ? _GEN_6 : _GEN_176; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_215 = internal_valid ? _GEN_7 : _GEN_177; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_216 = internal_valid ? _GEN_8 : _GEN_178; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_217 = internal_valid ? _GEN_9 : _GEN_179; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_218 = internal_valid ? _GEN_10 : _GEN_180; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_219 = internal_valid ? _GEN_11 : _GEN_181; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_220 = internal_valid ? _GEN_12 : _GEN_182; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_221 = internal_valid ? _GEN_13 : _GEN_183; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_222 = internal_valid ? _GEN_14 : _GEN_184; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_223 = internal_valid ? _GEN_15 : _GEN_185; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [3:0] _GEN_225 = internal_valid ? _rxMbUICounter_T_1 : _GEN_187; // @[src/main/scala/mbafe/MbAfe.scala 198:33 213:31]
  wire [16:0] _GEN_246 = internal_valid ? _GEN_0 : _GEN_208; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_247 = internal_valid ? _GEN_1 : _GEN_209; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_248 = internal_valid ? _GEN_2 : _GEN_210; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_249 = internal_valid ? _GEN_3 : _GEN_211; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_250 = internal_valid ? _GEN_4 : _GEN_212; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_251 = internal_valid ? _GEN_5 : _GEN_213; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_252 = internal_valid ? _GEN_6 : _GEN_214; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_253 = internal_valid ? _GEN_7 : _GEN_215; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_254 = internal_valid ? _GEN_8 : _GEN_216; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_255 = internal_valid ? _GEN_9 : _GEN_217; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_256 = internal_valid ? _GEN_10 : _GEN_218; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_257 = internal_valid ? _GEN_11 : _GEN_219; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_258 = internal_valid ? _GEN_12 : _GEN_220; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_259 = internal_valid ? _GEN_13 : _GEN_221; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_260 = internal_valid ? _GEN_14 : _GEN_222; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_261 = internal_valid ? _GEN_15 : _GEN_223; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [3:0] _GEN_263 = internal_valid ? _rxMbUICounter_T_1 : _GEN_225; // @[src/main/scala/mbafe/MbAfe.scala 198:33 213:31]
  wire [16:0] _GEN_284 = internal_valid ? _GEN_0 : _GEN_246; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_285 = internal_valid ? _GEN_1 : _GEN_247; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_286 = internal_valid ? _GEN_2 : _GEN_248; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_287 = internal_valid ? _GEN_3 : _GEN_249; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_288 = internal_valid ? _GEN_4 : _GEN_250; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_289 = internal_valid ? _GEN_5 : _GEN_251; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_290 = internal_valid ? _GEN_6 : _GEN_252; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_291 = internal_valid ? _GEN_7 : _GEN_253; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_292 = internal_valid ? _GEN_8 : _GEN_254; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_293 = internal_valid ? _GEN_9 : _GEN_255; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_294 = internal_valid ? _GEN_10 : _GEN_256; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_295 = internal_valid ? _GEN_11 : _GEN_257; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_296 = internal_valid ? _GEN_12 : _GEN_258; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_297 = internal_valid ? _GEN_13 : _GEN_259; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_298 = internal_valid ? _GEN_14 : _GEN_260; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_299 = internal_valid ? _GEN_15 : _GEN_261; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [3:0] _GEN_301 = internal_valid ? _rxMbUICounter_T_1 : _GEN_263; // @[src/main/scala/mbafe/MbAfe.scala 198:33 213:31]
  wire [16:0] _GEN_322 = internal_valid ? _GEN_0 : _GEN_284; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_323 = internal_valid ? _GEN_1 : _GEN_285; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_324 = internal_valid ? _GEN_2 : _GEN_286; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_325 = internal_valid ? _GEN_3 : _GEN_287; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_326 = internal_valid ? _GEN_4 : _GEN_288; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_327 = internal_valid ? _GEN_5 : _GEN_289; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_328 = internal_valid ? _GEN_6 : _GEN_290; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_329 = internal_valid ? _GEN_7 : _GEN_291; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_330 = internal_valid ? _GEN_8 : _GEN_292; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_331 = internal_valid ? _GEN_9 : _GEN_293; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_332 = internal_valid ? _GEN_10 : _GEN_294; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_333 = internal_valid ? _GEN_11 : _GEN_295; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_334 = internal_valid ? _GEN_12 : _GEN_296; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_335 = internal_valid ? _GEN_13 : _GEN_297; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_336 = internal_valid ? _GEN_14 : _GEN_298; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_337 = internal_valid ? _GEN_15 : _GEN_299; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [3:0] _GEN_339 = internal_valid ? _rxMbUICounter_T_1 : _GEN_301; // @[src/main/scala/mbafe/MbAfe.scala 198:33 213:31]
  wire [16:0] _GEN_360 = internal_valid ? _GEN_0 : _GEN_322; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_361 = internal_valid ? _GEN_1 : _GEN_323; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_362 = internal_valid ? _GEN_2 : _GEN_324; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_363 = internal_valid ? _GEN_3 : _GEN_325; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_364 = internal_valid ? _GEN_4 : _GEN_326; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_365 = internal_valid ? _GEN_5 : _GEN_327; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_366 = internal_valid ? _GEN_6 : _GEN_328; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_367 = internal_valid ? _GEN_7 : _GEN_329; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_368 = internal_valid ? _GEN_8 : _GEN_330; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_369 = internal_valid ? _GEN_9 : _GEN_331; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_370 = internal_valid ? _GEN_10 : _GEN_332; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_371 = internal_valid ? _GEN_11 : _GEN_333; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_372 = internal_valid ? _GEN_12 : _GEN_334; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_373 = internal_valid ? _GEN_13 : _GEN_335; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_374 = internal_valid ? _GEN_14 : _GEN_336; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_375 = internal_valid ? _GEN_15 : _GEN_337; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [3:0] _GEN_377 = internal_valid ? _rxMbUICounter_T_1 : _GEN_339; // @[src/main/scala/mbafe/MbAfe.scala 198:33 213:31]
  wire [16:0] _GEN_398 = internal_valid ? _GEN_0 : _GEN_360; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_399 = internal_valid ? _GEN_1 : _GEN_361; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_400 = internal_valid ? _GEN_2 : _GEN_362; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_401 = internal_valid ? _GEN_3 : _GEN_363; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_402 = internal_valid ? _GEN_4 : _GEN_364; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_403 = internal_valid ? _GEN_5 : _GEN_365; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_404 = internal_valid ? _GEN_6 : _GEN_366; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_405 = internal_valid ? _GEN_7 : _GEN_367; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_406 = internal_valid ? _GEN_8 : _GEN_368; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_407 = internal_valid ? _GEN_9 : _GEN_369; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_408 = internal_valid ? _GEN_10 : _GEN_370; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_409 = internal_valid ? _GEN_11 : _GEN_371; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_410 = internal_valid ? _GEN_12 : _GEN_372; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_411 = internal_valid ? _GEN_13 : _GEN_373; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_412 = internal_valid ? _GEN_14 : _GEN_374; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_413 = internal_valid ? _GEN_15 : _GEN_375; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [3:0] _GEN_415 = internal_valid ? _rxMbUICounter_T_1 : _GEN_377; // @[src/main/scala/mbafe/MbAfe.scala 198:33 213:31]
  wire [16:0] _GEN_436 = internal_valid ? _GEN_0 : _GEN_398; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_437 = internal_valid ? _GEN_1 : _GEN_399; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_438 = internal_valid ? _GEN_2 : _GEN_400; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_439 = internal_valid ? _GEN_3 : _GEN_401; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_440 = internal_valid ? _GEN_4 : _GEN_402; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_441 = internal_valid ? _GEN_5 : _GEN_403; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_442 = internal_valid ? _GEN_6 : _GEN_404; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_443 = internal_valid ? _GEN_7 : _GEN_405; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_444 = internal_valid ? _GEN_8 : _GEN_406; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_445 = internal_valid ? _GEN_9 : _GEN_407; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_446 = internal_valid ? _GEN_10 : _GEN_408; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_447 = internal_valid ? _GEN_11 : _GEN_409; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_448 = internal_valid ? _GEN_12 : _GEN_410; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_449 = internal_valid ? _GEN_13 : _GEN_411; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_450 = internal_valid ? _GEN_14 : _GEN_412; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_451 = internal_valid ? _GEN_15 : _GEN_413; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [3:0] _GEN_453 = internal_valid ? _rxMbUICounter_T_1 : _GEN_415; // @[src/main/scala/mbafe/MbAfe.scala 198:33 213:31]
  wire [16:0] _GEN_474 = internal_valid ? _GEN_0 : _GEN_436; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_475 = internal_valid ? _GEN_1 : _GEN_437; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_476 = internal_valid ? _GEN_2 : _GEN_438; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_477 = internal_valid ? _GEN_3 : _GEN_439; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_478 = internal_valid ? _GEN_4 : _GEN_440; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_479 = internal_valid ? _GEN_5 : _GEN_441; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_480 = internal_valid ? _GEN_6 : _GEN_442; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_481 = internal_valid ? _GEN_7 : _GEN_443; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_482 = internal_valid ? _GEN_8 : _GEN_444; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_483 = internal_valid ? _GEN_9 : _GEN_445; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_484 = internal_valid ? _GEN_10 : _GEN_446; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_485 = internal_valid ? _GEN_11 : _GEN_447; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_486 = internal_valid ? _GEN_12 : _GEN_448; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_487 = internal_valid ? _GEN_13 : _GEN_449; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_488 = internal_valid ? _GEN_14 : _GEN_450; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_489 = internal_valid ? _GEN_15 : _GEN_451; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [3:0] _GEN_491 = internal_valid ? _rxMbUICounter_T_1 : _GEN_453; // @[src/main/scala/mbafe/MbAfe.scala 198:33 213:31]
  wire [16:0] _GEN_512 = internal_valid ? _GEN_0 : _GEN_474; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_513 = internal_valid ? _GEN_1 : _GEN_475; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_514 = internal_valid ? _GEN_2 : _GEN_476; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_515 = internal_valid ? _GEN_3 : _GEN_477; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_516 = internal_valid ? _GEN_4 : _GEN_478; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_517 = internal_valid ? _GEN_5 : _GEN_479; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_518 = internal_valid ? _GEN_6 : _GEN_480; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_519 = internal_valid ? _GEN_7 : _GEN_481; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_520 = internal_valid ? _GEN_8 : _GEN_482; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_521 = internal_valid ? _GEN_9 : _GEN_483; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_522 = internal_valid ? _GEN_10 : _GEN_484; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_523 = internal_valid ? _GEN_11 : _GEN_485; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_524 = internal_valid ? _GEN_12 : _GEN_486; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_525 = internal_valid ? _GEN_13 : _GEN_487; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_526 = internal_valid ? _GEN_14 : _GEN_488; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_527 = internal_valid ? _GEN_15 : _GEN_489; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_550 = internal_valid ? _GEN_0 : _GEN_512; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_551 = internal_valid ? _GEN_1 : _GEN_513; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_552 = internal_valid ? _GEN_2 : _GEN_514; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_553 = internal_valid ? _GEN_3 : _GEN_515; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_554 = internal_valid ? _GEN_4 : _GEN_516; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_555 = internal_valid ? _GEN_5 : _GEN_517; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_556 = internal_valid ? _GEN_6 : _GEN_518; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_557 = internal_valid ? _GEN_7 : _GEN_519; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_558 = internal_valid ? _GEN_8 : _GEN_520; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_559 = internal_valid ? _GEN_9 : _GEN_521; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_560 = internal_valid ? _GEN_10 : _GEN_522; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_561 = internal_valid ? _GEN_11 : _GEN_523; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_562 = internal_valid ? _GEN_12 : _GEN_524; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_563 = internal_valid ? _GEN_13 : _GEN_525; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_564 = internal_valid ? _GEN_14 : _GEN_526; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_565 = internal_valid ? _GEN_15 : _GEN_527; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_588 = internal_valid ? _GEN_0 : _GEN_550; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_589 = internal_valid ? _GEN_1 : _GEN_551; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_590 = internal_valid ? _GEN_2 : _GEN_552; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_591 = internal_valid ? _GEN_3 : _GEN_553; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_592 = internal_valid ? _GEN_4 : _GEN_554; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_593 = internal_valid ? _GEN_5 : _GEN_555; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_594 = internal_valid ? _GEN_6 : _GEN_556; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_595 = internal_valid ? _GEN_7 : _GEN_557; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_596 = internal_valid ? _GEN_8 : _GEN_558; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_597 = internal_valid ? _GEN_9 : _GEN_559; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_598 = internal_valid ? _GEN_10 : _GEN_560; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_599 = internal_valid ? _GEN_11 : _GEN_561; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_600 = internal_valid ? _GEN_12 : _GEN_562; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_601 = internal_valid ? _GEN_13 : _GEN_563; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_602 = internal_valid ? _GEN_14 : _GEN_564; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_603 = internal_valid ? _GEN_15 : _GEN_565; // @[src/main/scala/mbafe/MbAfe.scala 198:33]
  wire [16:0] _GEN_864 = reset ? 17'h0 : _GEN_588; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_865 = reset ? 17'h0 : _GEN_589; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_866 = reset ? 17'h0 : _GEN_590; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_867 = reset ? 17'h0 : _GEN_591; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_868 = reset ? 17'h0 : _GEN_592; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_869 = reset ? 17'h0 : _GEN_593; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_870 = reset ? 17'h0 : _GEN_594; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_871 = reset ? 17'h0 : _GEN_595; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_872 = reset ? 17'h0 : _GEN_596; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_873 = reset ? 17'h0 : _GEN_597; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_874 = reset ? 17'h0 : _GEN_598; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_875 = reset ? 17'h0 : _GEN_599; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_876 = reset ? 17'h0 : _GEN_600; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_877 = reset ? 17'h0 : _GEN_601; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_878 = reset ? 17'h0 : _GEN_602; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  wire [16:0] _GEN_879 = reset ? 17'h0 : _GEN_603; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
  AsyncQueue rxMbFifos_0 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_0_io_enq_clock),
    .io_enq_reset(rxMbFifos_0_io_enq_reset),
    .io_enq_ready(rxMbFifos_0_io_enq_ready),
    .io_enq_valid(rxMbFifos_0_io_enq_valid),
    .io_enq_bits(rxMbFifos_0_io_enq_bits),
    .io_deq_clock(rxMbFifos_0_io_deq_clock),
    .io_deq_reset(rxMbFifos_0_io_deq_reset),
    .io_deq_ready(rxMbFifos_0_io_deq_ready),
    .io_deq_valid(rxMbFifos_0_io_deq_valid),
    .io_deq_bits(rxMbFifos_0_io_deq_bits)
  );
  AsyncQueue rxMbFifos_1 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_1_io_enq_clock),
    .io_enq_reset(rxMbFifos_1_io_enq_reset),
    .io_enq_ready(rxMbFifos_1_io_enq_ready),
    .io_enq_valid(rxMbFifos_1_io_enq_valid),
    .io_enq_bits(rxMbFifos_1_io_enq_bits),
    .io_deq_clock(rxMbFifos_1_io_deq_clock),
    .io_deq_reset(rxMbFifos_1_io_deq_reset),
    .io_deq_ready(rxMbFifos_1_io_deq_ready),
    .io_deq_valid(rxMbFifos_1_io_deq_valid),
    .io_deq_bits(rxMbFifos_1_io_deq_bits)
  );
  AsyncQueue rxMbFifos_2 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_2_io_enq_clock),
    .io_enq_reset(rxMbFifos_2_io_enq_reset),
    .io_enq_ready(rxMbFifos_2_io_enq_ready),
    .io_enq_valid(rxMbFifos_2_io_enq_valid),
    .io_enq_bits(rxMbFifos_2_io_enq_bits),
    .io_deq_clock(rxMbFifos_2_io_deq_clock),
    .io_deq_reset(rxMbFifos_2_io_deq_reset),
    .io_deq_ready(rxMbFifos_2_io_deq_ready),
    .io_deq_valid(rxMbFifos_2_io_deq_valid),
    .io_deq_bits(rxMbFifos_2_io_deq_bits)
  );
  AsyncQueue rxMbFifos_3 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_3_io_enq_clock),
    .io_enq_reset(rxMbFifos_3_io_enq_reset),
    .io_enq_ready(rxMbFifos_3_io_enq_ready),
    .io_enq_valid(rxMbFifos_3_io_enq_valid),
    .io_enq_bits(rxMbFifos_3_io_enq_bits),
    .io_deq_clock(rxMbFifos_3_io_deq_clock),
    .io_deq_reset(rxMbFifos_3_io_deq_reset),
    .io_deq_ready(rxMbFifos_3_io_deq_ready),
    .io_deq_valid(rxMbFifos_3_io_deq_valid),
    .io_deq_bits(rxMbFifos_3_io_deq_bits)
  );
  AsyncQueue rxMbFifos_4 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_4_io_enq_clock),
    .io_enq_reset(rxMbFifos_4_io_enq_reset),
    .io_enq_ready(rxMbFifos_4_io_enq_ready),
    .io_enq_valid(rxMbFifos_4_io_enq_valid),
    .io_enq_bits(rxMbFifos_4_io_enq_bits),
    .io_deq_clock(rxMbFifos_4_io_deq_clock),
    .io_deq_reset(rxMbFifos_4_io_deq_reset),
    .io_deq_ready(rxMbFifos_4_io_deq_ready),
    .io_deq_valid(rxMbFifos_4_io_deq_valid),
    .io_deq_bits(rxMbFifos_4_io_deq_bits)
  );
  AsyncQueue rxMbFifos_5 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_5_io_enq_clock),
    .io_enq_reset(rxMbFifos_5_io_enq_reset),
    .io_enq_ready(rxMbFifos_5_io_enq_ready),
    .io_enq_valid(rxMbFifos_5_io_enq_valid),
    .io_enq_bits(rxMbFifos_5_io_enq_bits),
    .io_deq_clock(rxMbFifos_5_io_deq_clock),
    .io_deq_reset(rxMbFifos_5_io_deq_reset),
    .io_deq_ready(rxMbFifos_5_io_deq_ready),
    .io_deq_valid(rxMbFifos_5_io_deq_valid),
    .io_deq_bits(rxMbFifos_5_io_deq_bits)
  );
  AsyncQueue rxMbFifos_6 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_6_io_enq_clock),
    .io_enq_reset(rxMbFifos_6_io_enq_reset),
    .io_enq_ready(rxMbFifos_6_io_enq_ready),
    .io_enq_valid(rxMbFifos_6_io_enq_valid),
    .io_enq_bits(rxMbFifos_6_io_enq_bits),
    .io_deq_clock(rxMbFifos_6_io_deq_clock),
    .io_deq_reset(rxMbFifos_6_io_deq_reset),
    .io_deq_ready(rxMbFifos_6_io_deq_ready),
    .io_deq_valid(rxMbFifos_6_io_deq_valid),
    .io_deq_bits(rxMbFifos_6_io_deq_bits)
  );
  AsyncQueue rxMbFifos_7 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_7_io_enq_clock),
    .io_enq_reset(rxMbFifos_7_io_enq_reset),
    .io_enq_ready(rxMbFifos_7_io_enq_ready),
    .io_enq_valid(rxMbFifos_7_io_enq_valid),
    .io_enq_bits(rxMbFifos_7_io_enq_bits),
    .io_deq_clock(rxMbFifos_7_io_deq_clock),
    .io_deq_reset(rxMbFifos_7_io_deq_reset),
    .io_deq_ready(rxMbFifos_7_io_deq_ready),
    .io_deq_valid(rxMbFifos_7_io_deq_valid),
    .io_deq_bits(rxMbFifos_7_io_deq_bits)
  );
  AsyncQueue rxMbFifos_8 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_8_io_enq_clock),
    .io_enq_reset(rxMbFifos_8_io_enq_reset),
    .io_enq_ready(rxMbFifos_8_io_enq_ready),
    .io_enq_valid(rxMbFifos_8_io_enq_valid),
    .io_enq_bits(rxMbFifos_8_io_enq_bits),
    .io_deq_clock(rxMbFifos_8_io_deq_clock),
    .io_deq_reset(rxMbFifos_8_io_deq_reset),
    .io_deq_ready(rxMbFifos_8_io_deq_ready),
    .io_deq_valid(rxMbFifos_8_io_deq_valid),
    .io_deq_bits(rxMbFifos_8_io_deq_bits)
  );
  AsyncQueue rxMbFifos_9 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_9_io_enq_clock),
    .io_enq_reset(rxMbFifos_9_io_enq_reset),
    .io_enq_ready(rxMbFifos_9_io_enq_ready),
    .io_enq_valid(rxMbFifos_9_io_enq_valid),
    .io_enq_bits(rxMbFifos_9_io_enq_bits),
    .io_deq_clock(rxMbFifos_9_io_deq_clock),
    .io_deq_reset(rxMbFifos_9_io_deq_reset),
    .io_deq_ready(rxMbFifos_9_io_deq_ready),
    .io_deq_valid(rxMbFifos_9_io_deq_valid),
    .io_deq_bits(rxMbFifos_9_io_deq_bits)
  );
  AsyncQueue rxMbFifos_10 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_10_io_enq_clock),
    .io_enq_reset(rxMbFifos_10_io_enq_reset),
    .io_enq_ready(rxMbFifos_10_io_enq_ready),
    .io_enq_valid(rxMbFifos_10_io_enq_valid),
    .io_enq_bits(rxMbFifos_10_io_enq_bits),
    .io_deq_clock(rxMbFifos_10_io_deq_clock),
    .io_deq_reset(rxMbFifos_10_io_deq_reset),
    .io_deq_ready(rxMbFifos_10_io_deq_ready),
    .io_deq_valid(rxMbFifos_10_io_deq_valid),
    .io_deq_bits(rxMbFifos_10_io_deq_bits)
  );
  AsyncQueue rxMbFifos_11 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_11_io_enq_clock),
    .io_enq_reset(rxMbFifos_11_io_enq_reset),
    .io_enq_ready(rxMbFifos_11_io_enq_ready),
    .io_enq_valid(rxMbFifos_11_io_enq_valid),
    .io_enq_bits(rxMbFifos_11_io_enq_bits),
    .io_deq_clock(rxMbFifos_11_io_deq_clock),
    .io_deq_reset(rxMbFifos_11_io_deq_reset),
    .io_deq_ready(rxMbFifos_11_io_deq_ready),
    .io_deq_valid(rxMbFifos_11_io_deq_valid),
    .io_deq_bits(rxMbFifos_11_io_deq_bits)
  );
  AsyncQueue rxMbFifos_12 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_12_io_enq_clock),
    .io_enq_reset(rxMbFifos_12_io_enq_reset),
    .io_enq_ready(rxMbFifos_12_io_enq_ready),
    .io_enq_valid(rxMbFifos_12_io_enq_valid),
    .io_enq_bits(rxMbFifos_12_io_enq_bits),
    .io_deq_clock(rxMbFifos_12_io_deq_clock),
    .io_deq_reset(rxMbFifos_12_io_deq_reset),
    .io_deq_ready(rxMbFifos_12_io_deq_ready),
    .io_deq_valid(rxMbFifos_12_io_deq_valid),
    .io_deq_bits(rxMbFifos_12_io_deq_bits)
  );
  AsyncQueue rxMbFifos_13 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_13_io_enq_clock),
    .io_enq_reset(rxMbFifos_13_io_enq_reset),
    .io_enq_ready(rxMbFifos_13_io_enq_ready),
    .io_enq_valid(rxMbFifos_13_io_enq_valid),
    .io_enq_bits(rxMbFifos_13_io_enq_bits),
    .io_deq_clock(rxMbFifos_13_io_deq_clock),
    .io_deq_reset(rxMbFifos_13_io_deq_reset),
    .io_deq_ready(rxMbFifos_13_io_deq_ready),
    .io_deq_valid(rxMbFifos_13_io_deq_valid),
    .io_deq_bits(rxMbFifos_13_io_deq_bits)
  );
  AsyncQueue rxMbFifos_14 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_14_io_enq_clock),
    .io_enq_reset(rxMbFifos_14_io_enq_reset),
    .io_enq_ready(rxMbFifos_14_io_enq_ready),
    .io_enq_valid(rxMbFifos_14_io_enq_valid),
    .io_enq_bits(rxMbFifos_14_io_enq_bits),
    .io_deq_clock(rxMbFifos_14_io_deq_clock),
    .io_deq_reset(rxMbFifos_14_io_deq_reset),
    .io_deq_ready(rxMbFifos_14_io_deq_ready),
    .io_deq_valid(rxMbFifos_14_io_deq_valid),
    .io_deq_bits(rxMbFifos_14_io_deq_bits)
  );
  AsyncQueue rxMbFifos_15 ( // @[src/main/scala/mbafe/MbAfe.scala 160:44]
    .io_enq_clock(rxMbFifos_15_io_enq_clock),
    .io_enq_reset(rxMbFifos_15_io_enq_reset),
    .io_enq_ready(rxMbFifos_15_io_enq_ready),
    .io_enq_valid(rxMbFifos_15_io_enq_valid),
    .io_enq_bits(rxMbFifos_15_io_enq_bits),
    .io_deq_clock(rxMbFifos_15_io_deq_clock),
    .io_deq_reset(rxMbFifos_15_io_deq_reset),
    .io_deq_ready(rxMbFifos_15_io_deq_ready),
    .io_deq_valid(rxMbFifos_15_io_deq_valid),
    .io_deq_bits(rxMbFifos_15_io_deq_bits)
  );
  assign io_txMbAfe_valid = rxMbFifos_15_io_deq_valid; // @[src/main/scala/mbafe/MbAfe.scala 232:31]
  assign io_txMbAfe_bits_0 = rxMbFifos_0_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_1 = rxMbFifos_1_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_2 = rxMbFifos_2_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_3 = rxMbFifos_3_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_4 = rxMbFifos_4_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_5 = rxMbFifos_5_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_6 = rxMbFifos_6_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_7 = rxMbFifos_7_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_8 = rxMbFifos_8_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_9 = rxMbFifos_9_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_10 = rxMbFifos_10_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_11 = rxMbFifos_11_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_12 = rxMbFifos_12_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_13 = rxMbFifos_13_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_14 = rxMbFifos_14_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_txMbAfe_bits_15 = rxMbFifos_15_io_deq_bits; // @[src/main/scala/mbafe/MbAfe.scala 231:33]
  assign io_clkn_out = io_rxMbIo_clkn; // @[src/main/scala/mbafe/MbAfe.scala 152:17]
  assign rxMbFifos_0_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_0_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_0_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_0_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_0_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_0_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_0_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_1_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_1_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_1_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_1_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_1_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_1_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_1_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_2_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_2_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_2_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_2_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_2_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_2_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_2_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_3_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_3_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_3_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_3_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_3_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_3_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_3_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_4_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_4_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_4_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_4_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_4_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_4_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_4_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_5_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_5_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_5_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_5_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_5_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_5_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_5_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_6_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_6_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_6_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_6_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_6_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_6_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_6_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_7_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_7_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_7_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_7_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_7_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_7_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_7_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_8_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_8_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_8_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_8_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_8_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_8_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_8_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_9_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_9_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_9_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_9_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_9_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_9_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_9_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_10_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_10_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_10_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_10_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_10_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_10_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_10_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_11_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_11_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_11_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_11_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_11_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_11_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_11_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_12_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_12_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_12_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_12_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_12_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_12_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_12_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_13_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_13_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_13_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_13_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_13_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_13_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_13_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_14_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_14_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_14_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_14_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_14_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_14_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_14_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  assign rxMbFifos_15_io_enq_clock = io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 190:35]
  assign rxMbFifos_15_io_enq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 191:35]
  assign rxMbFifos_15_io_enq_valid = _fifo_enq_valid_next_T_2 ^ fifo_enq_valid_next; // @[src/main/scala/mbafe/MbAfe.scala 217:17]
  assign rxMbFifos_15_io_enq_bits = _GEN_37[15:0];
  assign rxMbFifos_15_io_deq_clock = clock; // @[src/main/scala/mbafe/MbAfe.scala 229:35]
  assign rxMbFifos_15_io_deq_reset = reset; // @[src/main/scala/mbafe/MbAfe.scala 230:35]
  assign rxMbFifos_15_io_deq_ready = io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 233:35]
  always @(posedge io_rxMbIo_clkp or posedge io_rxMbIo_clkn) begin
    mbIoValid_pipe_0 <= io_rxMbIo_valid; // @[src/main/scala/mbafe/MbAfe.scala 169:39]
    mbIoValid_pipe_1 <= mbIoValid_pipe_0; // @[src/main/scala/mbafe/MbAfe.scala 170:39]
    mbIoValid_pipe_2 <= mbIoValid_pipe_1; // @[src/main/scala/mbafe/MbAfe.scala 171:39]
    mbIoValid_next <= mbIoValid_pipe_2; // @[src/main/scala/mbafe/MbAfe.scala 172:37]
    rxMbShiftRegs_0 <= _GEN_864[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_1 <= _GEN_865[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_2 <= _GEN_866[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_3 <= _GEN_867[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_4 <= _GEN_868[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_5 <= _GEN_869[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_6 <= _GEN_870[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_7 <= _GEN_871[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_8 <= _GEN_872[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_9 <= _GEN_873[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_10 <= _GEN_874[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_11 <= _GEN_875[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_12 <= _GEN_876[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_13 <= _GEN_877[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_14 <= _GEN_878[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    rxMbShiftRegs_15 <= _GEN_879[15:0]; // @[src/main/scala/mbafe/MbAfe.scala 175:{52,52}]
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_0 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_0 <= rxMbShiftRegs_0; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_0 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_1 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_1 <= rxMbShiftRegs_1; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_1 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_2 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_2 <= rxMbShiftRegs_2; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_2 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_3 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_3 <= rxMbShiftRegs_3; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_3 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_4 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_4 <= rxMbShiftRegs_4; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_4 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_5 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_5 <= rxMbShiftRegs_5; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_5 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_6 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_6 <= rxMbShiftRegs_6; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_6 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_7 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_7 <= rxMbShiftRegs_7; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_7 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_8 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_8 <= rxMbShiftRegs_8; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_8 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_9 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_9 <= rxMbShiftRegs_9; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_9 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_10 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_10 <= rxMbShiftRegs_10; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_10 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_11 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_11 <= rxMbShiftRegs_11; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_11 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_12 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_12 <= rxMbShiftRegs_12; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_12 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_13 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_13 <= rxMbShiftRegs_13; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_13 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_14 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_14 <= rxMbShiftRegs_14; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_14 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 176:57]
      rxMbShiftRegs_next_15 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 176:57]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      if (_fifo_enq_valid_next_T_1) begin // @[src/main/scala/mbafe/MbAfe.scala 200:45]
        rxMbShiftRegs_next_15 <= rxMbShiftRegs_15; // @[src/main/scala/mbafe/MbAfe.scala 204:43]
      end else begin
        rxMbShiftRegs_next_15 <= 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 210:43]
      end
    end
    if (reset) begin // @[src/main/scala/mbafe/MbAfe.scala 179:36]
      rxMbUICounter <= 4'h0; // @[src/main/scala/mbafe/MbAfe.scala 179:36]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      rxMbUICounter <= _rxMbUICounter_T_1; // @[src/main/scala/mbafe/MbAfe.scala 213:31]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      rxMbUICounter <= _rxMbUICounter_T_1; // @[src/main/scala/mbafe/MbAfe.scala 213:31]
    end else if (internal_valid) begin // @[src/main/scala/mbafe/MbAfe.scala 198:33]
      rxMbUICounter <= _rxMbUICounter_T_1; // @[src/main/scala/mbafe/MbAfe.scala 213:31]
    end else begin
      rxMbUICounter <= _GEN_491;
    end
    rxMbUICounter_next <= rxMbUICounter; // @[src/main/scala/mbafe/MbAfe.scala 181:41]
    fifo_enq_valid_next <= rxMbUICounter_next == 4'hf & rxMbUICounter == 4'h0; // @[src/main/scala/mbafe/MbAfe.scala 185:78]
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  mbIoValid_pipe_0 = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  mbIoValid_pipe_1 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  mbIoValid_pipe_2 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  mbIoValid_next = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  rxMbShiftRegs_0 = _RAND_4[15:0];
  _RAND_5 = {1{`RANDOM}};
  rxMbShiftRegs_1 = _RAND_5[15:0];
  _RAND_6 = {1{`RANDOM}};
  rxMbShiftRegs_2 = _RAND_6[15:0];
  _RAND_7 = {1{`RANDOM}};
  rxMbShiftRegs_3 = _RAND_7[15:0];
  _RAND_8 = {1{`RANDOM}};
  rxMbShiftRegs_4 = _RAND_8[15:0];
  _RAND_9 = {1{`RANDOM}};
  rxMbShiftRegs_5 = _RAND_9[15:0];
  _RAND_10 = {1{`RANDOM}};
  rxMbShiftRegs_6 = _RAND_10[15:0];
  _RAND_11 = {1{`RANDOM}};
  rxMbShiftRegs_7 = _RAND_11[15:0];
  _RAND_12 = {1{`RANDOM}};
  rxMbShiftRegs_8 = _RAND_12[15:0];
  _RAND_13 = {1{`RANDOM}};
  rxMbShiftRegs_9 = _RAND_13[15:0];
  _RAND_14 = {1{`RANDOM}};
  rxMbShiftRegs_10 = _RAND_14[15:0];
  _RAND_15 = {1{`RANDOM}};
  rxMbShiftRegs_11 = _RAND_15[15:0];
  _RAND_16 = {1{`RANDOM}};
  rxMbShiftRegs_12 = _RAND_16[15:0];
  _RAND_17 = {1{`RANDOM}};
  rxMbShiftRegs_13 = _RAND_17[15:0];
  _RAND_18 = {1{`RANDOM}};
  rxMbShiftRegs_14 = _RAND_18[15:0];
  _RAND_19 = {1{`RANDOM}};
  rxMbShiftRegs_15 = _RAND_19[15:0];
  _RAND_20 = {1{`RANDOM}};
  rxMbShiftRegs_next_0 = _RAND_20[15:0];
  _RAND_21 = {1{`RANDOM}};
  rxMbShiftRegs_next_1 = _RAND_21[15:0];
  _RAND_22 = {1{`RANDOM}};
  rxMbShiftRegs_next_2 = _RAND_22[15:0];
  _RAND_23 = {1{`RANDOM}};
  rxMbShiftRegs_next_3 = _RAND_23[15:0];
  _RAND_24 = {1{`RANDOM}};
  rxMbShiftRegs_next_4 = _RAND_24[15:0];
  _RAND_25 = {1{`RANDOM}};
  rxMbShiftRegs_next_5 = _RAND_25[15:0];
  _RAND_26 = {1{`RANDOM}};
  rxMbShiftRegs_next_6 = _RAND_26[15:0];
  _RAND_27 = {1{`RANDOM}};
  rxMbShiftRegs_next_7 = _RAND_27[15:0];
  _RAND_28 = {1{`RANDOM}};
  rxMbShiftRegs_next_8 = _RAND_28[15:0];
  _RAND_29 = {1{`RANDOM}};
  rxMbShiftRegs_next_9 = _RAND_29[15:0];
  _RAND_30 = {1{`RANDOM}};
  rxMbShiftRegs_next_10 = _RAND_30[15:0];
  _RAND_31 = {1{`RANDOM}};
  rxMbShiftRegs_next_11 = _RAND_31[15:0];
  _RAND_32 = {1{`RANDOM}};
  rxMbShiftRegs_next_12 = _RAND_32[15:0];
  _RAND_33 = {1{`RANDOM}};
  rxMbShiftRegs_next_13 = _RAND_33[15:0];
  _RAND_34 = {1{`RANDOM}};
  rxMbShiftRegs_next_14 = _RAND_34[15:0];
  _RAND_35 = {1{`RANDOM}};
  rxMbShiftRegs_next_15 = _RAND_35[15:0];
  _RAND_36 = {1{`RANDOM}};
  rxMbUICounter = _RAND_36[3:0];
  _RAND_37 = {1{`RANDOM}};
  rxMbUICounter_next = _RAND_37[3:0];
  _RAND_38 = {1{`RANDOM}};
  fifo_enq_valid_next = _RAND_38[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module PhyTest(
  input         clock,
  input         reset,
  input         io_tx_user_fifoParams_clk, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input         io_tx_user_fifoParams_reset, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input         io_tx_user_txData_ready, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output        io_tx_user_txData_valid, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_0, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_1, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_2, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_3, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_4, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_5, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_6, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_7, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_8, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_9, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_10, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_11, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_12, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_13, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_14, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_tx_user_txData_bits_15, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output        io_tx_user_rxData_ready, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input         io_tx_user_rxData_valid, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_0, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_1, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_2, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_3, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_4, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_5, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_6, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_7, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_8, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_9, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_10, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_11, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_12, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_13, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_14, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_tx_user_rxData_bits_15, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [2:0]  io_tx_user_txFreqSel, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output        io_tx_user_rxEn, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input         io_tx_user_pllLock, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input         io_rx_user_fifoParams_clk, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input         io_rx_user_fifoParams_reset, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input         io_rx_user_txData_ready, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output        io_rx_user_txData_valid, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_0, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_1, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_2, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_3, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_4, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_5, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_6, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_7, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_8, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_9, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_10, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_11, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_12, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_13, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_14, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [15:0] io_rx_user_txData_bits_15, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output        io_rx_user_rxData_ready, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input         io_rx_user_rxData_valid, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_0, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_1, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_2, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_3, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_4, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_5, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_6, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_7, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_8, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_9, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_10, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_11, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_12, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_13, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_14, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input  [15:0] io_rx_user_rxData_bits_15, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output [2:0]  io_rx_user_txFreqSel, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output        io_rx_user_rxEn, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input         io_rx_user_pllLock, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input         io_clkp, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  input         io_clkn, // @[src/main/scala/mbafe/MbAfe.scala 352:16]
  output        io_clkn_out // @[src/main/scala/mbafe/MbAfe.scala 352:16]
);
  wire  sender_clock; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire  sender_reset; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire  sender_io_rxMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire  sender_io_rxMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_0; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_1; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_2; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_3; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_4; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_5; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_6; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_7; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_8; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_9; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_10; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_11; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_12; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_13; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_14; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_rxMbAfe_bits_15; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire [15:0] sender_io_txMbIo_data; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire  sender_io_txMbIo_valid; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire  sender_io_txMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire  sender_io_txMbIo_clkn; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire  sender_io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire  sender_io_clkn; // @[src/main/scala/mbafe/MbAfe.scala 363:24]
  wire  receiver_clock; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire  receiver_reset; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_rxMbIo_data; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire  receiver_io_rxMbIo_valid; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire  receiver_io_rxMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire  receiver_io_rxMbIo_clkn; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire  receiver_io_txMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire  receiver_io_txMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_0; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_1; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_2; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_3; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_4; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_5; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_6; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_7; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_8; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_9; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_10; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_11; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_12; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_13; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_14; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire [15:0] receiver_io_txMbAfe_bits_15; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  wire  receiver_io_clkn_out; // @[src/main/scala/mbafe/MbAfe.scala 364:26]
  TxMainband sender ( // @[src/main/scala/mbafe/MbAfe.scala 363:24]
    .clock(sender_clock),
    .reset(sender_reset),
    .io_rxMbAfe_ready(sender_io_rxMbAfe_ready),
    .io_rxMbAfe_valid(sender_io_rxMbAfe_valid),
    .io_rxMbAfe_bits_0(sender_io_rxMbAfe_bits_0),
    .io_rxMbAfe_bits_1(sender_io_rxMbAfe_bits_1),
    .io_rxMbAfe_bits_2(sender_io_rxMbAfe_bits_2),
    .io_rxMbAfe_bits_3(sender_io_rxMbAfe_bits_3),
    .io_rxMbAfe_bits_4(sender_io_rxMbAfe_bits_4),
    .io_rxMbAfe_bits_5(sender_io_rxMbAfe_bits_5),
    .io_rxMbAfe_bits_6(sender_io_rxMbAfe_bits_6),
    .io_rxMbAfe_bits_7(sender_io_rxMbAfe_bits_7),
    .io_rxMbAfe_bits_8(sender_io_rxMbAfe_bits_8),
    .io_rxMbAfe_bits_9(sender_io_rxMbAfe_bits_9),
    .io_rxMbAfe_bits_10(sender_io_rxMbAfe_bits_10),
    .io_rxMbAfe_bits_11(sender_io_rxMbAfe_bits_11),
    .io_rxMbAfe_bits_12(sender_io_rxMbAfe_bits_12),
    .io_rxMbAfe_bits_13(sender_io_rxMbAfe_bits_13),
    .io_rxMbAfe_bits_14(sender_io_rxMbAfe_bits_14),
    .io_rxMbAfe_bits_15(sender_io_rxMbAfe_bits_15),
    .io_txMbIo_data(sender_io_txMbIo_data),
    .io_txMbIo_valid(sender_io_txMbIo_valid),
    .io_txMbIo_clkp(sender_io_txMbIo_clkp),
    .io_txMbIo_clkn(sender_io_txMbIo_clkn),
    .io_clkp(sender_io_clkp),
    .io_clkn(sender_io_clkn)
  );
  RxMainband receiver ( // @[src/main/scala/mbafe/MbAfe.scala 364:26]
    .clock(receiver_clock),
    .reset(receiver_reset),
    .io_rxMbIo_data(receiver_io_rxMbIo_data),
    .io_rxMbIo_valid(receiver_io_rxMbIo_valid),
    .io_rxMbIo_clkp(receiver_io_rxMbIo_clkp),
    .io_rxMbIo_clkn(receiver_io_rxMbIo_clkn),
    .io_txMbAfe_ready(receiver_io_txMbAfe_ready),
    .io_txMbAfe_valid(receiver_io_txMbAfe_valid),
    .io_txMbAfe_bits_0(receiver_io_txMbAfe_bits_0),
    .io_txMbAfe_bits_1(receiver_io_txMbAfe_bits_1),
    .io_txMbAfe_bits_2(receiver_io_txMbAfe_bits_2),
    .io_txMbAfe_bits_3(receiver_io_txMbAfe_bits_3),
    .io_txMbAfe_bits_4(receiver_io_txMbAfe_bits_4),
    .io_txMbAfe_bits_5(receiver_io_txMbAfe_bits_5),
    .io_txMbAfe_bits_6(receiver_io_txMbAfe_bits_6),
    .io_txMbAfe_bits_7(receiver_io_txMbAfe_bits_7),
    .io_txMbAfe_bits_8(receiver_io_txMbAfe_bits_8),
    .io_txMbAfe_bits_9(receiver_io_txMbAfe_bits_9),
    .io_txMbAfe_bits_10(receiver_io_txMbAfe_bits_10),
    .io_txMbAfe_bits_11(receiver_io_txMbAfe_bits_11),
    .io_txMbAfe_bits_12(receiver_io_txMbAfe_bits_12),
    .io_txMbAfe_bits_13(receiver_io_txMbAfe_bits_13),
    .io_txMbAfe_bits_14(receiver_io_txMbAfe_bits_14),
    .io_txMbAfe_bits_15(receiver_io_txMbAfe_bits_15),
    .io_clkn_out(receiver_io_clkn_out)
  );
  assign io_tx_user_txData_valid = 1'h0; // @[src/main/scala/mbafe/MbAfe.scala 378:29]
  assign io_tx_user_txData_bits_0 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_1 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_2 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_3 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_4 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_5 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_6 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_7 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_8 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_9 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_10 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_11 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_12 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_13 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_14 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_txData_bits_15 = 16'h0; // @[src/main/scala/mbafe/MbAfe.scala 377:28]
  assign io_tx_user_rxData_ready = sender_io_rxMbAfe_ready; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign io_tx_user_txFreqSel = 3'h3; // @[src/main/scala/mbafe/MbAfe.scala 381:26]
  assign io_tx_user_rxEn = 1'h0; // @[src/main/scala/mbafe/MbAfe.scala 382:21]
  assign io_rx_user_txData_valid = receiver_io_txMbAfe_valid; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_0 = receiver_io_txMbAfe_bits_0; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_1 = receiver_io_txMbAfe_bits_1; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_2 = receiver_io_txMbAfe_bits_2; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_3 = receiver_io_txMbAfe_bits_3; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_4 = receiver_io_txMbAfe_bits_4; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_5 = receiver_io_txMbAfe_bits_5; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_6 = receiver_io_txMbAfe_bits_6; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_7 = receiver_io_txMbAfe_bits_7; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_8 = receiver_io_txMbAfe_bits_8; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_9 = receiver_io_txMbAfe_bits_9; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_10 = receiver_io_txMbAfe_bits_10; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_11 = receiver_io_txMbAfe_bits_11; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_12 = receiver_io_txMbAfe_bits_12; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_13 = receiver_io_txMbAfe_bits_13; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_14 = receiver_io_txMbAfe_bits_14; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_txData_bits_15 = receiver_io_txMbAfe_bits_15; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
  assign io_rx_user_rxData_ready = 1'h0; // @[src/main/scala/mbafe/MbAfe.scala 379:29]
  assign io_rx_user_txFreqSel = 3'h3; // @[src/main/scala/mbafe/MbAfe.scala 383:26]
  assign io_rx_user_rxEn = 1'h0; // @[src/main/scala/mbafe/MbAfe.scala 384:21]
  assign io_clkn_out = receiver_io_clkn_out; // @[src/main/scala/mbafe/MbAfe.scala 375:17]
  assign sender_clock = clock;
  assign sender_reset = reset;
  assign sender_io_rxMbAfe_valid = io_tx_user_rxData_valid; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_0 = io_tx_user_rxData_bits_0; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_1 = io_tx_user_rxData_bits_1; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_2 = io_tx_user_rxData_bits_2; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_3 = io_tx_user_rxData_bits_3; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_4 = io_tx_user_rxData_bits_4; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_5 = io_tx_user_rxData_bits_5; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_6 = io_tx_user_rxData_bits_6; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_7 = io_tx_user_rxData_bits_7; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_8 = io_tx_user_rxData_bits_8; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_9 = io_tx_user_rxData_bits_9; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_10 = io_tx_user_rxData_bits_10; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_11 = io_tx_user_rxData_bits_11; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_12 = io_tx_user_rxData_bits_12; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_13 = io_tx_user_rxData_bits_13; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_14 = io_tx_user_rxData_bits_14; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_rxMbAfe_bits_15 = io_tx_user_rxData_bits_15; // @[src/main/scala/mbafe/MbAfe.scala 365:23]
  assign sender_io_clkp = io_clkp; // @[src/main/scala/mbafe/MbAfe.scala 367:20]
  assign sender_io_clkn = io_clkn; // @[src/main/scala/mbafe/MbAfe.scala 368:20]
  assign receiver_clock = clock;
  assign receiver_reset = reset;
  assign receiver_io_rxMbIo_data = sender_io_txMbIo_data; // @[src/main/scala/mbafe/MbAfe.scala 366:23]
  assign receiver_io_rxMbIo_valid = sender_io_txMbIo_valid; // @[src/main/scala/mbafe/MbAfe.scala 366:23]
  assign receiver_io_rxMbIo_clkp = sender_io_txMbIo_clkp; // @[src/main/scala/mbafe/MbAfe.scala 366:23]
  assign receiver_io_rxMbIo_clkn = sender_io_txMbIo_clkn; // @[src/main/scala/mbafe/MbAfe.scala 366:23]
  assign receiver_io_txMbAfe_ready = io_rx_user_txData_ready; // @[src/main/scala/mbafe/MbAfe.scala 373:25]
endmodule
