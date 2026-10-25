package parcelpoint;

import java.util.List;

public interface ParcelRepository {
    void save(List<Parcel> parcels);
}
