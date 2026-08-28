package kong;

import java.io.IOException;

import kong.command.Command;
import kong.exception.KongException;
import kong.parser.Parser;
import kong.storage.Storage;
import kong.task.TaskList;
import kong.ui.Ui;

/**
 * Coordinates the task list, persistent storage, command parsing, and console UI.
 */
public class Kong {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Kong(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = loadTasks();
    }

    /** Runs the command loop until the user exits. */
    public void run() {
        ui.showWelcome();

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (KongException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /** Starts Kong using its default data file. */
    public static void main(String[] args) {
        new Kong("data/duke.txt").run();
    }

    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (IOException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

}
