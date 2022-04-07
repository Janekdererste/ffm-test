import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.NativeSymbol;
import jdk.incubator.foreign.SymbolLookup;

class Datatype {

    private static final SymbolLookup loaderLookup = SymbolLookup.loaderLookup();

    private final MemoryAddress pointer;

    Datatype(String symbolName) {
        NativeSymbol nativeMpiCommWorld = loaderLookup.lookup(symbolName).orElseThrow();
        pointer = nativeMpiCommWorld.address();
    }

    public MemoryAddress getAddress() {
        return pointer;
    }

    static MemoryAddress getIntDatatype() {
        // this could be cached I guess.
       return new Datatype.Int().getAddress();
    }

    public static class Int extends Datatype{

        public Int() {
            super("ompi_mpi_int");
        }
    }
}