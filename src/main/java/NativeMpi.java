import jdk.incubator.foreign.*;

import java.lang.invoke.MethodHandle;

public class NativeMpi {

    private final SymbolLookup loaderLookup = SymbolLookup.loaderLookup();

    private boolean isInitialized = false;
    MemoryAddress commPointer;

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
            var argv = args.length == 0 ? allocator.allocate(ValueLayout.ADDRESS) :
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

            // we constantly need the global communicator. Get it here once and store it.
            NativeSymbol nativeMpiCommWorld = loaderLookup.lookup("ompi_mpi_comm_world").orElseThrow();
            commPointer = nativeMpiCommWorld.address();

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
            isInitialized = false;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public int getRank() {

        try (ResourceScope scope = ResourceScope.newConfinedScope()) {

            // find the native symbol and get a java method handle int MPI_Comm_rank(*Comm communicator, *int rank)
            NativeSymbol nativeGetRank = loaderLookup.lookup("MPI_Comm_rank").orElseThrow();
            MethodHandle handle = CLinker.systemCLinker().downcallHandle(
                    nativeGetRank, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
            );
            // allocate space for the rank pointer
            MemorySegment rankPointer = MemorySegment.allocateNative(ValueLayout.JAVA_INT, scope);

            handle.invoke(this.commPointer, rankPointer);

            return rankPointer.get(ValueLayout.JAVA_INT, 0);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public int getSize() {

        try (var scope = ResourceScope.newConfinedScope()) {

            // find the native symbol and get a java method handle of "int MPI_Comm_size(*Comm communicator, *int size)
            var getSizeSymbol = loaderLookup.lookup("MPI_Comm_size").orElseThrow();
            var getSizeHandle = CLinker.systemCLinker().downcallHandle(
                    getSizeSymbol, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
            );
            var sizePointer = MemorySegment.allocateNative(ValueLayout.JAVA_INT, scope);

            getSizeHandle.invoke(this.commPointer, sizePointer);
            return sizePointer.get(ValueLayout.JAVA_INT, 0);
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
