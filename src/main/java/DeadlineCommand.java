import java.time.LocalDate;

/**
 * Adds a deadline task to the task list.
 */
public class DeadlineCommand extends Command {
    private final String description;
    private final LocalDate by;

    public DeadlineCommand(String description, LocalDate by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws KongException {
        Task task = new Deadline(description, by);
        tasks.add(task);
        ui.showTaskAdded(task);
        saveTasks(tasks, storage);
    }
}
