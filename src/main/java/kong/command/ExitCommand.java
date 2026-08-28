package kong.command;

import kong.storage.Storage;
import kong.task.TaskList;
import kong.ui.Ui;

/**
 * Ends the chatbot session.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
