import com.beust.jcommander.JCommander;

public class MPIRing {

    public static void main(String[] args) {

        var libMpiArg = new LibMpiArgs();
        JCommander.newBuilder().addObject(libMpiArg).build().parse(args);

        var mpi = new NativeMpi(libMpiArg.getPath());
        mpi.mpiInit(new String[0]);

        int rank = mpi.getRank();
        int size = mpi.getSize();
        int next = (rank + 1) % size;
        int prev = (rank + size - 1) % size;

        System.out.println("My rank is: " + rank + ", my size is: " + size + ", prev: " + prev + ", next: " + next);

        if (rank == 0) {
            var message = 10;
            mpi.send(message, next);
            System.out.println("Process: " + rank + " sent " + message + " to " + next);
        }

        while (true) {

            int received = mpi.receive(prev);
            System.out.println("Process: " + rank + " received " + received + " from " + prev);

            if (rank == 0) {
                received--;
                System.out.println("Process: " + rank + " decrements message to: " + received);
            }

            // pass message to next process
            mpi.send(received, next);

            if (received == 0) {
                System.out.println("Process: " + rank + " exits!");
                break;
            }
        }

        if (0 == rank) {
            var result = mpi.receive(prev);
            System.out.println("Process 0 received last message");
        }
        mpi.mpiFinalize();
    }
}
