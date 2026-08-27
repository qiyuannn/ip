import java.time.LocalDate;

public class Task {
    private String description;
    private boolean done = false;

    public Task(String desc) {
        this.description = desc;
    }

    public Task(String desc, boolean done) {
        this.description = desc;
        this.done = done;
    }

    public void mark() {
        this.done = true;
    }
    public void unmark() {
        this.done = false;
    }

    protected String getDescription() {
        return this.description;
    }

    protected String getDoneStatus() {
        return this.done ? "1" : "0";
    }

    public String toFileString() {
        return String.format("? | %s | %s", this.getDoneStatus(), this.description);
    }

    public boolean occursOn(LocalDate date) {
        return false;
    }

    @Override
    public String toString() {
        String out = "";
        if (this.done) {
            out += "[X]";
        } else {
            out += "[ ]";
        }
        return String.format("%s %s", out, this.description);
    }
}
