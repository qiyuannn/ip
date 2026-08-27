import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Event extends Task{
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private LocalDate from;
    private LocalDate to;

    public Event(String desc, LocalDate from, LocalDate to) {
        this(desc, false, from, to, true);
    }

    public Event(String desc, boolean done, LocalDate from, LocalDate to) {
        this(desc, done, from, to, false);
    }

    private Event(String desc, boolean done, LocalDate from, LocalDate to, boolean shouldPrint) {
        super(desc, done);
        this.from = from;
        this.to = to;

        if (shouldPrint) {
            System.out.println("Got it. I've added this task.");
            System.out.println(this.toString());
        }
    }

    @Override
    public void mark() {
        super.mark();
        System.out.println("I've marked this task as done.");
        System.out.println(this.toString());
    }

    @Override
    public void unmark() {
        super.unmark();
        System.out.println("I've marked this task as undone.");
        System.out.println(this.toString());
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
