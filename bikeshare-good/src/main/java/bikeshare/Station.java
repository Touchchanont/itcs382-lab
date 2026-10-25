package bikeshare;

import java.util.ArrayDeque;
import java.util.Queue;

public class Station {
    private final String name;
    private final Queue<Bike> available = new ArrayDeque<>();
    public Station(String name) {
        this.name = name;
        available.add(new Bike(name + "-1"));
        available.add(new Bike(name + "-2"));
    }
    public String name() { return name; }
    public Bike rentBike() {
        Bike bike = available.poll();
        if (bike == null) throw new IllegalStateException("No bikes available at " + name);
        return bike;
    }
    public void returnBike(Bike bike) { available.add(bike); }
}
