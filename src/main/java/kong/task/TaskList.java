package kong.task;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Holds the user's tasks and provides operations on the task collection.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this(new ArrayList<>());
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public Task remove(int index) {
        return tasks.remove(index);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public ArrayList<Task> asList() {
        return tasks;
    }

    /**
     * Returns all tasks that happen on the given date.
     */
    public ArrayList<Task> getTasksOnDate(LocalDate date) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
