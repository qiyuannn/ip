package kong.task;

/**
 * Represents a task without an associated date.
 */
public class ToDo extends Task{
    /**
     * Creates an incomplete todo.
     *
     * @param desc task description
     */
    public ToDo(String desc) {
        super(desc);
    }

    /**
     * Creates a todo with an explicit completion status.
     *
     * @param desc task description
     * @param done whether the todo is complete
     */
    public ToDo(String desc, boolean done) {
        super(desc, done);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }

    /** {@inheritDoc} */
    @Override
    public String toFileString() {
        return String.format("T | %s | %s", this.getDoneStatus(), this.getDescription());
    }
}
