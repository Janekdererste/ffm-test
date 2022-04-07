import com.beust.jcommander.JCommander;

public class MPIRing {

    public static void main(String[] args) {

        var libMpiArg = new LibMpiArgs();
        JCommander.newBuilder().addObject(libMpiArg).build().parse(args);

        var mpi = new NativeMpi(libMpiArg.getPath());
        mpi.mpiInit(new String[0]);



        mpi.mpiFinalize();
    }
}
