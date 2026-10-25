# AI code and design review: original ParcelPoint starter

Files reviewed: `Locker.java`, `Main.java`, `Parcel.java`, and `ParcelService.java` in the original starter (`parcelpoint/src/main/java/parcelpoint/`), at commit `3e356f4`.

Review run: 2026-10-25, ChatGPT/Codex GPT-6. The code was read from the local workspace; no external upload was needed.

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
| --- | --- | --- | --- | --- | --- |
| 1 | Code quality | `ParcelService.java:42` | The parcel code is compared with `==`; separately created input strings can have equal text but different object identities, so pickup can fail for an existing parcel. | Must fix | Compare with `parcel.getCode().equals(code)`. |
| 2 | Code quality | `ParcelService.java:76-81` | `FileWriter` is not closed and the empty catch silently ignores write failures; buffered output may be lost and the caller cannot tell that persistence failed. | Must fix | Use try-with-resources and propagate or report the `IOException`. |
| 3 | Code quality | `Main.java:8` | The `Scanner` wrapping standard input is not closed. | Should fix | Use try-with-resources around the CLI input loop. |
| 4 | Code quality | `Main.java:11-13` | Splitting on one literal space and indexing tokens without checking length fails on blank input, repeated spaces, or incomplete commands. | Should fix | Ignore blank lines, split on whitespace, and validate the argument count before indexing. |
| 5 | Code quality | `ParcelService.java:6,12` | `java.util.Date` is unused and `maxParcels` is never read. | Note | Remove the unused import and field, or implement a real capacity rule if intended. |
| 6 | Code quality | `ParcelService.java:80-81` | The empty exception handler hides disk, permission, and path errors from the user. | Must fix | Surface a useful error or throw an exception with the original cause. |
| 7 | Cohesion / coupling | `ParcelService.java:24-33,74-82` | `ParcelService` coordinates locker allocation, status transitions, SMS-like console output, fees, and CSV persistence; these are separate responsibilities. | Should fix | Move CSV persistence behind a repository and keep console interaction in the command-line layer. |
| 8 | Cohesion | `ParcelService.java:64-72` | `printParcels(boolean)` changes between staff and customer output modes, so the method's work depends on a control flag rather than one focused operation. | Should fix | Give each caller a focused query/result and let the command-line layer format it. |
| 9 | Connascence | `Locker.java:5`, `Parcel.java:6`, `ParcelService.java:26,56-60` | Multiple classes must agree that integer sizes 1, 2, and 3 mean small, medium, and large; this is connascence of convention/type. | Should fix | Introduce a shared size enum or named domain type. |
| 10 | Connascence | `Parcel.java:9,16`, `ParcelService.java:28,44,68` | The parcel lifecycle is represented by public string values (`NEW`, `STORED`, `PICKED_UP`) repeated across classes; misspellings or visibility bypass can break behavior. | Should fix | Encapsulate status updates and use a private field with controlled methods or an enum. |
| 11 | Coupling / encapsulation | `ParcelService.java:20-22` | `getLockers()` returns the mutable internal list, allowing callers to remove or reorder service-owned lockers. | Should fix | Return an unmodifiable view or expose only the needed count/query operations. |
| 12 | Coupling | `Locker.java:29-31`, `ParcelService.java:41-46` | The service directly retrieves a parcel from a locker and coordinates its removal/status changes, coupling it to locker internals. | Note | Encapsulate the take/return operation in `Locker` if this interaction grows. |
| 13 | Cohesion | `ParcelService.java:52-62` | `fee` is a domain calculation but sits beside storage and presentation/persistence work in the same service. | Note | Keep fee policy in a focused policy/domain method if it expands. |

Manual verification: the pickup comparison and CSV writer findings were reproduced by the required run—the starter returned `Fee: 0.0` and left `parcels.csv` empty. SonarQube's four `java:S106` findings are direct CLI output calls; they are appropriate for this small interactive CLI and were retained with that justification.

Finding counts by prompt check: Check 1 (code quality): 6; Check 2 (cohesion, coupling, connascence): 7.
