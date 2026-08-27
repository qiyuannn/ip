import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Makes sense of user command text and turns it into structured values.
 */
public class Parser {
    private static final Pattern DEADLINE_PATTERN = Pattern.compile("^(.*?)\\s*/by\\s+(.*)$",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern EVENT_PATTERN = Pattern.compile("^(.*?)\\s*/from\\s+(.*?)\\s*/to\\s+(.*)$",
            Pattern.CASE_INSENSITIVE);

    public static ParsedCommand parse(String input) throws KongException {
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new KongException("Please enter a command.");
        }

        String[] parts = trimmedInput.split("\\s+", 2);
        Command command = Command.fromString(parts[0]);
        String arg = parts.length > 1 ? parts[1] : "";
        return new ParsedCommand(command, arg);
    }

    public static DeadlineDetails parseDeadline(String arg) throws KongException {
        Matcher matcher = DEADLINE_PATTERN.matcher(arg);
        if (!matcher.find()) {
            throw new KongException("Invalid command. A deadline command needs to be in the following format: deadline <description> /by <date>");
        }

        String description = matcher.group(1);
        String by = matcher.group(2);
        if (description.isEmpty() || by.isEmpty()) {
            throw new KongException("Invalid command. A deadline command needs to be in the following format: deadline <description> /by <date>");
        }
        return new DeadlineDetails(description, parseDate(by));
    }

    public static EventDetails parseEvent(String arg) throws KongException {
        Matcher matcher = EVENT_PATTERN.matcher(arg);
        if (!matcher.find()) {
            throw new KongException("Invalid command. An event command needs to be in the following format: event <description> /from <date> /to <date>");
        }

        String description = matcher.group(1);
        String from = matcher.group(2);
        String to = matcher.group(3);
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new KongException("Invalid command. An event command needs to be in the following format: event <description> /from <date> /to <date>");
        }
        return new EventDetails(description, parseDate(from), parseDate(to));
    }

    public static LocalDate parseDate(String dateText) throws KongException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new KongException("Invalid date. Please use the format yyyy-MM-dd, for example 2019-10-15.");
        }
    }

    /**
     * The command word and the remaining argument text from a user input line.
     */
    public static class ParsedCommand {
        private final Command command;
        private final String arg;

        public ParsedCommand(Command command, String arg) {
            this.command = command;
            this.arg = arg;
        }

        public Command getCommand() {
            return command;
        }

        public String getArg() {
            return arg;
        }
    }

    /**
     * Parsed description and date for a deadline command.
     */
    public static class DeadlineDetails {
        private final String description;
        private final LocalDate by;

        public DeadlineDetails(String description, LocalDate by) {
            this.description = description;
            this.by = by;
        }

        public String getDescription() {
            return description;
        }

        public LocalDate getBy() {
            return by;
        }
    }

    /**
     * Parsed description and dates for an event command.
     */
    public static class EventDetails {
        private final String description;
        private final LocalDate from;
        private final LocalDate to;

        public EventDetails(String description, LocalDate from, LocalDate to) {
            this.description = description;
            this.from = from;
            this.to = to;
        }

        public String getDescription() {
            return description;
        }

        public LocalDate getFrom() {
            return from;
        }

        public LocalDate getTo() {
            return to;
        }
    }
}
