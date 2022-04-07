import jdk.incubator.foreign.*;

import java.lang.invoke.MethodHandle;

public class NativeMpi {

    private final SymbolLookup loaderLookup = SymbolLookup.loaderLookup();

    public NativeMpi(String libraryPath) {
        // this links the mpi library
        System.out.println("Loading library path: " + libraryPath);
        System.load(libraryPath);
    }

    public void mpiInit(String[] args) {
        System.out.println("Call to MPI_Init");
        NativeSymbol nativeMpiInit = loaderLookup.lookup("MPI_Init").orElseThrow();

        MethodHandle mpiInit = CLinker.systemCLinker().downcallHandle(
                nativeMpiInit, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
        );

        try (ResourceScope scope = ResourceScope.newConfinedScope()) {

            var allocator = SegmentAllocator.nativeAllocator(scope);

            // mpi_init expects a *char[], I don't know whether this is the way to go
            // make room for an array of pointers to the strings of the String[] args
            var argv = args.length == 0 ? allocator.allocate(ValueLayout.ADDRESS):
                    allocator.allocate(
                            MemoryLayout.sequenceLayout(args.length, ValueLayout.ADDRESS)
                    );

            // allocate space for each string in args and store the pointer in the
            // previously allocated pointer array.
            for (int i = 0; i < args.length; i++) {
                var arg = args[i];
                var cString = allocator.allocateUtf8String(arg);
                argv.setAtIndex(ValueLayout.ADDRESS, i, cString);
            }

            // mpi_init expects a *int, so copy an int to native memory and store a pointer to it.
            var cLength = allocator.allocate(ValueLayout.JAVA_INT, args.length);
            var argc = allocator.allocate(ValueLayout.ADDRESS, cLength);

            var result = mpiInit.invoke(argc, argv);

            System.out.println("Called MPI_Init. Result was: " + result);

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void mpiFinalize() {

        NativeSymbol nativeMpiFinalize = loaderLookup.lookup("MPI_Finalize").orElseThrow();
        MethodHandle mpiFinalize = CLinker.systemCLinker().downcallHandle(
                nativeMpiFinalize, FunctionDescriptor.of(ValueLayout.JAVA_INT)
        );

        try {
            var result = mpiFinalize.invoke();
            System.out.println("Called MPI_Finalize. Result was: " + result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void getRank() {

        var nativeMpiCommWorld = loaderLookup.lookup("MPI_COMM_WORLD").orElseThrow();
        MethodHandle handle = CLinker.systemCLinker().downcallHandle(
                nativeMpiCommWorld, FunctionDescriptor.of(ValueLayout.ADDRESS)
        );

        try {

            var result = handle.invoke();
            System.out.println(result);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public void send() {

    }

    public void receive() {

     /*   var nativeSend = loaderLookup.lookup("MPI_send").orElseThrow();
        var nativeSendHandle = CLinker.systemCLinker().downcallHandle(
                nativeSend, FunctionDescriptor.of(
                        ValueLayout.JAVA_INT,
                        ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                )
        )

      */
    }
}
