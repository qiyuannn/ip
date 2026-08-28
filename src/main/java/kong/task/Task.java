package kong.task;

import java.time.LocalDate;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private String description;
    private boolean isDone = false;

    /**
     * Creates an incomplete task.
     *
     * @param desc task description
     */
    public Task(String desc) {
        this.description = desc;
    }

    /**
     * Creates a task with an explicit completion status.
     *
     * @param desc task description
     * @param isDone whether the task is complete
     */
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

    /**
     * Returns the task description for display or storage formatting.
     *
     * @return task description
     */
    protected String getDescription() {
        return this.description;
    }

    /**
     * Returns the completion status in the format used by the data file.
     *
     * @return {@code "1"} when complete; otherwise {@code "0"}
     */
    protected String getDoneStatus() {
        return this.isDone ? "1" : "0";
    }

    /**
     * Formats this task for persistent storage.
     *
     * @return pipe-delimited task data
     */
    public String toFileString() {
        return String.format("? | %s | %s", this.getDoneStatus(), this.description);
    }

    /**
     * Indicates whether this task occurs on a date.
     *
     * @param date date to test
     * @return {@code false} for a task without a date
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    /**
     * Formats the task for display to the user.
     *
     * @return completion marker followed by the task description
     */
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
