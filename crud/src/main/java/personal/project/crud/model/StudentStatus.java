package personal.project.crud.model;

public enum StudentStatus {
    ADMIT("Student Enrolled in the University."),
    DEFERRED("Alumni Student!"),
    REFERRED("Student Terminated"),
    WAIT_LIST(""),
    DENIED(""),
    CANCELLED("");

    private final String STATUS;

    StudentStatus(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getSTATUS() {
        return STATUS;
    }

    @Override
    public String toString() {
        return STATUS;
    }
}
