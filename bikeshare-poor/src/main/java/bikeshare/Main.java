package bikeshare;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static final Map<String, List<String>> STATION_BIKES = new LinkedHashMap<>();
    public static final Map<String, String> RENTALS = new LinkedHashMap<>();
    public static final Map<String, Integer> MEMBER_TYPES = new LinkedHashMap<>();
    public static final List<String> FINISHED_RIDES = new ArrayList<>();

    static {
        for (String station : List.of("Library", "Canteen", "Dorm")) {
            STATION_BIKES.put(station, new ArrayList<>(List.of(station + "-1", station + "-2")));
        }
    }

    public static void main(String[] args) {
        System.out.println("Commands: rent <memberId> <student|staff> <station>, return <memberId> <station> <minutes>, quit");
        try (Scanner input = new Scanner(System.in)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\s+");
                    if (parts[0].equals("quit")) break;
                    try {
                        if (parts[0].equals("rent") && parts.length == 4) rent(parts[1], parts[2], parts[3]);
                        else if (parts[0].equals("return") && parts.length == 4) finishRide(parts[1], parts[2], Integer.parseInt(parts[3]));
                        else System.out.println("Invalid command");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    private static void rent(String memberId, String memberType, String station) {
        List<String> bikes = STATION_BIKES.get(station);
        if (bikes == null) throw new IllegalArgumentException("Unknown station: " + station);
        if (bikes.isEmpty()) throw new IllegalArgumentException("No bikes available at " + station);
        if (!memberType.equals("student") && !memberType.equals("staff")) throw new IllegalArgumentException("Unknown member type");
        String bike = bikes.remove(0);
        RENTALS.put(memberId, bike);
        MEMBER_TYPES.put(memberId, memberType.equals("student") ? 1 : 2);
        System.out.println("Rented " + bike + " to " + memberId);
    }

    private static void finishRide(String memberId, String station, int minutes) {
        List<String> bikes = STATION_BIKES.get(station);
        if (bikes == null) throw new IllegalArgumentException("Unknown station: " + station);
        if (minutes < 0) throw new IllegalArgumentException("Minutes must not be negative");
        String bike = RENTALS.remove(memberId);
        Integer memberType = MEMBER_TYPES.remove(memberId);
        if (bike == null || memberType == null) throw new IllegalArgumentException("No active rental for " + memberId);
        bikes.add(bike);
        int fee = minutes * (memberType == 1 ? 2 : 3);
        String ride = bike + "," + minutes + "," + fee;
        FINISHED_RIDES.add(ride);
        saveRides(FINISHED_RIDES.size() > 1, ride);
        System.out.println("Receipt: bike=" + bike + ", minutes=" + minutes + ", fee=" + fee + " baht");
    }

    private static void saveRides(boolean append, String ride) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("rides.csv"),
                java.nio.file.StandardOpenOption.CREATE,
                append ? java.nio.file.StandardOpenOption.APPEND : java.nio.file.StandardOpenOption.TRUNCATE_EXISTING)) {
            if (append) writer.newLine();
            writer.write(ride);
        } catch (IOException e) {
            System.out.println("Could not save ride: " + e.getMessage());
        }
    }
}
