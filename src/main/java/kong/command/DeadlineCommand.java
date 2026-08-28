package kong.command;

import java.time.LocalDate;

import kong.exception.KongException;
import kong.storage.Storage;
import kong.task.Deadline;
import kong.task.Task;
import kong.task.TaskList;
import kong.ui.Ui;

/**
 * Adds a deadline task to the task list.
 */
public class DeadlineCommand extends Command {
    private final String description;
    private final LocalDate by;

    /**
     * Creates a command that adds a deadline.
     *
     * @param description task description
     * @param by deadline date
     */
    public DeadlineCommand(String description, LocalDate by) {
        this.description = description;
        this.by = by;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KongException {
        Task task = new Deadline(description, by);
        tasks.add(task);
        ui.showTaskAdded(task);
        saveTasks(tasks, storage);
    }
}
