package parcelpoint;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvParcelRepository implements ParcelRepository {
    private final Path path;

    public CsvParcelRepository() {
        this(Path.of("parcels.csv"));
    }

    public CsvParcelRepository(Path path) {
        this.path = path;
    }

    @Override
    public void save(List<Parcel> parcels) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Parcel parcel : parcels) {
                writer.write(parcel.getCode() + "," + parcel.getStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not save parcels to " + path, e);
        }
    }
}
