package kong;

import java.io.IOException;

import kong.command.Command;
import kong.exception.KongException;
import kong.parser.Parser;
import kong.storage.Storage;
import kong.task.TaskList;
import kong.ui.Ui;

/**
 * Coordinates Kong's storage, task list, parser, commands, and console UI.
 */
public class Kong {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates a Kong application backed by the specified task data file.
     *
     * @param filePath path to the file used to load and save tasks
     */
    public Kong(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = loadTasks();
    }

    /**
     * Runs the command-reading loop until the user issues an exit command.
     */
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

    /**
     * Starts Kong using the default task data file.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new Kong("data/duke.txt").run();
    }

    /**
     * Loads saved tasks, falling back to an empty list if the file cannot be read.
     *
     * @return the loaded task list, or an empty task list after a loading error
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (IOException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

}
