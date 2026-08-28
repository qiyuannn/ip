package kong.parser;

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

    public static CommandType fromString(String command) {
        try {
            return CommandType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CommandType.UNKNOWN;
        }
    }
}
