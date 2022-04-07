import com.beust.jcommander.JCommander;

public class MPIHelloWorld {

    public static void main(String[] args) {

        var libMpiArg = new LibMpiArgs();
        JCommander.newBuilder().addObject(libMpiArg).build().parse(args);

        var mpi = new NativeMpi(libMpiArg.getPath());
        mpi.mpiInit(new String[0]);

        var rank = mpi.getRank();
        var size = mpi.getSize();

        System.out.println("Rank: " + rank + ", Size: " + size);
        mpi.mpiFinalize();
    }
}
