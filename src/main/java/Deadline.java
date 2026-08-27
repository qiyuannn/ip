import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private LocalDate by;

    public Deadline(String desc, LocalDate by) {
        super(desc);
        this.by = by;
    }

    public Deadline(String desc, boolean done, LocalDate by) {
        super(desc, done);
        this.by = by;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return this.by.equals(date);
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.by.format(DISPLAY_DATE_FORMAT));
    }

    @Override
    public String toFileString() {
        return String.format("D | %s | %s | %s", this.getDoneStatus(), this.getDescription(), this.by);
    }
}
