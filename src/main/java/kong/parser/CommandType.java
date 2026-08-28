package kong.parser;

/**
 * Identifies the command words understood by Kong.
 */
public enum CommandType {
    TODO,
    DEADLINE,
    EVENT,
    LIST,
    ON,
    MARK,
    UNMARK,
    DELETE,
    BYE,
    UNKNOWN;

    /** Returns the command type represented by a case-insensitive command word. */
    public static CommandType fromString(String command) {
        try {
            return CommandType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CommandType.UNKNOWN;
        }
    }
}
