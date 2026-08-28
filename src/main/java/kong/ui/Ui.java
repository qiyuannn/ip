package kong.ui;

import java.util.ArrayList;
import java.util.Scanner;

import kong.task.Task;
import kong.task.TaskList;
import kong.task.ToDo;

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

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println(BANNER + LINE + "\n" + "Hello, I'm Kong.\nWhat can I do for you?");
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void showLoadingError() {
        System.out.println("Unable to load tasks from disk. Starting with an empty list.");
    }

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
     * Displays tasks matching a search keyword, or a no-match message.
     */
    public void showMatchingTasks(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            System.out.println("There are no matching tasks in your list.");
            return;
        }

        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, matchingTasks.get(i).toString()));
        }
    }

    public void showTaskAdded(Task task) {
        if (task instanceof ToDo) {
            System.out.println("Got it. I've added this task");
        } else {
            System.out.println("Got it. I've added this task.");
        }
        System.out.println(task.toString());
    }

    public void showTaskMarked(Task task) {
        System.out.println("I've marked this task as done.");
        System.out.println(task.toString());
    }

    public void showTaskUnmarked(Task task) {
        System.out.println("I've marked this task as undone.");
        System.out.println(task.toString());
    }

    public void showTaskDeleted(Task task) {
        System.out.println("The following task have been removed.");
        System.out.println(task.toString());
    }

    public void showGoodbye() {
        System.out.println("BYEBYE!");
    }
}
