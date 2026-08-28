import java.io.IOException;
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
                Command command = Parser.parse(scanner.nextLine());
                executeCommand(command);
                if (command.isExit()) {
                    return;
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

    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (IOException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

}
