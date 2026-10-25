package bikeshare;

public record Ride(String memberId, MemberType memberType, Bike bike, int minutes, int fee) {
    public static Ride complete(String memberId, MemberType type, Bike bike, int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Minutes must not be negative");
        return new Ride(memberId, type, bike, minutes, type.feeFor(minutes));
    }
}
