package kong.ui;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

import kong.task.Task;
import kong.task.TaskList;
import kong.task.Todo;

/**
 * Handles interactions with the user through the console.
 */
public class Ui {
    private static final String LINE = "________________________________________";
    private static final String BANNER = " _  __                 \n"
            + "| |/ /___  _ __   __ _ \n"
            + "| ' // _ \\| '_ \\ / _` |\n"
            + "| . \\ (_) | | | | (_| |\n"
            + "|_|\\_\\___/|_| |_|\\__, |\n"
            + "                 |___/ \n";
    private final Scanner scanner;
    private final Consumer<String> output;

    /** Creates a console UI that reads from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
        output = System.out::println;
    }

    /**
     * Creates a response UI that sends each displayed message to the supplied consumer.
     *
     * @param output destination for user-facing messages
     */
    public Ui(Consumer<String> output) {
        scanner = null;
        this.output = output;
    }

    /** Displays Kong's banner and greeting. */
    public void showWelcome() {
        output.accept(BANNER + LINE + "\n" + "Hello, I'm Kong.\nWhat can I do for you?");
    }

    /** Displays a divider between command interactions. */
    public void showLine() {
        output.accept(LINE);
    }

    /**
     * Reads the user's next command line.
     *
     * @return entered command text
     */
    public String readCommand() {
        assert scanner != null : "A response-only UI cannot read commands";
        return scanner.nextLine();
    }

    /**
     * Displays a user-facing error.
     *
     * @param message error explanation
     */
    public void showError(String message) {
        output.accept(message);
    }

    /** Displays a warning when stored tasks cannot be loaded. */
    public void showLoadingError() {
        output.accept("Unable to load tasks from disk. Starting with an empty list.");
    }

    /**
     * Displays all current tasks or an empty-list message.
     *
     * @param tasks task list to display
     */
    public void showTaskList(TaskList tasks) {
        if (!tasks.isEmpty()) {
            output.accept("Here are the tasks in your list.");
            for (int i = 0; i < tasks.size(); i++) {
                output.accept(String.format("%d. %s", i + 1, tasks.get(i).toString()));
            }
        } else {
            output.accept("There are currently no tasks in your list.");
        }
    }

    /**
     * Displays tasks occurring on a requested date.
     *
     * @param matchingTasks tasks matching the date query
     */
    public void showTasksOnDate(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            output.accept("There are no deadlines or events on this date.");
            return;
        }

        output.accept("Here are the deadlines and events on this date.");
        for (int i = 0; i < matchingTasks.size(); i++) {
            output.accept(String.format("%d. %s", i + 1, matchingTasks.get(i).toString()));
        }
    }

    /**
     * Displays tasks matching a search keyword, or a no-match message.
     *
     * @param matchingTasks tasks matching the search keyword
     */
    public void showMatchingTasks(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            output.accept("There are no matching tasks in your list.");
            return;
        }

        output.accept("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            output.accept(String.format("%d. %s", i + 1, matchingTasks.get(i).toString()));
        }
    }

    /**
     * Confirms that a task was added.
     *
     * @param task added task
     */
    public void showTaskAdded(Task task) {
        if (task instanceof Todo) {
            output.accept("Got it. I've added this task");
        } else {
            output.accept("Got it. I've added this task.");
        }
        output.accept(task.toString());
    }

    /**
     * Confirms that a task was marked complete.
     *
     * @param task updated task
     */
    public void showTaskMarked(Task task) {
        output.accept("I've marked this task as done.");
        output.accept(task.toString());
    }

    /**
     * Confirms that a task was marked incomplete.
     *
     * @param task updated task
     */
    public void showTaskUnmarked(Task task) {
        output.accept("I've marked this task as undone.");
        output.accept(task.toString());
    }

    /**
     * Confirms that a task was removed.
     *
     * @param task removed task
     */
    public void showTaskDeleted(Task task) {
        output.accept("The following task have been removed.");
        output.accept(task.toString());
    }

    /** Displays the exit message. */
    public void showGoodbye() {
        output.accept("BYEBYE!");
    }
}
