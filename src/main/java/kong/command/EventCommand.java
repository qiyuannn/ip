package kong.command;

import java.time.LocalDate;

import kong.exception.KongException;
import kong.storage.Storage;
import kong.task.Event;
import kong.task.Task;
import kong.task.TaskList;
import kong.ui.Ui;

/**
 * Adds an event task to the task list.
 */
public class EventCommand extends Command {
    private final String description;
    private final LocalDate from;
    private final LocalDate to;

    /**
     * Creates a command that adds an event.
     *
     * @param description task description
     * @param from first date of the event
     * @param to last date of the event
     */
    public EventCommand(String description, LocalDate from, LocalDate to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KongException {
        Task task = new Event(description, from, to);
        tasks.add(task);
        ui.showTaskAdded(task);
        saveTasks(tasks, storage);
    }
}
