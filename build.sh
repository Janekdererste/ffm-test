mkdir -p c-target
rm c-target/HelloWorld.o
rm c-target/HelloWorld.so

gcc -c -Wall -Werror -fpic src/main/c/HelloWorld.c -o c-target/HelloWorld.o
gcc -shared -o c-target/HelloWorld.so c-target/HelloWorld.o