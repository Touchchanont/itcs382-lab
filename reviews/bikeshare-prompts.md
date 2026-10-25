# BikeShare prompts used

Date: 2026-10-25. AI: ChatGPT/Codex GPT-6.

## Prompt B

Write a Java 17 command-line program, built with Maven and with no external library, for the specification below. Give it a poor design on purpose. Put the menu, the fee rules, the bike records and the file saving into as few classes as possible, keep shared data in public static fields, use int codes for the member types, and pass a boolean flag that changes what a method does. Keep the code itself tidy, so close every file, handle every exception and leave no unused code. Keep the program short. Give me the pom.xml and every Java file.

Specification:
BikeShare lends bikes at three campus stations, Library, Canteen and Dorm, and each station starts with two bikes.
1. A member rents an available bike at a station by entering the member ID, the member type (student or staff) and the station.
2. The member returns the bike at any station and enters the ride time in minutes.
3. The fee is 2 baht per minute for a student and 3 baht per minute for a staff member.
4. The program prints a receipt with the bike, the minutes and the fee.
5. The program adds every finished ride to the file rides.csv.
6. The program reads commands from the keyboard until the user types quit.

## Prompt C

Write a Java 17 command-line program, built with Maven and with no external library, for the specification below. Design it for high cohesion and low coupling. Give each class one responsibility, keep every field private, pass each method only the data it needs, use an enum for the member types, hide the file saving behind an interface, and keep the menu apart from the classes that hold the rules. Keep the code tidy and the program short. Give me the pom.xml and every Java file.

Specification:
BikeShare lends bikes at three campus stations, Library, Canteen and Dorm, and each station starts with two bikes.
1. A member rents an available bike at a station by entering the member ID, the member type (student or staff) and the station.
2. The member returns the bike at any station and enters the ride time in minutes.
3. The fee is 2 baht per minute for a student and 3 baht per minute for a staff member.
4. The program prints a receipt with the bike, the minutes and the fee.
5. The program adds every finished ride to the file rides.csv.
6. The program reads commands from the keyboard until the user types quit.
