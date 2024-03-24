# This file is adapted from the original parse.py file in 
# https://github.com/riscv/riscv-opcodes/blob/master/constants.py

import re


isa_regex = \
re.compile("^RV(32|64|128)[IE]+[ABCDEFGHJKLMNPQSTUVX]*(Zicsr|Zifencei|Zihintpause|Zam|Ztso|Zkne|Zknd|Zknh|Zkse|Zksh|Zkg|Zkb|Zkr|Zks|Zkn|Zba|Zbc|Zbb|Zbp|Zbr|Zbm|Zbs|Zbe|Zbf|Zbt|Zmmul|Zbpbo|Zca|Zcf|Zcd|Zcb|Zcmp|Zcmt){,1}(_Zicsr){,1}(_Zifencei){,1}(_Zihintpause){,1}(_Zmmul){,1}(_Zam){,1}(_Zba){,1}(_Zbb){,1}(_Zbc){,1}(_Zbe){,1}(_Zbf){,1}(_Zbm){,1}(_Zbp){,1}(_Zbpbo){,1}(_Zbr){,1}(_Zbs){,1}(_Zbt){,1}(_Zkb){,1}(_Zkg){,1}(_Zkr){,1}(_Zks){,1}(_Zkn){,1}(_Zknd){,1}(_Zkne){,1}(_Zknh){,1}(_Zkse){,1}(_Zksh){,1}(_Ztso){,1}(_Zca){,1}(_Zcf){,1}(_Zcd){,1}(_Zcb){,1}(_Zcmp){,1}(_Zcmt){,1}$")

# regex to find <msb>..<lsb>=<val> patterns in instruction
fixed_ranges = re.compile(
    '\s*(?P<msb>\d+.?)\.\.(?P<lsb>\d+.?)\s*=\s*(?P<val>\d[\w]*)[\s$]*', re.M)

# regex to find <lsb>=<val> patterns in instructions
#single_fixed = re.compile('\s+(?P<lsb>\d+)=(?P<value>[\w\d]*)[\s$]*', re.M)
single_fixed = re.compile('(?:^|[\s])(?P<lsb>\d+)=(?P<value>[\w]*)((?=\s|$))', re.M)

# regex to find the overloading condition variable
var_regex = re.compile('(?P<var>[a-zA-Z][\w\d]*)\s*=\s*.*?[\s$]*', re.M)

# regex for pseudo op instructions returns the dependent filename, dependent
# instruction, the pseudo op name and the encoding string
pseudo_regex = re.compile(
    '^\$pseudo_op\s+(?P<filename>rv[\d]*_[\w].*)::\s*(?P<orig_inst>.*?)\s+(?P<pseudo_inst>.*?)\s+(?P<overload>.*)$'
, re.M)

imported_regex = re.compile('^\s*\$import\s*(?P<extension>.*)\s*::\s*(?P<instruction>.*)', re.M)

# look up table of position of various arguments that are used by the
# instructions in the encoding files.
arg_lut = {}
arg_lut['rd'] = (11, 7)
arg_lut['rs1'] = (19, 15)
arg_lut['rs2'] = (24, 20)
