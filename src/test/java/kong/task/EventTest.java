package kong.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EventTest {
    @ParameterizedTest
    @CsvSource({
        "2019-10-13, false",
        "2019-10-14, true",
        "2019-10-15, true",
        "2019-10-16, true",
        "2019-10-17, false"
    })
    void occursOnReturnsWhetherDateIsInRangeForMultiDayEvent(String dateText, boolean expected) {
        Event event = new Event("conference", LocalDate.parse("2019-10-14"), LocalDate.parse("2019-10-16"));

        assertEquals(expected, event.occursOn(LocalDate.parse(dateText)));
    }

    @ParameterizedTest
    @CsvSource({
        "2019-10-14, false",
        "2019-10-15, true",
        "2019-10-16, false"
    })
    void occursOnReturnsWhetherDateMatchesOneDayEvent(String dateText, boolean expected) {
        Event event = new Event("workshop", LocalDate.parse("2019-10-15"), LocalDate.parse("2019-10-15"));

        assertEquals(expected, event.occursOn(LocalDate.parse(dateText)));
    }

    @ParameterizedTest
    @CsvSource({
        "false, [E][ ] conference (from: Oct 14 2019 to: Oct 16 2019), "
                + "E | 0 | conference | 2019-10-14 | 2019-10-16",
        "true, [E][X] conference (from: Oct 14 2019 to: Oct 16 2019), "
                + "E | 1 | conference | 2019-10-14 | 2019-10-16"
    })
    void displayAndStorageFormatsShowDoneStatusConsistently(boolean isDone, String expectedDisplay,
            String expectedFileText) {
        Event event = new Event("conference", isDone, LocalDate.parse("2019-10-14"),
                LocalDate.parse("2019-10-16"));

        assertEquals(expectedDisplay, event.toString());
        assertEquals(expectedFileText, event.toFileString());
    }
}
