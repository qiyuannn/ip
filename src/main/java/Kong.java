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
                CommandType command = parsedCommand.getCommand();
                String arg = parsedCommand.getArg();

                switch (command) {
                    case TODO: {
                        executeCommand(createTodoCommand(arg));
                        break;
                    }
                    case DEADLINE: {
                        executeCommand(createDeadlineCommand(arg));
                        break;
                    }
                    case EVENT: {
                        executeCommand(createEventCommand(arg));
                        break;
                    }
                    case LIST: {
                        executeCommand(new ListCommand());
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
                        Command exitCommand = new ExitCommand();
                        executeCommand(exitCommand);
                        if (exitCommand.isExit()) {
                            return;
                        }
                        break;
                    }
                    case UNKNOWN: {
                        executeCommand(new UnknownCommand());
                        break;
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

    private void executeCommand(Command command) throws KongException {
        command.execute(tasks, ui, storage);
    }

    private Command createTodoCommand(String description) throws KongException {
        if (description.isEmpty()) {
            throw new KongException("Invalid command. A todo command needs to be in the following format: todo <description>");
        }
        return new TodoCommand(description);
    }

    private Command createDeadlineCommand(String arg) throws KongException {
        Parser.DeadlineDetails deadlineDetails = Parser.parseDeadline(arg);
        return new DeadlineCommand(deadlineDetails.getDescription(), deadlineDetails.getBy());
    }

    private Command createEventCommand(String arg) throws KongException {
        Parser.EventDetails eventDetails = Parser.parseEvent(arg);
        return new EventCommand(eventDetails.getDescription(), eventDetails.getFrom(), eventDetails.getTo());
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
            Task task = tasks.get(parseTaskIndex(arg, "mark"));
            task.mark();
            ui.showTaskMarked(task);
            saveTasks();
        } catch (IndexOutOfBoundsException e) {
            throw invalidTaskNumberException();
        }
    }

    private void unmarkTask(String arg) throws KongException {
        try {
            Task task = tasks.get(parseTaskIndex(arg, "unmark"));
            task.unmark();
            ui.showTaskUnmarked(task);
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
