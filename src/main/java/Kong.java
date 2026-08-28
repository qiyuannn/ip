import java.io.IOException;

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

        boolean isExit = false;
        while (!isExit) {
            ui.showLine();
            try {
                String fullCommand = ui.readCommand();
                Command command = Parser.parse(fullCommand);
                executeCommand(command);
                isExit = command.isExit();
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

    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (IOException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

}
