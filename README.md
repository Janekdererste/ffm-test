# Test Repository to fiddle with the FFM-Framework
This repository is for playing with the foreign function and memory framework currently in [second incubator](https://openjdk.java.net/jeps/419)

## Set up
This project requires Java-18

## Run the HelloWorld example
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
