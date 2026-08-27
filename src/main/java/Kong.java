import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class Kong {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Kong(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = loadTasks();
    }

    public void run() {
        ui.showWelcome();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            ui.showLine();
            try {
                Parser.ParsedCommand parsedCommand = Parser.parse(scanner.nextLine());
                Command command = parsedCommand.getCommand();
                String arg = parsedCommand.getArg();

                switch (command) {
                    case TODO: {
                        addTodo(arg);
                        break;
                    }
                    case DEADLINE: {
                        addDeadline(arg);
                        break;
                    }
                    case EVENT: {
                        addEvent(arg);
                        break;
                    }
                    case LIST: {
                        ui.showTaskList(tasks);
                        break;
                    }
                    case ON: {
                        printTasksOnDate(arg);
                        break;
                    }
                    case MARK: {
                        markTask(arg);
                        break;
                    }
                    case UNMARK: {
                        unmarkTask(arg);
                        break;
                    }
                    case DELETE: {
                        deleteTask(arg);
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

    public static void main(String[] args) {
        new Kong("data/duke.txt").run();
    }

    private void addTodo(String description) throws KongException {
        if (description.isEmpty()) {
            throw new KongException("Invalid command. A todo command needs to be in the following format: todo <description>");
        }
        tasks.add(new ToDo(description));
        saveTasks();
    }

    private void addDeadline(String arg) throws KongException {
        Parser.DeadlineDetails deadlineDetails = Parser.parseDeadline(arg);
        tasks.add(new Deadline(deadlineDetails.getDescription(), deadlineDetails.getBy()));
        saveTasks();
    }

    private void addEvent(String arg) throws KongException {
        Parser.EventDetails eventDetails = Parser.parseEvent(arg);
        tasks.add(new Event(eventDetails.getDescription(), eventDetails.getFrom(), eventDetails.getTo()));
        saveTasks();
    }

    private void printTasksOnDate(String arg) throws KongException {
        if (arg.isEmpty()) {
            throw new KongException("Invalid command. An on command needs to be in the following format: on <date>");
        }
        LocalDate date = Parser.parseDate(arg);
        ui.showTasksOnDate(tasks.getTasksOnDate(date));
    }

    private void markTask(String arg) throws KongException {
        try {
            tasks.get(parseTaskIndex(arg, "mark")).mark();
            saveTasks();
        } catch (IndexOutOfBoundsException e) {
            throw invalidTaskNumberException();
        }
    }

    private void unmarkTask(String arg) throws KongException {
        try {
            tasks.get(parseTaskIndex(arg, "unmark")).unmark();
            saveTasks();
        } catch (IndexOutOfBoundsException e) {
            throw invalidTaskNumberException();
        }
    }

    private void deleteTask(String arg) throws KongException {
        try {
            Task task = tasks.remove(parseTaskIndex(arg, "unmark"));
            ui.showTaskDeleted(task);
            saveTasks();
        } catch (IndexOutOfBoundsException e) {
            throw invalidTaskNumberException();
        }
    }

    private int parseTaskIndex(String arg, String commandName) throws KongException {
        try {
            return Integer.parseInt(arg) - 1;
        } catch (NumberFormatException e) {
            throw new KongException(String.format("Invalid command. A %s command needs to be followed by a number.",
                    commandName));
        }
    }

    private KongException invalidTaskNumberException() {
        return new KongException(String.format("This task number is invalid. You currently have %d tasks in your list.",
                tasks.size()));
    }

    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (IOException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

    private void saveTasks() throws KongException {
        try {
            storage.saveTasks(tasks.asList());
        } catch (IOException e) {
            throw new KongException("Unable to save tasks to disk.");
        }
    }
}
