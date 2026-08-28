package kong.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kong.command.Command;
import kong.command.DeadlineCommand;
import kong.command.DeleteCommand;
import kong.command.EventCommand;
import kong.command.ExitCommand;
import kong.command.ListCommand;
import kong.command.MarkCommand;
import kong.command.OnDateCommand;
import kong.command.TodoCommand;
import kong.command.UnknownCommand;
import kong.command.UnmarkCommand;
import kong.exception.KongException;

/**
 * Makes sense of user command text and turns it into structured values.
 */
public class Parser {
    private static final String ERROR_TODO_FORMAT =
            "Invalid command. A todo command needs to be in the following format: todo <description>";
    private static final String ERROR_DEADLINE_FORMAT =
            "Invalid command. A deadline command needs to be in the following format: "
                    + "deadline <description> /by <date>";
    private static final String ERROR_EVENT_FORMAT =
            "Invalid command. An event command needs to be in the following format: "
                    + "event <description> /from <date> /to <date>";
    private static final Pattern DEADLINE_PATTERN = Pattern.compile("^(.*?)\\s*/by\\s+(.*)$",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern EVENT_PATTERN = Pattern.compile("^(.*?)\\s*/from\\s+(.*?)\\s*/to\\s+(.*)$",
            Pattern.CASE_INSENSITIVE);

    /**
     * Parses a user-entered line into an executable command.
     *
     * @return command represented by the input
     * @throws KongException if a recognized command has invalid arguments
     */
    public static Command parse(String input) throws KongException {
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new KongException("Please enter a command.");
        }

        String[] parts = trimmedInput.split("\\s+", 2);
        CommandType commandType = CommandType.fromString(parts[0]);
        String arg = parts.length > 1 ? parts[1] : "";

        switch (commandType) {
            case TODO:
                return createTodoCommand(arg);
            case DEADLINE:
                return createDeadlineCommand(arg);
            case EVENT:
                return createEventCommand(arg);
            case LIST:
                return new ListCommand();
            case ON:
                return createOnDateCommand(arg);
            case MARK:
                return new MarkCommand(arg);
            case UNMARK:
                return new UnmarkCommand(arg);
            case DELETE:
                return new DeleteCommand(arg);
            case BYE:
                return new ExitCommand();
            case UNKNOWN:
                return new UnknownCommand();
            default:
                return new UnknownCommand();
        }
    }

    private static Command createTodoCommand(String description) throws KongException {
        if (description.isEmpty()) {
            throw new KongException(ERROR_TODO_FORMAT);
        }
        return new TodoCommand(description);
    }

    private static Command createDeadlineCommand(String arg) throws KongException {
        DeadlineDetails deadlineDetails = parseDeadline(arg);
        return new DeadlineCommand(deadlineDetails.getDescription(), deadlineDetails.getBy());
    }

    private static Command createEventCommand(String arg) throws KongException {
        EventDetails eventDetails = parseEvent(arg);
        return new EventCommand(eventDetails.getDescription(), eventDetails.getFrom(), eventDetails.getTo());
    }

    private static Command createOnDateCommand(String arg) throws KongException {
        if (arg.isEmpty()) {
            throw new KongException("Invalid command. An on command needs to be in the following format: on <date>");
        }
        return new OnDateCommand(parseDate(arg));
    }

    private static DeadlineDetails parseDeadline(String arg) throws KongException {
        Matcher matcher = DEADLINE_PATTERN.matcher(arg);
        if (!matcher.find()) {
            throw new KongException(ERROR_DEADLINE_FORMAT);
        }

        String description = matcher.group(1);
        String by = matcher.group(2);
        if (description.isEmpty() || by.isEmpty()) {
            throw new KongException(ERROR_DEADLINE_FORMAT);
        }
        return new DeadlineDetails(description, parseDate(by));
    }

    private static EventDetails parseEvent(String arg) throws KongException {
        Matcher matcher = EVENT_PATTERN.matcher(arg);
        if (!matcher.find()) {
            throw new KongException(ERROR_EVENT_FORMAT);
        }

        String description = matcher.group(1);
        String from = matcher.group(2);
        String to = matcher.group(3);
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new KongException(ERROR_EVENT_FORMAT);
        }
        return new EventDetails(description, parseDate(from), parseDate(to));
    }

    private static LocalDate parseDate(String dateText) throws KongException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new KongException("Invalid date. Please use the format yyyy-MM-dd, for example 2019-10-15.");
        }
    }

    /**
     * Parsed description and date for a deadline command.
     */
    private static class DeadlineDetails {
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
    private static class EventDetails {
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
