package parcelpoint;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ParcelService service = new ParcelService();
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit");
            while (in.hasNextLine()) {
                String line = in.nextLine().trim();
                if (line.equals("quit")) break;
                if (!line.isEmpty()) handleCommand(service, line.split("\\s+"));
            }
        }
    }

    private static void handleCommand(ParcelService service, String[] words) {
        if (words[0].equals("store") && words.length == 4) {
            Parcel parcel = new Parcel(words[1], words[2], Integer.parseInt(words[3]));
            Locker locker = service.storeParcel(parcel);
            System.out.println(locker == null ? "No free locker" : "Locker " + locker.getNumber());
        } else if (words[0].equals("pickup") && words.length == 3) {
            System.out.println("Fee: " + service.pickUp(words[1], Integer.parseInt(words[2])));
        } else if (words[0].equals("list")) {
            service.getParcels().stream().filter(p -> "STORED".equals(p.getStatus()))
                    .forEach(p -> System.out.println(p.getCode() + " is waiting"));
        } else if (words[0].equals("staff")) {
            service.getParcels().forEach(p -> System.out.println(
                    p.getCode() + " " + p.getStudentId() + " " + p.getStatus()));
            System.out.println("Free lockers: " + service.getLockers().stream().filter(Locker::isFree).count());
        }
    }
}
