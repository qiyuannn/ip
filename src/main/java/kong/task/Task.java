package kong.task;

import java.time.LocalDate;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private String description;
    private boolean isDone = false;

    public Task(String desc) {
        this.description = desc;
    }

    public Task(String desc, boolean isDone) {
        this.description = desc;
        this.isDone = isDone;
    }

    /** Marks this task as complete. */
    public void mark() {
        this.isDone = true;
    }
    /** Marks this task as incomplete. */
    public void unmark() {
        this.isDone = false;
    }

    protected String getDescription() {
        return this.description;
    }

    protected String getDoneStatus() {
        return this.isDone ? "1" : "0";
    }

    /** Returns the task in the format used by the data file. */
    public String toFileString() {
        return String.format("? | %s | %s", this.getDoneStatus(), this.description);
    }

    /** Returns whether this task occurs on the specified date. */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    /** Returns the task in its user-facing display format. */
    @Override
    public String toString() {
        String out = "";
        if (this.isDone) {
            out += "[X]";
        } else {
            out += "[ ]";
        }
        return String.format("%s %s", out, this.description);
    }
}
