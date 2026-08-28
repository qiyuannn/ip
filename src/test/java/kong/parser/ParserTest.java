package kong.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

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

class ParserTest {
    @Test
    void parse_validCommands_returnsCorrespondingCommandType() throws KongException {
        assertInstanceOf(TodoCommand.class, Parser.parse("todo read book"));
        assertInstanceOf(DeadlineCommand.class, Parser.parse("deadline return book /by 2019-10-15"));
        assertInstanceOf(EventCommand.class, Parser.parse("event conference /from 2019-10-14 /to 2019-10-16"));
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
        assertInstanceOf(OnDateCommand.class, Parser.parse("on 2019-10-15"));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1"));
        assertInstanceOf(ExitCommand.class, Parser.parse("bye"));
        assertInstanceOf(UnknownCommand.class, Parser.parse("something else"));
    }

    @Test
    void parse_commandWordsWithDifferentCasing_returnsCorrespondingCommandType() throws KongException {
        assertInstanceOf(TodoCommand.class, Parser.parse("ToDo read book"));
        assertInstanceOf(ExitCommand.class, Parser.parse("BYE"));
    }

    @Test
    void parse_blankInput_throwsHelpfulException() {
        KongException exception = assertThrows(KongException.class, () -> Parser.parse("   "));

        assertEquals("Please enter a command.", exception.getMessage());
    }

    @Test
    void parse_missingRequiredArguments_throwsHelpfulException() {
        assertParseError("todo", "Invalid command. A todo command needs to be in the following format: todo <description>");
        assertParseError("deadline return book", "Invalid command. A deadline command needs to be in the following format: deadline <description> /by <date>");
        assertParseError("event conference /from 2019-10-14", "Invalid command. An event command needs to be in the following format: event <description> /from <date> /to <date>");
        assertParseError("on", "Invalid command. An on command needs to be in the following format: on <date>");
    }

    @Test
    void parse_invalidDates_throwsHelpfulException() {
        String expectedMessage = "Invalid date. Please use the format yyyy-MM-dd, for example 2019-10-15.";

        assertParseError("deadline return book /by tomorrow", expectedMessage);
        assertParseError("event conference /from tomorrow /to 2019-10-16", expectedMessage);
        assertParseError("on tomorrow", expectedMessage);
    }

    private static void assertParseError(String input, String expectedMessage) {
        KongException exception = assertThrows(KongException.class, () -> Parser.parse(input));

        assertEquals(expectedMessage, exception.getMessage());
    }
}
