import jdk.incubator.foreign.*;

import java.lang.invoke.MethodHandle;

public class LinkedHelloWorld {

    public static void main(String[] args) throws Throwable {

        var filename = "C:/Users/janek/Downloads/ffm-test/c-target/HelloWorld";
        System.load(filename);

        SymbolLookup loaderLookup = SymbolLookup.loaderLookup();
        NativeSymbol nativeMain = loaderLookup.lookup("main").orElseThrow();

        CLinker linker = CLinker.systemCLinker();
        MethodHandle handle = linker.downcallHandle(
                linker.lookup("hello").orElseThrow(),
                FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT)
        );
        handle.invoke();
    }
}
