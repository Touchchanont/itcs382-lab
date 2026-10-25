package bikeshare;

import java.util.LinkedHashMap;
import java.util.Map;

public class BikeShareService {
    private final Map<String, Station> stations = new LinkedHashMap<>();
    private final Map<String, Rental> rentals = new LinkedHashMap<>();
    private final RideRepository rides;
    public BikeShareService(RideRepository rides) {
        this.rides = rides;
        for (String name : new String[]{"Library", "Canteen", "Dorm"}) stations.put(name, new Station(name));
    }
    public Bike rent(String memberId, MemberType type, String stationName) {
        if (rentals.containsKey(memberId)) throw new IllegalStateException("Member already has a bike");
        Bike bike = station(stationName).rentBike();
        rentals.put(memberId, new Rental(type, bike));
        return bike;
    }
    public Ride returnBike(String memberId, String stationName, int minutes) {
        Station returnStation = station(stationName);
        if (minutes < 0) throw new IllegalArgumentException("Minutes must not be negative");
        Rental rental = rentals.remove(memberId);
        if (rental == null) throw new IllegalStateException("No active rental for " + memberId);
        Ride ride = Ride.complete(memberId, rental.type(), rental.bike(), minutes);
        returnStation.returnBike(rental.bike());
        rides.save(ride);
        return ride;
    }
    private Station station(String name) {
        Station station = stations.get(name);
        if (station == null) throw new IllegalArgumentException("Unknown station: " + name);
        return station;
    }
    private record Rental(MemberType type, Bike bike) {}
}
