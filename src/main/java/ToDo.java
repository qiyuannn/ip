public class ToDo extends Task{
    public ToDo(String desc) {
        super(desc);
    }

    public ToDo(String desc, boolean done) {
        super(desc, done);
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }

    @Override
    public String toFileString() {
        return String.format("T | %s | %s", this.getDoneStatus(), this.getDescription());
    }
}
