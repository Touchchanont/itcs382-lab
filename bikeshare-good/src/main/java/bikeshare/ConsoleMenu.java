package bikeshare;

import java.util.Scanner;

public class ConsoleMenu {
    private final BikeShareService service;
    public ConsoleMenu(BikeShareService service) { this.service = service; }
    public void run() {
        System.out.println("Commands: rent <memberId> <student|staff> <station>, return <memberId> <station> <minutes>, quit");
        try (Scanner input = new Scanner(System.in)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.equals("quit")) return;
                if (!line.isEmpty()) {
                    try { execute(line.split("\\s+")); }
                    catch (IllegalArgumentException | IllegalStateException e) { System.out.println(e.getMessage()); }
                }
            }
        }
    }
    private void execute(String[] parts) {
        if (parts[0].equals("rent") && parts.length == 4) {
            Bike bike = service.rent(parts[1], MemberType.from(parts[2]), parts[3]);
            System.out.println("Rented " + bike.id() + " to " + parts[1]);
        } else if (parts[0].equals("return") && parts.length == 4) {
            Ride ride = service.returnBike(parts[1], parts[2], Integer.parseInt(parts[3]));
            System.out.println("Receipt: bike=" + ride.bike().id() + ", minutes=" + ride.minutes() + ", fee=" + ride.fee() + " baht");
        } else {
            System.out.println("Invalid command");
        }
    }
}
