# AI review: bikeshare-poor

Files read: `bikeshare-poor/src/main/java/bikeshare/Main.java`.
Review run: 2026-10-25, ChatGPT/Codex GPT-6; local workspace source.

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
| --- | --- | --- | --- | --- | --- |
| 1 | Cohesion | `Main.java:14-69` | The one class owns the CLI loop, station inventory, rental state, fee calculation, receipts, and CSV persistence, giving it many unrelated reasons to change. | Should fix | Split the menu, rental service/domain state, and persistence responsibilities. |
| 2 | Coupling / encapsulation | `Main.java:14-17` | The station, rental, member-type, and ride stores are public mutable static collections, so any caller can bypass the rules and corrupt shared state. | Must fix | Make state private and expose operations that preserve invariants. |
| 3 | Connascence | `Main.java:43,54-55,64` | The integer codes `1` and `2` must be interpreted consistently as student and staff in multiple branches; this is connascence of type/convention. | Should fix | Use a `MemberType` enum with its fee policy. |
| 4 | Connascence | `Main.java:59-62,68-72` | `saveRides` receives a boolean whose meaning controls truncate-versus-append behavior, leaving its callers coupled to a hidden operation mode. | Should fix | Use an append-only save operation with no mode flag. |
| 5 | Coupling / domain representation | `Main.java:15-17,38-42` | Bikes and stations are represented as strings and nested lists/maps; code relies on naming conventions such as `<station>-1`. | Note | Use `Bike` and `Station` domain types. |
| 6 | Code quality | `Main.java:51-55` | A member ID is the sole key for active rental and type, so a second rent by the same member overwrites the prior bike and loses the association. | Should fix | Reject a second active rental or key rentals independently. |

No unused code was found. CSV writers and scanner are closed and exceptions are handled. The two sample rides were verified in the built program; both receipts and both CSV rows were produced.

Finding counts by prompt check: Check 1 (code quality): 1; Check 2 (cohesion, coupling, connascence): 5.
