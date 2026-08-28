package kong.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class TaskListTest {
    @Test
    void getTasksOnDateReturnsOnlyMatchingDeadlineAndEvent() {
        TaskList tasks = new TaskList();
        Task todo = new Todo("read book");
        Task deadline = new Deadline("return book", LocalDate.parse("2019-10-15"));
        Task matchingEvent = new Event("conference", LocalDate.parse("2019-10-14"), LocalDate.parse("2019-10-16"));
        Task nonMatchingEvent = new Event("holiday", LocalDate.parse("2019-10-20"), LocalDate.parse("2019-10-21"));
        tasks.add(todo);
        tasks.add(deadline);
        tasks.add(matchingEvent);
        tasks.add(nonMatchingEvent);

        assertEquals(2, tasks.getTasksOnDate(LocalDate.parse("2019-10-15")).size());
        assertEquals(deadline, tasks.getTasksOnDate(LocalDate.parse("2019-10-15")).get(0));
        assertEquals(matchingEvent, tasks.getTasksOnDate(LocalDate.parse("2019-10-15")).get(1));
    }

    @Test
    void addAndRemoveStoreAndRemoveTasksInOrder() {
        TaskList tasks = new TaskList();
        Task firstTask = new Todo("first");
        Task secondTask = new Todo("second");

        assertTrue(tasks.isEmpty());
        tasks.add(firstTask);
        tasks.add(secondTask);

        assertEquals(2, tasks.size());
        assertEquals(firstTask, tasks.get(0));
        assertEquals(firstTask, tasks.remove(0));
        assertEquals(secondTask, tasks.get(0));
        assertFalse(tasks.isEmpty());
    }
}
