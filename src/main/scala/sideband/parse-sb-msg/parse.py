# This file is adapted from the original parse.py file in 
# https://github.com/riscv/riscv-opcodes/blob/master/parse.py

from constants import *
import re
import glob
import os
import pprint
import logging
import collections
import yaml
import sys

def process_enc_line(line):

    # fill all bits with don't care. we use '-' to represent don't care
    # TODO: hardcoded for 128-bits.
    encoding = ['-'] * 128

    # get the name of instruction by splitting based on the first space
    [name, remaining] = line.split(' ', 1)
    # replace dots with underscores as dot doesn't work with C/Sverilog, etc
    name = name.replace('.', '_')

    # remove leading whitespaces
    remaining = remaining.lstrip()

    # check each field for it's length and overlapping bits
    # ex: 1..0=5 will result in an error --> x<y
    # ex: 5..0=0 2..1=2 --> overlapping bits
    for (s2, s1, entry) in fixed_ranges.findall(remaining):
        msb = int(s2)
        lsb = int(s1)

        # check msb < lsb
        if msb < lsb:
            logging.error(
                f'{line.split(" ")[0]:<10} has position {msb} less than position {lsb} in it\'s encoding'
            )
            raise SystemExit(1)

        # illegal value assigned as per bit width
        entry_value = int(entry, 0)
        if entry_value >= (1 << (msb - lsb + 1)):
            logging.error(
                f'{line.split(" ")[0]:<10} has an illegal value {entry_value} assigned as per the bit width {msb - lsb}'
            )
            raise SystemExit(1)

        for ind in range(lsb, msb + 1):
            # overlapping bits
            if encoding[127 - ind] != '-':
                logging.error(
                    f'{line.split(" ")[0]:<10} has {ind} bit overlapping in it\'s opcodes'
                )
                raise SystemExit(1)
            bit = str((entry_value >> (ind - lsb)) & 1)
            encoding[127 - ind] = bit

    # extract bit pattern assignments of the form hi..lo=val
    remaining = fixed_ranges.sub(' ', remaining)

    # do the same as above but for <lsb>=<val> pattern. single_fixed is a regex
    # expression present in constants.py
    for (lsb, value, drop) in single_fixed.findall(remaining):
        lsb = int(lsb, 0)
        value = int(value, 0)
        if encoding[127 - lsb] != '-':
            logging.error(
                f'{line.split(" ")[0]:<10} has {lsb} bit overlapping in it\'s opcodes'
            )
            raise SystemExit(1)
        encoding[127 - lsb] = str(value)
        
    # convert the list of encodings into a single string for match and mask
    match = "".join(encoding).replace('-','0')
    mask = "".join(encoding).replace('0','1').replace('-','0')

    # check if all args of the instruction are present in arg_lut present in
    # constants.py
    args = single_fixed.sub(' ', remaining).split()
    encoding_args = encoding.copy()
    for a in args:
        if a not in arg_lut:
            logging.error(f' Found variable {a} in instruction {name} whose mapping in arg_lut does not exist')
            raise SystemExit(1)
        else:
            (msb, lsb) = arg_lut[a]
            for ind in range(lsb, msb + 1):
                # overlapping bits
                if encoding_args[127 - ind] != '-':
                    logging.error(f' Found variable {a} in instruction {name} overlapping {encoding_args[127 - ind]} variable in bit {ind}')
                    raise SystemExit(1)
                encoding_args[127 - ind] = a
                
    single_dict = {}

    # update the fields of the instruction as a dict and return back along with
    # the name of the instruction
    single_dict['encoding'] = "".join(encoding)
    single_dict['variable_fields'] = args
    # single_dict['extension'] = [ext.split('/')[-1]]
    # single_dict['match']=hex(int(match,2))
    # single_dict['mask']=hex(int(mask,2))

    return (name, single_dict)

def create_inst_dict(f):
    instr_dict = {}
    
    logging.debug(f'Parsing File: {f} for sideband messages')
    with open(f) as fp:
        lines = (line.rstrip()
                    for line in fp)  # All lines including the blank ones
        lines = list(line for line in lines if line)  # Non-blank lines
        lines = list(
            line for line in lines
            if not line.startswith("#"))  # remove comment lines

        # go through each line of the file
        for line in lines:
            # if the an instruction needs to be imported then go to the
            # respective file and pick the line that has the instruction.
            # The variable 'line' will now point to the new line from the
            # imported file

            logging.debug(f'     Processing line: {line}')

            # call process_enc_line to get the data about the current
            # instruction
            (name, single_dict) = process_enc_line(line)

            if name not in instr_dict:
                # update the final dict with the instruction
                instr_dict[name] = single_dict
    return instr_dict

def make_chisel(instr_dict):
    factory = '''
// A factory function to create a message from a bitpat, source, destination, and data
object SBMessage_factory {
  def apply (base: BitPat, src: String, remote: Boolean = false, dst: String, data: UInt=0.U(64.W)) = {
    // take the bottom 64 bits of the base by modulo
    var msg: BigInt = base.value % (BigInt(1) << 64)
    val src_num: BigInt = src match {
      case "Protocol_0" => 0
      case "Protocol_1" => 4
      case "D2D" => 1
      case "PHY" => 2
      case _ => 0
    }
    var dst_num: BigInt = dst match {
      case "Protocol_0" => 0
      case "Protocol_1" => 4
      case "D2D" => 1
      case "PHY" => 2
      case _ => 0
    }
    dst_num += (if (remote) 4 else 0)
    msg += src_num << 29
    dst_num = dst_num << 30
    dst_num = dst_num << 26
    msg += dst_num
    println("SBMessage_factory: " + msg)
    val new_msg = Cat(data, msg.U(64.W))
    println("SBMessage_factory: " + new_msg) 
    new_msg
  }
}
'''
    chisel_names=''
    for i in instr_dict:
        chisel_names += f'  def {i.upper().replace(".","_"):<30s} = BitPat("b{instr_dict[i]["encoding"].replace("-","?")}")\n'
    else:
        chisel_file = open('../sb-msg-encoding.scala','w')
    chisel_file.write(f'''package ucie.sideband
import chisel3._
import chisel3.util._
import chisel3.experimental._
import freechips.rocketchip._
/* Automatically generated by parse_opcodes */
object SBM {{
{chisel_names}
  def isComplete (x: UInt) = x === COMP_0 | x === COMP_32 | x === COMP_64
  def isMessage (x: UInt) = x === MSG_0 | x === MSG_64
  def isRequest (x: UInt) = ~x(4)
}}

{factory}
''')
    chisel_file.close()

if __name__ == '__main__':
    # get all the encoding files in the current directory
    file_name = "sb-msg-encodings.txt"
    instr_dict = create_inst_dict(file_name)
    make_chisel(instr_dict)
