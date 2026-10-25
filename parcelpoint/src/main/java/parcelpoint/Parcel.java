package parcelpoint;

public class Parcel {
    private final String code;
    private final String studentId;
    private final int size;
    private String status;

    public Parcel(String code, String studentId, int size) {
        this.code = code;
        this.studentId = studentId;
        this.size = size;
        this.status = "NEW";
    }

    public String getCode() { return code; }
    public String getStudentId() { return studentId; }
    public int getSize() { return size; }
    public String getStatus() { return status; }
    public void markStored() { status = "STORED"; }
    public void markPickedUp() { status = "PICKED_UP"; }
}
