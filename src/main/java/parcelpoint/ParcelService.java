package parcelpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParcelService {
    private final List<Locker> lockers = new ArrayList<>();
    private final List<Parcel> parcels = new ArrayList<>();
    private final ParcelRepository repository;

    public ParcelService() {
        this(new CsvParcelRepository());
    }

    public ParcelService(ParcelRepository repository) {
        this.repository = repository;
        lockers.add(new Locker(1, 1));
        lockers.add(new Locker(2, 2));
        lockers.add(new Locker(3, 3));
    }

    public List<Locker> getLockers() {
        return Collections.unmodifiableList(lockers);
    }

    public List<Parcel> getParcels() {
        return Collections.unmodifiableList(parcels);
    }

    public Locker storeParcel(Parcel parcel) {
        for (Locker locker : lockers) {
            if (locker.isFree() && locker.getSize() >= parcel.getSize()) {
                locker.setParcel(parcel);
                parcel.markStored();
                parcels.add(parcel);
                repository.save(parcels);
                return locker;
            }
        }
        return null;
    }

    public double pickUp(String code, int days) {
        for (Locker locker : lockers) {
            Parcel parcel = locker.getParcel();
            if (parcel != null && parcel.getCode().equals(code)) {
                locker.setParcel(null);
                parcel.markPickedUp();
                repository.save(parcels);
                return fee(parcel, days);
            }
        }
        return 0;
    }

    public double fee(Parcel parcel, int days) {
        if (days <= 3) return 0;
        if (parcel.getSize() == 1) return (days - 3) * 10.0;
        if (parcel.getSize() == 2) return (days - 3) * 15.0;
        return (days - 3) * 20.0;
    }
}
