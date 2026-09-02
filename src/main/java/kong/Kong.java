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
    private boolean isExitRequested;

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
     * Executes one command and returns all user-facing output as one response.
     * This method lets graphical interfaces reuse the same parser and commands as the console UI.
     *
     * @param input complete command entered by the user
     * @return response produced while executing the command
     */
    public String getResponse(String input) {
        StringBuilder response = new StringBuilder();
        Ui responseUi = new Ui(message -> appendResponse(response, message));
        isExitRequested = false;

        try {
            Command command = Parser.parse(input);
            command.execute(tasks, responseUi, storage);
            isExitRequested = command.isExit();
        } catch (KongException e) {
            responseUi.showError(e.getMessage());
        }

        return response.toString();
    }

    /**
     * Indicates whether the most recent GUI command asked Kong to exit.
     *
     * @return {@code true} if the most recent command was {@code bye}
     */
    public boolean isExitRequested() {
        return isExitRequested;
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

    /** Adds one UI message to a multi-line GUI response. */
    private static void appendResponse(StringBuilder response, String message) {
        if (response.length() > 0) {
            response.append(System.lineSeparator());
        }
        response.append(message);
    }

}
