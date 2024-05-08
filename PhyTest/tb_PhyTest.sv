`timescale 1ns/100ps
// Testbench for PhyTest
module tb_PhyTest;
// Default clock

reg  clock;
reg  reset;
reg  io_tx_user_txData_ready;
reg  io_tx_user_rxData_valid;
reg [7:0] io_tx_user_rxData_bits_0;
reg [7:0] io_tx_user_rxData_bits_1;
reg [7:0] io_tx_user_rxData_bits_2;
reg [7:0] io_tx_user_rxData_bits_3;
reg [7:0] io_tx_user_rxData_bits_4;
reg [7:0] io_tx_user_rxData_bits_5;
reg [7:0] io_tx_user_rxData_bits_6;
reg [7:0] io_tx_user_rxData_bits_7;
reg [7:0] io_tx_user_rxData_bits_8;
reg [7:0] io_tx_user_rxData_bits_9;
reg [7:0] io_tx_user_rxData_bits_10;
reg [7:0] io_tx_user_rxData_bits_11;
reg [7:0] io_tx_user_rxData_bits_12;
reg [7:0] io_tx_user_rxData_bits_13;
reg [7:0] io_tx_user_rxData_bits_14;
reg [7:0] io_tx_user_rxData_bits_15;
reg  io_rx_user_txData_ready;
reg  io_rx_user_rxData_valid;
reg [7:0] io_rx_user_rxData_bits_0;
reg [7:0] io_rx_user_rxData_bits_1;
reg [7:0] io_rx_user_rxData_bits_2;
reg [7:0] io_rx_user_rxData_bits_3;
reg [7:0] io_rx_user_rxData_bits_4;
reg [7:0] io_rx_user_rxData_bits_5;
reg [7:0] io_rx_user_rxData_bits_6;
reg [7:0] io_rx_user_rxData_bits_7;
reg [7:0] io_rx_user_rxData_bits_8;
reg [7:0] io_rx_user_rxData_bits_9;
reg [7:0] io_rx_user_rxData_bits_10;
reg [7:0] io_rx_user_rxData_bits_11;
reg [7:0] io_rx_user_rxData_bits_12;
reg [7:0] io_rx_user_rxData_bits_13;
reg [7:0] io_rx_user_rxData_bits_14;
reg [7:0] io_rx_user_rxData_bits_15;
reg  io_clkp;
reg  io_clkn;

wire  io_tx_user_txData_valid;
wire [7:0] io_tx_user_txData_bits_0;
wire [7:0] io_tx_user_txData_bits_1;
wire [7:0] io_tx_user_txData_bits_2;
wire [7:0] io_tx_user_txData_bits_3;
wire [7:0] io_tx_user_txData_bits_4;
wire [7:0] io_tx_user_txData_bits_5;
wire [7:0] io_tx_user_txData_bits_6;
wire [7:0] io_tx_user_txData_bits_7;
wire [7:0] io_tx_user_txData_bits_8;
wire [7:0] io_tx_user_txData_bits_9;
wire [7:0] io_tx_user_txData_bits_10;
wire [7:0] io_tx_user_txData_bits_11;
wire [7:0] io_tx_user_txData_bits_12;
wire [7:0] io_tx_user_txData_bits_13;
wire [7:0] io_tx_user_txData_bits_14;
wire [7:0] io_tx_user_txData_bits_15;
wire  io_tx_user_rxData_ready;
wire  io_rx_user_txData_valid;
wire [7:0] io_rx_user_txData_bits_0;
wire [7:0] io_rx_user_txData_bits_1;
wire [7:0] io_rx_user_txData_bits_2;
wire [7:0] io_rx_user_txData_bits_3;
wire [7:0] io_rx_user_txData_bits_4;
wire [7:0] io_rx_user_txData_bits_5;
wire [7:0] io_rx_user_txData_bits_6;
wire [7:0] io_rx_user_txData_bits_7;
wire [7:0] io_rx_user_txData_bits_8;
wire [7:0] io_rx_user_txData_bits_9;
wire [7:0] io_rx_user_txData_bits_10;
wire [7:0] io_rx_user_txData_bits_11;
wire [7:0] io_rx_user_txData_bits_12;
wire [7:0] io_rx_user_txData_bits_13;
wire [7:0] io_rx_user_txData_bits_14;
wire [7:0] io_rx_user_txData_bits_15;
wire  io_rx_user_rxData_ready;
wire  io_clkn_out;
always #2.5 io_clkp = ~io_clkp;
always #2.5 io_clkn = ~io_clkn;
always #5 clock = ~clock;

// Signal initializations
initial begin
    clock = 0;
    reset = 0;
    io_tx_user_txData_ready = 0;
    io_tx_user_rxData_valid = 0;
    io_tx_user_rxData_bits_0 = 0;
    io_tx_user_rxData_bits_1 = 0;
    io_tx_user_rxData_bits_2 = 0;
    io_tx_user_rxData_bits_3 = 0;
    io_tx_user_rxData_bits_4 = 0;
    io_tx_user_rxData_bits_5 = 0;
    io_tx_user_rxData_bits_6 = 0;
    io_tx_user_rxData_bits_7 = 0;
    io_tx_user_rxData_bits_8 = 0;
    io_tx_user_rxData_bits_9 = 0;
    io_tx_user_rxData_bits_10 = 0;
    io_tx_user_rxData_bits_11 = 0;
    io_tx_user_rxData_bits_12 = 0;
    io_tx_user_rxData_bits_13 = 0;
    io_tx_user_rxData_bits_14 = 0;
    io_tx_user_rxData_bits_15 = 0;
    io_rx_user_txData_ready = 0;
    io_rx_user_rxData_valid = 0;
    io_rx_user_rxData_bits_0 = 0;
    io_rx_user_rxData_bits_1 = 0;
    io_rx_user_rxData_bits_2 = 0;
    io_rx_user_rxData_bits_3 = 0;
    io_rx_user_rxData_bits_4 = 0;
    io_rx_user_rxData_bits_5 = 0;
    io_rx_user_rxData_bits_6 = 0;
    io_rx_user_rxData_bits_7 = 0;
    io_rx_user_rxData_bits_8 = 0;
    io_rx_user_rxData_bits_9 = 0;
    io_rx_user_rxData_bits_10 = 0;
    io_rx_user_rxData_bits_11 = 0;
    io_rx_user_rxData_bits_12 = 0;
    io_rx_user_rxData_bits_13 = 0;
    io_rx_user_rxData_bits_14 = 0;
    io_rx_user_rxData_bits_15 = 0;
    io_clkp = 0;
    io_clkn = 1;
end

PhyTest uut (
    .clock(clock),
    .reset(reset),
    .io_tx_user_txData_ready(io_tx_user_txData_ready),
    .io_tx_user_rxData_valid(io_tx_user_rxData_valid),
    .io_tx_user_rxData_bits_0(io_tx_user_rxData_bits_0),
    .io_tx_user_rxData_bits_1(io_tx_user_rxData_bits_1),
    .io_tx_user_rxData_bits_2(io_tx_user_rxData_bits_2),
    .io_tx_user_rxData_bits_3(io_tx_user_rxData_bits_3),
    .io_tx_user_rxData_bits_4(io_tx_user_rxData_bits_4),
    .io_tx_user_rxData_bits_5(io_tx_user_rxData_bits_5),
    .io_tx_user_rxData_bits_6(io_tx_user_rxData_bits_6),
    .io_tx_user_rxData_bits_7(io_tx_user_rxData_bits_7),
    .io_tx_user_rxData_bits_8(io_tx_user_rxData_bits_8),
    .io_tx_user_rxData_bits_9(io_tx_user_rxData_bits_9),
    .io_tx_user_rxData_bits_10(io_tx_user_rxData_bits_10),
    .io_tx_user_rxData_bits_11(io_tx_user_rxData_bits_11),
    .io_tx_user_rxData_bits_12(io_tx_user_rxData_bits_12),
    .io_tx_user_rxData_bits_13(io_tx_user_rxData_bits_13),
    .io_tx_user_rxData_bits_14(io_tx_user_rxData_bits_14),
    .io_tx_user_rxData_bits_15(io_tx_user_rxData_bits_15),
    .io_rx_user_txData_ready(io_rx_user_txData_ready),
    .io_rx_user_rxData_valid(io_rx_user_rxData_valid),
    .io_rx_user_rxData_bits_0(io_rx_user_rxData_bits_0),
    .io_rx_user_rxData_bits_1(io_rx_user_rxData_bits_1),
    .io_rx_user_rxData_bits_2(io_rx_user_rxData_bits_2),
    .io_rx_user_rxData_bits_3(io_rx_user_rxData_bits_3),
    .io_rx_user_rxData_bits_4(io_rx_user_rxData_bits_4),
    .io_rx_user_rxData_bits_5(io_rx_user_rxData_bits_5),
    .io_rx_user_rxData_bits_6(io_rx_user_rxData_bits_6),
    .io_rx_user_rxData_bits_7(io_rx_user_rxData_bits_7),
    .io_rx_user_rxData_bits_8(io_rx_user_rxData_bits_8),
    .io_rx_user_rxData_bits_9(io_rx_user_rxData_bits_9),
    .io_rx_user_rxData_bits_10(io_rx_user_rxData_bits_10),
    .io_rx_user_rxData_bits_11(io_rx_user_rxData_bits_11),
    .io_rx_user_rxData_bits_12(io_rx_user_rxData_bits_12),
    .io_rx_user_rxData_bits_13(io_rx_user_rxData_bits_13),
    .io_rx_user_rxData_bits_14(io_rx_user_rxData_bits_14),
    .io_rx_user_rxData_bits_15(io_rx_user_rxData_bits_15),
    .io_clkp(io_clkp),
    .io_clkn(io_clkn),
    .io_tx_user_txData_valid(io_tx_user_txData_valid),
    .io_tx_user_txData_bits_0(io_tx_user_txData_bits_0),
    .io_tx_user_txData_bits_1(io_tx_user_txData_bits_1),
    .io_tx_user_txData_bits_2(io_tx_user_txData_bits_2),
    .io_tx_user_txData_bits_3(io_tx_user_txData_bits_3),
    .io_tx_user_txData_bits_4(io_tx_user_txData_bits_4),
    .io_tx_user_txData_bits_5(io_tx_user_txData_bits_5),
    .io_tx_user_txData_bits_6(io_tx_user_txData_bits_6),
    .io_tx_user_txData_bits_7(io_tx_user_txData_bits_7),
    .io_tx_user_txData_bits_8(io_tx_user_txData_bits_8),
    .io_tx_user_txData_bits_9(io_tx_user_txData_bits_9),
    .io_tx_user_txData_bits_10(io_tx_user_txData_bits_10),
    .io_tx_user_txData_bits_11(io_tx_user_txData_bits_11),
    .io_tx_user_txData_bits_12(io_tx_user_txData_bits_12),
    .io_tx_user_txData_bits_13(io_tx_user_txData_bits_13),
    .io_tx_user_txData_bits_14(io_tx_user_txData_bits_14),
    .io_tx_user_txData_bits_15(io_tx_user_txData_bits_15),
    .io_tx_user_rxData_ready(io_tx_user_rxData_ready),
    .io_rx_user_txData_valid(io_rx_user_txData_valid),
    .io_rx_user_txData_bits_0(io_rx_user_txData_bits_0),
    .io_rx_user_txData_bits_1(io_rx_user_txData_bits_1),
    .io_rx_user_txData_bits_2(io_rx_user_txData_bits_2),
    .io_rx_user_txData_bits_3(io_rx_user_txData_bits_3),
    .io_rx_user_txData_bits_4(io_rx_user_txData_bits_4),
    .io_rx_user_txData_bits_5(io_rx_user_txData_bits_5),
    .io_rx_user_txData_bits_6(io_rx_user_txData_bits_6),
    .io_rx_user_txData_bits_7(io_rx_user_txData_bits_7),
    .io_rx_user_txData_bits_8(io_rx_user_txData_bits_8),
    .io_rx_user_txData_bits_9(io_rx_user_txData_bits_9),
    .io_rx_user_txData_bits_10(io_rx_user_txData_bits_10),
    .io_rx_user_txData_bits_11(io_rx_user_txData_bits_11),
    .io_rx_user_txData_bits_12(io_rx_user_txData_bits_12),
    .io_rx_user_txData_bits_13(io_rx_user_txData_bits_13),
    .io_rx_user_txData_bits_14(io_rx_user_txData_bits_14),
    .io_rx_user_txData_bits_15(io_rx_user_txData_bits_15),
    .io_rx_user_rxData_ready(io_rx_user_rxData_ready),
    .io_clkn_out(io_clkn_out)
);

// Dump waveforms
initial begin
    $dumpfile("PhyTest.vcd");
    $dumpvars(0, tb_PhyTest);
end

// Reset handling
initial begin
    reset = 1'b1;
    @(negedge clock);
    repeat(9) @(negedge clock);
    reset = 1'b0;
 
    for(int i = 0; i < 4; i++) begin
        @(negedge clock);
        io_tx_user_rxData_valid = 'b1;
        io_rx_user_txData_ready = 'b1;
        io_tx_user_rxData_bits_0 = i+1;
        io_tx_user_rxData_bits_1 = i+1;
        io_tx_user_rxData_bits_2 = i+1;
        io_tx_user_rxData_bits_3 = i+1;
        io_tx_user_rxData_bits_4 = i+1;
        io_tx_user_rxData_bits_5 = i+1;
        io_tx_user_rxData_bits_6 = i+1;
        io_tx_user_rxData_bits_7 = i+1;
        io_tx_user_rxData_bits_8 = i+1;
        io_tx_user_rxData_bits_9 = i+1;
        io_tx_user_rxData_bits_10 = i+1;
        io_tx_user_rxData_bits_11 = i+1;
        io_tx_user_rxData_bits_12 = i+1;
        io_tx_user_rxData_bits_13 = i+1;
        io_tx_user_rxData_bits_14 = i+1;
        io_tx_user_rxData_bits_15 = i+1;
    end
    @(negedge clock)
    io_tx_user_rxData_valid = 'b0;

    repeat(20) @(negedge clock);
    repeat(20) @(negedge clock);
    $display("Simulation completed");
    $finish;
end
// Monitor $time
initial begin
    $monitor("Time: %0t", $time);
end
endmodule