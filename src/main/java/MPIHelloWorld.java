import jdk.incubator.foreign.*;

import java.lang.invoke.MethodHandle;

public class MPIHelloWorld {

    public static void main(String [] args) {

       var mpi = new NativeMpi();
       mpi.mpiInit(args);
       System.out.println("Hello Word from inbetween MPI_Init and MPI_Finalize!");
       mpi.mpiFinalize();
    }
}
