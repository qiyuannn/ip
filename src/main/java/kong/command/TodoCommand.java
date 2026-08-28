package kong.command;

import kong.exception.KongException;
import kong.storage.Storage;
import kong.task.Task;
import kong.task.TaskList;
import kong.task.Todo;
import kong.ui.Ui;

/**
 * Adds a todo task to the task list.
 */
public class TodoCommand extends Command {
    private final String description;

    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KongException {
        Task task = new Todo(description);
        tasks.add(task);
        ui.showTaskAdded(task);
        saveTasks(tasks, storage);
    }
}
