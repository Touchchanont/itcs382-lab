package bikeshare;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new ConsoleMenu(new BikeShareService(new CsvRideRepository())).run();
    }
}
