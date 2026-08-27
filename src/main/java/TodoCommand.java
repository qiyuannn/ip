/**
 * Adds a todo task to the task list.
 */
public class TodoCommand extends Command {
    private final String description;

    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KongException {
        Task task = new ToDo(description);
        tasks.add(task);
        ui.showTaskAdded(task);
        saveTasks(tasks, storage);
    }
}
