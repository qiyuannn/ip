import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.ArrayList;

public class Kong {
    public static void main(String[] args) {
        String banner = " _  __                 \n"
                + "| |/ /___  _ __   __ _ \n"
                + "| ' // _ \\| '_ \\ / _` |\n"
                + "| . \\ (_) | | | | (_| |\n"
                + "|_|\\_\\___/|_| |_|\\__, |\n"
                + "                 |___/ \n";

        String line = "________________________________________";
        String out = banner + line + "\n" + "Hello, I'm Kong.\nWhat can I do for you?";
        System.out.println(out);

        TaskList tasks = loadTasks();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(line);
            try {
                Parser.ParsedCommand parsedCommand = Parser.parse(scanner.nextLine());
                Command command = parsedCommand.getCommand();
                String arg = parsedCommand.getArg();

                switch (command) {
                    case TODO: {
                        if (arg.isEmpty()) {
                            throw new KongException("Invalid command. A todo command needs to be in the following format: todo <description>");
                        }
                        tasks.add(new ToDo(arg));
                        saveTasks(tasks);
                        break;
                    }
                    case DEADLINE: {
                        Parser.DeadlineDetails deadlineDetails = Parser.parseDeadline(arg);
                        tasks.add(new Deadline(deadlineDetails.getDescription(), deadlineDetails.getBy()));
                        saveTasks(tasks);
                        break;
                    }
                    case EVENT: {
                        Parser.EventDetails eventDetails = Parser.parseEvent(arg);
                        tasks.add(new Event(eventDetails.getDescription(), eventDetails.getFrom(), eventDetails.getTo()));
                        saveTasks(tasks);
                        break;
                    }
                    case LIST: {
                        if (!tasks.isEmpty()) {
                            System.out.println("Here are the tasks in your list.");
                            for (int i = 0; i < tasks.size(); i++) {
                                System.out.println(String.format("%d. %s", i + 1, tasks.get(i).toString()));
                            }
                        } else {
                            System.out.println("There are currently no tasks in your list.");
                        }
                        break;
                    }
                    case ON: {
                        if (arg.isEmpty()) {
                            throw new KongException("Invalid command. An on command needs to be in the following format: on <date>");
                        }
                        printTasksOnDate(tasks, Parser.parseDate(arg));
                        break;
                    }
                    case MARK: {
                        try {
                            tasks.get(Integer.parseInt(arg) - 1).mark();
                            saveTasks(tasks);
                        } catch (NumberFormatException e) {
                            throw new KongException("Invalid command. A mark command needs to be followed by a number.");
                        } catch (IndexOutOfBoundsException e) {
                            throw new KongException(String.format("This task number is invalid. You currently have %d tasks in your list.", tasks.size()));
                        }
                        break;
                    }
                    case UNMARK: {
                        try {
                            tasks.get(Integer.parseInt(arg) - 1).unmark();
                            saveTasks(tasks);
                        } catch (NumberFormatException e) {
                            throw new KongException("Invalid command. A unmark command needs to be followed by a number.");
                        } catch (IndexOutOfBoundsException e) {
                            throw new KongException(String.format("This task number is invalid. You currently have %d tasks in your list.", tasks.size()));
                        }
                        break;
                    }
                    case DELETE: {
                        try {
                            int ix = Integer.parseInt(arg) - 1;
                            Task task = tasks.remove(ix);
                            System.out.println("The following task have been removed.");
                            System.out.println(task.toString());
                            saveTasks(tasks);
                        } catch (NumberFormatException e) {
                            throw new KongException("Invalid command. A unmark command needs to be followed by a number.");
                        } catch (IndexOutOfBoundsException e) {
                            throw new KongException(String.format("This task number is invalid. You currently have %d tasks in your list.", tasks.size()));
                        }
                        break;
                    }
                    case BYE: {
                        System.out.println("BYEBYE!");
                        return;
                    }
                    case UNKNOWN: {
                        throw new KongException("Sorry we do not recognise that command yet.");
                    }
                }
            } catch (KongException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void printTasksOnDate(TaskList tasks, LocalDate date) {
        ArrayList<Task> matchingTasks = tasks.getTasksOnDate(date);
        if (matchingTasks.isEmpty()) {
            System.out.println("There are no deadlines or events on this date.");
            return;
        }

        System.out.println("Here are the deadlines and events on this date.");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, matchingTasks.get(i).toString()));
        }
    }

    private static TaskList loadTasks() {
        try {
            return new TaskList(Storage.loadTasks());
        } catch (IOException e) {
            System.out.println("Unable to load tasks from disk. Starting with an empty list.");
            return new TaskList();
        }
    }

    private static void saveTasks(TaskList tasks) throws KongException {
        try {
            Storage.saveTasks(tasks.asList());
        } catch (IOException e) {
            throw new KongException("Unable to save tasks to disk.");
        }
    }
}
