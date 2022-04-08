# Test Repository to fiddle with the FFM-Framework
This repository is for playing with the foreign function and memory framework currently in [second incubator](https://openjdk.java.net/jeps/419)

## Set up
This project requires Java-18!!!

### Open-Mpi
Download a recent Open-Mpi [tar-all](https://www.open-mpi.org/software/ompi/v4.1/).

Follow the [instrcutions](https://github.com/open-mpi/ompi/blob/master/docs/installing-open-mpi/quickstart.rst) from the open-mpi repo.
For me the following commands worked:
```
$ tar xf openmpi-<version>.tar.bz2
$ cd openmpi-<version>
$ ./configure --prefix=<path/to/where/the/installation/should/go> --without-verbs 
...lots of output...
$ make -j 8 
...lots of output...
$ make install
...lots of output...
```

The ```--without-verbs``` parameter is necessary to disable Verbs according to [this FAQ](https://www-lb.open-mpi.org/faq/?category=openfabrics#ofa-device-error)


## Run the LinkedHelloWorld example
The ```LinkedHelloWorld``` example loads a custom C-library which exposes a ```hello``` function. The code for the program
can be found in ```src/main/c/HelloWorld.c```. 

Before the native code can be called, it needs to be compiled. On a linux system with ```gcc``` installed one can execute
the ```build.sh``` file. This will put ```HelloWorld.so``` binaries into the ```c-target``` folder. From there it can be
called by the ```LinkedHelloWorld``` Java code. 

To Run the ```LinkedHelloWorld``` class, one needs to set ```--add-modules jdk.incubator.foreign``` as a compiler option.
In IntelliJ this is under:

Preferences -> Build, Execution, Development -> Compiler -> Java Compiler -> Additional command line parameters

Also, when running the program the following additional JVM-Parameters are necessary 
`````--add-modules jdk.incubator.foreign --enable-native-access ALL-UNNAMED`````

The output of the Program should be 10 ```Hello World <i>``` messages.

## Run Hello_MPI.c and Ring.c

To get familiar with doing stuff in c, this repository contains ```Hello_MPI.c```
and ```Ring.c```. Both have a dependency on the MPI-library. Both programs can be
compiled with ```cmake```. Cmake needs to be configured by running 

```aidl
$ cmake CMakeLists.txt
```
This will produce some files. Afterwards, both programs can be build with
```aidl
$ cmake --build .
```

## Run MPIHelloWorld
This is the Hello World example ported to Java. The following command starts the 
program:
```aidl
$ mpirun -np 2 $HOME/jdk-18/bin/java \
 --add-modules jdk.incubator.foreign --enable-native-access ALL-UNNAMED \
 -cp ffm-test-1.0-SNAPSHOT.jar MPIHelloWorld \
 -libmpi /usr/lib/x86_64-linux-gnu/openmpi/lib/libmpi.so
```
The executable passed to ```mpirun``` is the java executable. The ```--add-modules jdk.incubator.foreign --enable-native-access ALL-UNNAMED```
parameters are necesarry to execute the foreign function api. ```-cp ffm-test-1.0-SNAPSHOT.jar MPIHelloWorld```
calls the executable jar and runs the ```MPIHelloWorld``` program.
```-libmpi /usr/lib/x86_64-linux-gnu/openmpi/lib/libmpi.so``` is the path of the shared mpi library. This
varies depending on how open-mpi was installed. It must be passed to the program since the Java progam expects
an absolute path to loaded libraries. 

## Run MPIRing
This works the same as the ```MPIHelloWorld``` example.