cmake_minimum_required(VERSION 3.16)
project(ffm_test C)

set(CMAKE_C_STANDARD 11)

include_directories(src/main/c
        /etc/alternatives/mpi-x86_64-linux-gnu/mpi
        /usr/lib/x86_64-linux-gnu/openmpi/include/openmpi
        /usr/lib/x86_64-linux-gnu/openmpi/include
        /usr/lib/x86_64-linux-gnu/openmpi/lib)

add_executable(hellompi
        src/main/c/Hello_MPI.c)

add_executable(ringmpi
        src/main/c/Ring.c)
target_link_libraries(ringmpi mpi)
target_link_libraries(hellompi mpi)