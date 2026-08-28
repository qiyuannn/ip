package kong.command;

import java.io.IOException;

import kong.exception.KongException;
import kong.storage.Storage;
import kong.task.TaskList;
import kong.ui.Ui;

/**
 * Represents an executable user command.
 */
public abstract class Command {
    /** Executes this command against the current task list. */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws KongException;

    /** Returns whether this command should end the application. */
    public boolean isExit() {
        return false;
    }

    protected void saveTasks(TaskList tasks, Storage storage) throws KongException {
        try {
            storage.saveTasks(tasks.asList());
        } catch (IOException e) {
            throw new KongException("Unable to save tasks to disk.");
        }
    }

    protected int parseTaskIndex(String arg, String commandName) throws KongException {
        try {
            return Integer.parseInt(arg) - 1;
        } catch (NumberFormatException e) {
            throw new KongException(String.format("Invalid command. A %s command needs to be followed by a number.",
                    commandName));
        }
    }

    protected KongException invalidTaskNumberException(TaskList tasks) {
        return new KongException(String.format("This task number is invalid. You currently have %d tasks in your list.",
                tasks.size()));
    }
}
