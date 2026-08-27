import java.io.IOException;

/**
 * Represents an executable user command.
 */
public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws KongException;

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
}
