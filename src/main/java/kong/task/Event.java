package kong.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that occurs over an inclusive date range.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private LocalDate from;
    private LocalDate to;

    /**
     * Creates an incomplete event.
     *
     * @param desc task description
     * @param from first date of the event
     * @param to last date of the event
     */
    public Event(String desc, LocalDate from, LocalDate to) {
        super(desc);
        this.from = from;
        this.to = to;
    }

    /**
     * Creates an event with an explicit completion status.
     *
     * @param desc task description
     * @param done whether the event is complete
     * @param from first date of the event
     * @param to last date of the event
     */
    public Event(String desc, boolean done, LocalDate from, LocalDate to) {
        super(desc, done);
        this.from = from;
        this.to = to;
    }

    /** {@inheritDoc} */
    @Override
    public boolean occursOn(LocalDate date) {
        return !date.isBefore(this.from) && !date.isAfter(this.to);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)",
                super.toString(), this.from.format(DISPLAY_DATE_FORMAT), this.to.format(DISPLAY_DATE_FORMAT));
    }

    /** {@inheritDoc} */
    @Override
    public String toFileString() {
        return String.format("E | %s | %s | %s | %s",
                this.getDoneStatus(), this.getDescription(), this.from, this.to);
    }
}
