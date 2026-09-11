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
}
