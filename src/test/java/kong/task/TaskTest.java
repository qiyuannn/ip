package kong.task;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class TaskTest {
    @Test
    void constructorRejectsDescriptionThatWasNotValidated() {
        assertThrows(AssertionError.class, () -> new Todo(" "));
    }

    @Test
    void deadlineConstructorRejectsMissingValidatedDate() {
        assertThrows(AssertionError.class, () -> new Deadline("return book", null));
    }

    @Test
    void eventConstructorRejectsMissingValidatedDates() {
        LocalDate validDate = LocalDate.parse("2019-10-15");

        assertThrows(AssertionError.class, () -> new Event("conference", null, validDate));
        assertThrows(AssertionError.class, () -> new Event("conference", validDate, null));
    }

    @Test
    void constructorRejectsDescriptionWithForbiddenCharacters() {
        assertThrows(AssertionError.class, () -> new Todo("buy milk | bread"));
        assertThrows(AssertionError.class, () -> new Todo("buy milk\nbread"));
    }

    @Test
    void eventConstructorRejectsStartDateAfterEndDate() {
        LocalDate startDate = LocalDate.parse("2019-10-16");
        LocalDate endDate = LocalDate.parse("2019-10-14");

        assertThrows(AssertionError.class, () -> new Event("conference", startDate, endDate));
    }
}
