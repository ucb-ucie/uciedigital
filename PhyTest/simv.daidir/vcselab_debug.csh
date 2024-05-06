#!/bin/csh -f

cd /bwrcq/C/stefan_shi/uciedigital/PhyTest

#This ENV is used to avoid overriding current script in next vcselab run 
setenv SNPS_VCSELAB_SCRIPT_NO_OVERRIDE  1

/tools/synopsys/vcs/S-2021.09-SP1-1/linux64/bin/vcselab $* \
    -o \
    simv \
    -nobanner \

cd -

