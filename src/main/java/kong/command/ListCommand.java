package kong.command;

import kong.storage.Storage;
import kong.task.TaskList;
import kong.ui.Ui;

/**
 * Shows all tasks in the task list.
 */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
