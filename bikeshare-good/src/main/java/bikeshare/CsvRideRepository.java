package bikeshare;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CsvRideRepository implements RideRepository {
    private final Path path;
    public CsvRideRepository() { this(Path.of("rides.csv")); }
    public CsvRideRepository(Path path) { this.path = path; }
    @Override public void save(Ride ride) {
        String line = ride.memberId() + "," + ride.memberType() + "," + ride.bike().id() + "," + ride.minutes() + "," + ride.fee() + System.lineSeparator();
        try { Files.writeString(path, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND); }
        catch (IOException e) { throw new IllegalStateException("Could not save ride", e); }
    }
}
