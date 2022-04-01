import jdk.incubator.foreign.*;

import java.lang.invoke.MethodHandle;

public class LinkedHelloWorld {

    public static void main(String[] args) throws Throwable {

        var filename = "/mnt/c/Users/Janekdererste/Projects/ffm-test/c-target/HelloWorld.so";
        System.load(filename);

        SymbolLookup loaderLookup = SymbolLookup.loaderLookup();
        NativeSymbol nativeHello = loaderLookup.lookup("hello").orElseThrow();

        CLinker linker = CLinker.systemCLinker();
        MethodHandle handle = linker.downcallHandle(
                nativeHello,
                FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT)
        );

        for (int i = 0; i < 10; i++)
            handle.invoke(i);
    }
}
