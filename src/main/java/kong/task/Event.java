package kong.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task occurring over an inclusive date range.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private LocalDate from;
    private LocalDate to;

    public Event(String desc, LocalDate from, LocalDate to) {
        super(desc);
        this.from = from;
        this.to = to;
    }

    public Event(String desc, boolean isDone, LocalDate from, LocalDate to) {
        super(desc, isDone);
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return !date.isBefore(this.from) && !date.isAfter(this.to);
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)",
                super.toString(), this.from.format(DISPLAY_DATE_FORMAT), this.to.format(DISPLAY_DATE_FORMAT));
    }

    @Override
    public String toFileString() {
        return String.format("E | %s | %s | %s | %s",
                this.getDoneStatus(), this.getDescription(), this.from, this.to);
    }
}
