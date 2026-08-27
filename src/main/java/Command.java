/**
 * Represents an executable user command.
 */
public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws KongException;

    public boolean isExit() {
        return false;
    }
}
