package kong.command;

import kong.exception.KongException;
import kong.storage.Storage;
import kong.task.Task;
import kong.task.TaskList;
import kong.ui.Ui;

/**
 * Deletes a task from the task list.
 */
public class DeleteCommand extends Command {
    private final String taskNumber;

    public DeleteCommand(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KongException {
        try {
            Task task = tasks.remove(parseTaskIndex(taskNumber, "unmark"));
            ui.showTaskDeleted(task);
            saveTasks(tasks, storage);
        } catch (IndexOutOfBoundsException e) {
            throw invalidTaskNumberException(tasks);
        }
    }
}
