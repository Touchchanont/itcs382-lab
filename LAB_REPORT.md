# Lab Report, Code Quality and Design Quality

Repository URL: https://github.com/Touchchanont/itcs382-lab

## Tools

| Item | Version or name |
| --- | --- |
| JDK | Eclipse Temurin OpenJDK 25.0.5 (used for Maven and Java runs; system default is Amazon Corretto 11.0.25) |
| Maven | Apache Maven 3.10.0 |
| Git | 2.56.0 |
| IDE | Visual Studio Code 1.141.2 |
| SonarQube for IDE | SonarQube for IDE 6.2.0 |
| AI tool and exact model | ChatGPT / Codex, GPT-6 |

## Task 1. SonarQube on the starter code

Commit of the starter, before any change: 3e356f4

Table 1. SonarQube issues in the starter code

| No. | Rule | File:line | Message |
| --- | --- | --- | --- |
| 1 | `java:S106` | `Main.java:9` | Replace this use of System.out by a logger. |
| 2 | `java:S106` | `Main.java:15` | Replace this use of System.out by a logger. |
| 3 | `java:S106` | `Main.java:17` | Replace this use of System.out by a logger. |
| 4 | `java:S106` | `Main.java:22` | Replace this use of System.out by a logger. |

The Problems panel also showed four Java-extension diagnostics (an unclosed `Scanner`, an unused `Date` import, an unused `maxParcels` field, and an unclosed `FileWriter`). Those were not counted as SonarQube findings.

Before any code fix, `mvn package` failed while Maven used the default Java 11 runtime (`release version 17 not supported`). Temurin 25.0.5 was already installed, and running Maven/Java with that JDK produced `BUILD SUCCESS`.

Starter program output for the required commands:

```text
Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit
SMS to 6501: parcel P1 is in locker 2
Locker 2
Fee: 0.0
P1 6501 STORED
Free lockers: 2
```

Starter `parcels.csv` was empty after the run. The program did not match the parcel code on pickup and the CSV writer was not closed.

## Task 2. AI review of the starter code

| Item | Value |
| --- | --- |
| Date | 2026-10-25 |
| AI tool and exact model | ChatGPT / Codex, GPT-6 |
| Commit reviewed | 3e356f4 |
| How the AI read the files (attached the files, opened the folder, or pasted the files) | Read all four original Java source files in the local workspace |
| Output file in the reviews folder | `reviews/parcelpoint-review.md` |

Table 2. All findings on the starter code

| No. | File:line | Finding | Found by (SonarQube, AI or both) | SonarQube rule or course term | Correct? (yes or no, and why) |
| --- | --- | --- | --- | --- | --- |
| 1 | `ParcelService.java:42` | Pickup compares parcel code strings with `==`, so it can fail for equal text in distinct String objects. | AI | Code quality / probable bug | Yes — the required run returned a zero fee and left the parcel stored. |
| 2 | `ParcelService.java:76-81` | CSV writer is not closed and write errors are swallowed. | AI | Code quality / resource handling | Yes — the CSV was empty after the starter run; an empty catch also hides file errors. |
| 3 | `Main.java:8` | Scanner is not closed. | AI | Code quality / resource handling | Yes — the input resource is never closed; fixed with try-with-resources. |
| 4 | `Main.java:11-13` | Splitting only on one literal space and indexing without validating arguments can fail on malformed commands. | AI | Code quality / robustness | Yes — repeated spaces, blank input, or missing fields can cause invalid parsing or indexing. |
| 5 | `ParcelService.java:6,12` | Unused `Date` import and unused `maxParcels` field. | AI | Code quality / unused code | Yes — neither is used in the starter implementation. |
| 6 | `Main.java:9,15,17,22` | Direct standard output calls are reported as logging replacements. | SonarQube | `java:S106` | Yes — those are the four findings shown in the SonarQube panel. |
| 7 | `ParcelService.java:24-33,74-82` | `ParcelService` handles locker allocation, state changes, output, fees, and persistence. | AI | Cohesion | Yes — unrelated responsibilities change in the same class. |
| 8 | `ParcelService.java:64-72` | `printParcels(boolean)` selects different behavior with a control flag. | AI | Cohesion | Yes — its behavior and output format change based on `forStaff`. |
| 9 | `Locker.java:5`, `Parcel.java:6`, `ParcelService.java:26,56-60` | Integer size codes must be interpreted consistently across classes. | AI | Connascence of convention/type | Yes — each class relies on shared numeric meanings. |
| 10 | `Parcel.java:9,16`, `ParcelService.java:28,44,68` | Parcel status is a public string convention used by multiple classes. | AI | Connascence of name/convention | Yes — public state can be changed without lifecycle checks. |
| 11 | `ParcelService.java:20-22` | `getLockers()` returns the mutable internal list. | AI | Coupling / encapsulation | Yes — callers can remove or reorder the service's lockers. |
| 12 | `ParcelService.java:30-31` | Domain service emits an SMS-like console message. | AI | Cohesion / coupling | Yes — presentation output is mixed into domain behavior. |

2.1 The two problems found only by the AI review were the combined responsibilities in `ParcelService` (cohesion) and the shared numeric size codes (connascence). The AI also identified several other design concerns in Table 2.

2.2 SonarQube alone reported the four `java:S106` direct-output findings. The AI review did not count CLI output as a code-quality defect.

2.3 I checked each AI finding against the original source and the required run. The string comparison and CSV writer findings were corroborated by the observed pickup and empty-file results; the encapsulation, cohesion, and shared-code findings are visible directly in the referenced declarations and calls.

## Task 3. Fix and check

Table 3. What you did with each SonarQube issue in Table 1

| No. in Table 1 | Fixed or kept | Commit, or the reason for keeping it |
| --- | --- | --- |
| 1-4 (`java:S106`) | Kept | Command-line feedback and receipts must appear in the terminal for this CLI lab. The logger suggestion would change the expected interactive output; this is the justified retained issue. |

Number of SonarQube issues that remain after your fixes: 6 direct-output findings (`java:S106`) in the updated root `Main.java`. The CLI must print its commands, receipts, lists, and staff view to the terminal.

The two design problems that you fixed

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| Cohesion: persistence mixed into `ParcelService` | Added `ParcelRepository` and `CsvParcelRepository`; service performs parcel/locker operations and delegates file saving. | `c2d5123` |
| Connascence/encapsulation: shared public status strings and exposed mutable list | Made parcel status private with lifecycle methods, locker size/number final, and returned unmodifiable locker/parcel lists. | `c2d5123` |

Additional fixes: pickup now compares string values with `equals`; CSV output uses try-with-resources and surfaces I/O failures; unused `Date` and `maxParcels` were removed; the CLI closes its scanner and handles blank lines/repeated spaces. The starter file paths are duplicated at repository root and under `parcelpoint/`; both source trees were updated to keep them consistent.

Output of the program after your fixes, for the commands `store P1 6501 2`, `pickup P1 5` and `staff`, and the content of parcels.csv:

```text
Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit
Locker 2
Fee: 30.0
P1 6501 PICKED_UP
Free lockers: 3

parcels.csv:
P1,PICKED_UP
```

## Task 4. AI-generated poor and good designs

The two projects use the same specification and commands: `rent <memberId> <student|staff> <station>` and `return <memberId> <station> <minutes>`. Each built with Maven. A student renting for 10 minutes paid 20 baht; a staff member renting for 5 minutes paid 15 baht in both programs. Each program wrote two completed rides to its `rides.csv`.

| Item | bikeshare-poor | bikeshare-good |
| --- | --- | --- |
| Date | 2026-10-25 | 2026-10-25 |
| AI tool and exact model | ChatGPT / Codex, GPT-6 | ChatGPT / Codex, GPT-6 |
| Prompt (B or C as given, or your changed prompt in full) | Prompt B used as provided; only its specification placeholder was replaced as instructed. Full prompt appears below. | Prompt C used as provided; only its specification placeholder was replaced as instructed. Full prompt appears below. |
| Builds with mvn package (yes or no) | Yes | Yes |
| Number of classes, interfaces, enums and records | 1 class | 9 types (6 classes, 1 interface, 1 enum, 2 records) |
| SonarQube issues, total | 11 | 5 |
| SonarQube issues, by rule | `java:S2386` × 4; `java:S3776` × 1; `java:S106` × 6 | `java:S106` × 5 |
| AI review findings in Check 1 | 1 | 2 |
| AI review findings in Check 2 | 5 | 0 |
| Review output file in the reviews folder | `reviews/bikeshare-poor-review.md` | `reviews/bikeshare-good-review.md` |

### Task 4 prompts used

The prompt wording below is copied from the repository `prompts.md`. The only substitution was replacing the specification placeholder with the complete specification, as directed by the lab.

**Prompt B — poor design**

```text
Write a Java 17 command-line program, built with Maven and with no external library, for the specification below. Give it a poor design on purpose. Put the menu, the fee rules, the bike records and the file saving into as few classes as possible, keep shared data in public static fields, use int codes for the member types, and pass a boolean flag that changes what a method does. Keep the code itself tidy, so close every file, handle every exception and leave no unused code. Keep the program short. Give me the pom.xml and every Java file.

Specification:
BikeShare lends bikes at three campus stations, Library, Canteen and Dorm, and each station starts with two bikes.
1. A member rents an available bike at a station by entering the member ID, the member type (student or staff) and the station.
2. The member returns the bike at any station and enters the ride time in minutes.
3. The fee is 2 baht per minute for a student and 3 baht per minute for a staff member.
4. The program prints a receipt with the bike, the minutes and the fee.
5. The program adds every finished ride to the file rides.csv.
6. The program reads commands from the keyboard until the user types quit.
```

**Prompt C — good design**

```text
Write a Java 17 command-line program, built with Maven and with no external library, for the specification below. Design it for high cohesion and low coupling. Give each class one responsibility, keep every field private, pass each method only the data it needs, use an enum for the member types, hide the file saving behind an interface, and keep the menu apart from the classes that hold the rules. Keep the code tidy and the program short. Give me the pom.xml and every Java file.

Specification:
BikeShare lends bikes at three campus stations, Library, Canteen and Dorm, and each station starts with two bikes.
1. A member rents an available bike at a station by entering the member ID, the member type (student or staff) and the station.
2. The member returns the bike at any station and enters the ride time in minutes.
3. The fee is 2 baht per minute for a student and 3 baht per minute for a staff member.
4. The program prints a receipt with the bike, the minutes and the fee.
5. The program adds every finished ride to the file rides.csv.
6. The program reads commands from the keyboard until the user types quit.
```

4.1 `bikeshare-poor` had four `java:S2386` findings on public mutable static fields, one `java:S3776` finding for cognitive complexity 16 (limit 15) in `main`, and six `java:S106` findings for direct console output. The good design had five `java:S106` findings in `ConsoleMenu`; those are terminal interaction output. The SonarQube panel displayed 22 total findings across the open projects, including the six ParcelPoint findings.

4.2 The AI reviews distinguished the designs. For `bikeshare-poor`, the AI found the public mutable static shared state and the one class owning the menu, state, fees, and CSV saving. These findings do not appear in the good-design review.

4.3 I would extend `bikeshare-good`. `MemberType` already owns the fee policy, so a visitor type can be added with a 4 baht rate without changing the menu's knowledge of integer codes or rewriting a monolithic workflow.

## Task 5. Reflection

5.1 SonarQube checks specific code patterns and reports rule-based locations, such as direct `System.out` use (`java:S106`). In this lab it did not report the ParcelPoint string-identity bug or the design problem of mixing persistence and allocation in one class; those required running the program and reviewing class relationships.

5.2 I should not trust an AI finding until I check it against the source and observed behavior. The starter pickup defect was supported by the code and run, while a design label such as “high coupling” is only useful when the review points to concrete fields, calls, or dependencies.

5.3 In Project Phase 2, I will run SonarQube while coding, record rule IDs and source locations, and run the affected behavior before accepting a suggested fix. I will also use AI to inspect cohesion, coupling, and connascence, then verify each claim in the source and keep design changes small.

## AI usage declaration

The lab's Level 3 AI Collaboration requirement applies. I used AI for reviews, generating the BikeShare variants, code changes, debugging support and report drafting. I checked the generated findings and changes against the source, SonarQube results, Maven builds, program runs and CSV files as shown above. Dates below are the dates of use; all listed uses occurred on 2026-10-25.

| Date | AI tool and exact model | Purpose and files or parts produced or changed | How I checked the output |
| --- | --- | --- | --- |
| 2026-10-25 | ChatGPT, GPT-6 | Reviewed the ParcelPoint starter for Task 2; produced `reviews/parcelpoint-review.md` and assisted with the findings and verification notes in this report. | Checked each finding against the referenced starter source lines; reproduced the pickup and empty-CSV behavior with the required run. |
| 2026-10-25 | ChatGPT, GPT-6 | Generated the poor BikeShare Maven project from Prompt B and the `bikeshare-poor` source files; produced `reviews/bikeshare-poor-review.md`. | Built with Maven, ran student and staff rentals/returns, checked both receipts and CSV rows, and compared each review finding with the source. |
| 2026-10-25 | ChatGPT, GPT-6 | Generated the good BikeShare Maven project from Prompt C and the `bikeshare-good` source files; produced `reviews/bikeshare-good-review.md`. | Built with Maven, ran the same student and staff scenarios, checked receipts and CSV rows, and compared each review finding with the source. |
| 2026-10-25 | ChatGPT, GPT-6 | Assisted with ParcelPoint refactoring and debugging in both tracked source trees, and drafted report answers and review artifacts. | Built all three Maven projects with Temurin 25.0.5; ran the required ParcelPoint commands and both BikeShare scenarios; inspected program output, CSV contents and source changes. |
| 2026-10-25 | ChatGPT, GPT-6 | Audited and updated this AI declaration and the report's Task 4 prompt record against the supplied requirement and repository `prompts.md`. | Compared the recorded prompt text against `prompts.md`, and checked the current Git status and commit history. |
