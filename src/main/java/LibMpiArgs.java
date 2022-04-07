import com.beust.jcommander.Parameter;

public class LibMpiArgs {

    @Parameter(names = "-libmpi", required = true)
    private String path;

    public String getPath() {
        return path;
    }
}
