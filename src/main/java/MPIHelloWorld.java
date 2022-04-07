import com.beust.jcommander.JCommander;

public class MPIHelloWorld {

    public static void main(String[] args) {

        var libMpiArg = new LibMpiArgs();
        JCommander.newBuilder().addObject(libMpiArg).build().parse(args);

        var mpi = new NativeMpi(libMpiArg.getPath());
        mpi.mpiInit(new String[0]);
        System.out.println("Hello Word from inbetween MPI_Init and MPI_Finalize!");

        //mpi.getRank();
        mpi.mpiFinalize();
    }
}
