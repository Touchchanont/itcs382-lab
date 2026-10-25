package bikeshare;

public enum MemberType {
    STUDENT(2), STAFF(3);

    private final int bahtPerMinute;
    MemberType(int bahtPerMinute) { this.bahtPerMinute = bahtPerMinute; }
    public int feeFor(int minutes) { return minutes * bahtPerMinute; }

    public static MemberType from(String value) {
        return switch (value.toLowerCase()) {
            case "student" -> STUDENT;
            case "staff" -> STAFF;
            default -> throw new IllegalArgumentException("Member type must be student or staff");
        };
    }
}
