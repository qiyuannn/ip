package kong.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that is due on a specific date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private LocalDate by;

    /**
     * Creates an incomplete deadline.
     *
     * @param desc task description
     * @param by due date
     */
    public Deadline(String desc, LocalDate by) {
        super(desc);
        this.by = by;
    }

    /**
     * Creates a deadline with an explicit completion status.
     *
     * @param desc task description
     * @param done whether the deadline is complete
     * @param by due date
     */
    public Deadline(String desc, boolean done, LocalDate by) {
        super(desc, done);
        this.by = by;
    }

    /** {@inheritDoc} */
    @Override
    public boolean occursOn(LocalDate date) {
        return this.by.equals(date);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.by.format(DISPLAY_DATE_FORMAT));
    }

    /** {@inheritDoc} */
    @Override
    public String toFileString() {
        return String.format("D | %s | %s | %s", this.getDoneStatus(), this.getDescription(), this.by);
    }
}
