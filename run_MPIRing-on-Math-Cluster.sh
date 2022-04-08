#!/bin/bash --login
#$ -cwd
#$ -N mpi-test
#$ -m be
#$ -M laudan@tu-berlin.de
#$ -j y
#$ -o ffm-ring.log
#$ -l h_rt=1000
#$ -l mem_free=4G
#$ -pe ompi* 2

OMPI="/homes2/ils/laudan/ompi-4.1.2"
JAVA="/homes2/ils/laudan/jdk-18/bin/java"
$OMPI/bin/mpirun --mca pml ucx -np 2\
 $JAVA --add-modules jdk.incubator.foreign --enable-native-access ALL-UNNAMED -cp \
 ffm-test-1.0-SNAPSHOT.jar MPIRing \
 -libmpi $OMPI/lib/libmpi.so