package kong.task;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Holds the user's tasks and provides operations on the task collection.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this(new ArrayList<>());
    }

    /**
     * Creates a task list backed by the supplied collection.
     *
     * @param tasks initial tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at a zero-based index.
     *
     * @param index zero-based task index
     * @return task at the index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at a zero-based index.
     *
     * @param index zero-based task index
     * @return removed task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks.
     *
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Indicates whether the list contains no tasks.
     *
     * @return {@code true} when the list is empty
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the underlying task collection for persistence.
     *
     * @return current tasks
     */
    public ArrayList<Task> asList() {
        return tasks;
    }

    /**
     * Returns all tasks that happen on the given date.
     *
     * @param date date to query
     * @return matching deadlines and events in list order
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
