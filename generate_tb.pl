#!/usr/bin/perl
use strict;
use warnings;

# Use the first command line argument as the design file name
my $design_filename = $ARGV[0] or die "Usage: $0 <design_file_name.sv>";
(my $module_name = $design_filename) =~ s/\.sv$//;

my @inputs = ();
my @outputs = ();
my @parameters = ();
my $processing_module = 0; # Flag to indicate when the correct module is being processed

# Open the design file
open(my $fh, '<', $design_filename) or die "Could not open file '$design_filename' $!";

while (my $row = <$fh>) {
    chomp $row;
    
    if ($row =~ /module\s+$module_name\s*\(/) {
        $processing_module = 1;
        next;
    }
    if ($processing_module && $row =~ /^\s*endmodule\b/) {
        $processing_module = 0;
        last;
    }
    if ($processing_module) {
        if ($row =~ /parameter\s+(\w+)\s*=\s*(.*?)[,;]/) {
            push @parameters, $1;
        }
        if ($row =~ /(input|output)\s+(reg|wire)?\s*(\[.*?\])?\s*(\w+)/) {
            my $direction = $1;
            my $type = ($direction eq "input") ? "reg" : "wire";
            my $width = $3 // "";
            my $name = $4;
            my $port = {direction => $direction, type => $type, name => $name, width => $width};
            if ($direction eq "input") {
                push @inputs, $port;
            } elsif ($direction eq "output") {
                push @outputs, $port;
            }
        }
    }
}

close($fh);

# Generate testbench file
my $tb_filename = "tb_${module_name}.sv";
open(my $tb_fh, '>', $tb_filename) or die "Could not open file '$tb_filename' for writing: $!";

print $tb_fh "`timescale 1ns/100ps\n";
print $tb_fh "// Testbench for $module_name\n";
print $tb_fh "module tb_$module_name;\n";

# Default clock
print $tb_fh "// Default clock\nreg clk = 0;\nalways #5 clk = ~clk;\n\n";

# Declare inputs as reg, including widths
foreach my $port (@inputs) {
    print $tb_fh "$port->{type} $port->{width} $port->{name};\n";
}
print $tb_fh "\n";

# Declare outputs as wire, including widths
foreach my $port (@outputs) {
    print $tb_fh "$port->{type} $port->{width} $port->{name};\n";
}
print $tb_fh "\n";

# Signal initializations
print $tb_fh "// Signal initializations\ninitial begin\n";
foreach my $port (@inputs) {
    print $tb_fh "    $port->{name} = 0;\n";
}
print $tb_fh "end\n\n";

# Instance of the module
print $tb_fh "$module_name uut (\n";

my $last_input = pop @inputs;
my $last_output = pop @outputs;

foreach my $port (@inputs, @outputs) {
    print $tb_fh "    .$port->{name}($port->{name}),\n";
}

if ($last_input && $last_output ) {
    print $tb_fh "    .$last_input->{name}($last_input->{name}),\n";
    print $tb_fh "    .$last_output->{name}($last_output->{name})\n";
} elsif ($last_input) {
    print $tb_fh "    .$last_input->{name}($last_input->{name})\n";
} elsif ($last_output) {
    print $tb_fh "    .$last_output->{name}($last_output->{name})\n";
} 

print $tb_fh ");\n\n";

# Dump waveforms
print $tb_fh "// Dump waveforms\ninitial begin\n    \$dumpfile(\"${module_name}.vcd\");\n    \$dumpvars(0, tb_$module_name);\nend\n\n";

# Reset handling
print $tb_fh "// Reset handling\n";
print $tb_fh "initial begin\n";
print $tb_fh "    reset = 1'b1;\n";
print $tb_fh "    @(negedge clk);\n"; # Wait for the first negedge of clk
print $tb_fh "    repeat(9) @(negedge clk);\n"; # Then wait for additional 9 negedges
print $tb_fh "    reset = 1'b0;\n";
print $tb_fh "    #100; // Wait some time before finishing the simulation\n";
print $tb_fh "    \$display(\"Simulation completed\");\n";
print $tb_fh "    \$finish;\n";
print $tb_fh "end\n";

# Monitor $time in a separate initial block
print $tb_fh "// Monitor \$time\n";
print $tb_fh "initial begin\n";
print $tb_fh "    \$monitor(\"Time: %0t\", \$time);\n";
print $tb_fh "end\n";

print $tb_fh "endmodule\n";

close($tb_fh);

print "Testbench $tb_filename generated.\n";
