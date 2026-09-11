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
import kong.command.FindCommand;
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
    private static final String ERROR_FIND_FORMAT =
            "Invalid command. A find command needs to be in the following format: find <keyword>";
    private static final Pattern DEADLINE_PATTERN = Pattern.compile("^(.*?)\\s*/by\\s+(.*)$",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern EVENT_PATTERN = Pattern.compile("^(.*?)\\s*/from\\s+(.*?)\\s*/to\\s+(.*)$",
            Pattern.CASE_INSENSITIVE);

    /** Creates a parser. */
    public Parser() {
    }

    /**
     * Parses a complete input line into an executable command.
     *
     * @param input user-entered command line
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
            case FIND:
                return createFindCommand(arg);
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
                assert false : "Every command type must be handled by the parser";
                return new UnknownCommand();
        }
    }

    /** Creates a todo command after validating its description. */
    private static Command createTodoCommand(String description) throws KongException {
        if (description.isEmpty()) {
            throw new KongException(ERROR_TODO_FORMAT);
        }
        return new TodoCommand(description);
    }

    /** Creates a deadline command from its structured arguments. */
    private static Command createDeadlineCommand(String arg) throws KongException {
        DeadlineDetails deadlineDetails = parseDeadline(arg);
        return new DeadlineCommand(deadlineDetails.getDescription(), deadlineDetails.getBy());
    }

    /** Creates an event command from its structured arguments. */
    private static Command createEventCommand(String arg) throws KongException {
        EventDetails eventDetails = parseEvent(arg);
        return new EventCommand(eventDetails.getDescription(), eventDetails.getFrom(), eventDetails.getTo());
    }

    /** Creates a date-query command after validating its date argument. */
    private static Command createOnDateCommand(String arg) throws KongException {
        if (arg.isEmpty()) {
            throw new KongException("Invalid command. An on command needs to be in the following format: on <date>");
        }
        return new OnDateCommand(parseDate(arg));
    }

    /** Creates a find command after validating its keyword. */
    private static Command createFindCommand(String keyword) throws KongException {
        if (keyword.isEmpty()) {
            throw new KongException(ERROR_FIND_FORMAT);
        }
        return new FindCommand(keyword);
    }

    /**
     * Extracts the description and due date from deadline arguments.
     *
     * @param arg text following the deadline command word
     * @return validated deadline details
     * @throws KongException if the arguments do not match the required format
     */
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

    /**
     * Extracts the description and date range from event arguments.
     *
     * @param arg text following the event command word
     * @return validated event details
     * @throws KongException if the arguments do not match the required format
     */
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

    /**
     * Parses a date in ISO {@code yyyy-MM-dd} format.
     *
     * @param dateText date entered by the user
     * @return parsed date
     * @throws KongException if the text is not a valid ISO date
     */
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

        /** Creates a parsed deadline value. */
        public DeadlineDetails(String description, LocalDate by) {
            this.description = description;
            this.by = by;
        }

        /** @return parsed task description */
        public String getDescription() {
            return description;
        }

        /** @return parsed due date */
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

        /** Creates a parsed event value. */
        public EventDetails(String description, LocalDate from, LocalDate to) {
            this.description = description;
            this.from = from;
            this.to = to;
        }

        /** @return parsed task description */
        public String getDescription() {
            return description;
        }

        /** @return parsed first date */
        public LocalDate getFrom() {
            return from;
        }

        /** @return parsed last date */
        public LocalDate getTo() {
            return to;
        }
    }
}
