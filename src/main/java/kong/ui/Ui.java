package kong.ui;

import java.util.ArrayList;
import java.util.Scanner;

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

    /** Creates a console UI that reads from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays Kong's banner and greeting. */
    public void showWelcome() {
        System.out.println(BANNER + LINE + "\n" + "Hello, I'm Kong.\nWhat can I do for you?");
    }

    /** Displays a divider between command interactions. */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Reads the user's next command line.
     *
     * @return entered command text
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays a user-facing error.
     *
     * @param message error explanation
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /** Displays a warning when stored tasks cannot be loaded. */
    public void showLoadingError() {
        System.out.println("Unable to load tasks from disk. Starting with an empty list.");
    }

    /**
     * Displays all current tasks or an empty-list message.
     *
     * @param tasks task list to display
     */
    public void showTaskList(TaskList tasks) {
        if (!tasks.isEmpty()) {
            System.out.println("Here are the tasks in your list.");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(String.format("%d. %s", i + 1, tasks.get(i).toString()));
            }
        } else {
            System.out.println("There are currently no tasks in your list.");
        }
    }

    /**
     * Displays tasks occurring on a requested date.
     *
     * @param matchingTasks tasks matching the date query
     */
    public void showTasksOnDate(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            System.out.println("There are no deadlines or events on this date.");
            return;
        }

        System.out.println("Here are the deadlines and events on this date.");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, matchingTasks.get(i).toString()));
        }
    }

    /**
     * Confirms that a task was added.
     *
     * @param task added task
     */
    public void showTaskAdded(Task task) {
        if (task instanceof Todo) {
            System.out.println("Got it. I've added this task");
        } else {
            System.out.println("Got it. I've added this task.");
        }
        System.out.println(task.toString());
    }

    /**
     * Confirms that a task was marked complete.
     *
     * @param task updated task
     */
    public void showTaskMarked(Task task) {
        System.out.println("I've marked this task as done.");
        System.out.println(task.toString());
    }

    /**
     * Confirms that a task was marked incomplete.
     *
     * @param task updated task
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("I've marked this task as undone.");
        System.out.println(task.toString());
    }

    /**
     * Confirms that a task was removed.
     *
     * @param task removed task
     */
    public void showTaskDeleted(Task task) {
        System.out.println("The following task have been removed.");
        System.out.println(task.toString());
    }

    /** Displays the exit message. */
    public void showGoodbye() {
        System.out.println("BYEBYE!");
    }
}
