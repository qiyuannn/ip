import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class Kong {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        TaskList tasks = loadTasks(ui);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            ui.showLine();
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
                        ui.showTaskList(tasks);
                        break;
                    }
                    case ON: {
                        if (arg.isEmpty()) {
                            throw new KongException("Invalid command. An on command needs to be in the following format: on <date>");
                        }
                        printTasksOnDate(ui, tasks, Parser.parseDate(arg));
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
                            ui.showTaskDeleted(task);
                            saveTasks(tasks);
                        } catch (NumberFormatException e) {
                            throw new KongException("Invalid command. A unmark command needs to be followed by a number.");
                        } catch (IndexOutOfBoundsException e) {
                            throw new KongException(String.format("This task number is invalid. You currently have %d tasks in your list.", tasks.size()));
                        }
                        break;
                    }
                    case BYE: {
                        ui.showGoodbye();
                        return;
                    }
                    case UNKNOWN: {
                        throw new KongException("Sorry we do not recognise that command yet.");
                    }
                }
            } catch (KongException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private static void printTasksOnDate(Ui ui, TaskList tasks, LocalDate date) {
        ui.showTasksOnDate(tasks.getTasksOnDate(date));
    }

    private static TaskList loadTasks(Ui ui) {
        try {
            return new TaskList(Storage.loadTasks());
        } catch (IOException e) {
            ui.showLoadingError();
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
