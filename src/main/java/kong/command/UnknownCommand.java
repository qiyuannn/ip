package kong.command;

import kong.exception.KongException;
import kong.storage.Storage;
import kong.task.TaskList;
import kong.ui.Ui;

/**
 * Represents a command word that Kong does not understand.
 */
public class UnknownCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KongException {
        throw new KongException("Sorry we do not recognise that command yet.");
    }
}
