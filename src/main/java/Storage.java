import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final Path FILE_PATH = Path.of("data", "duke.txt");

    public static ArrayList<Task> loadTasks() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(FILE_PATH)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(FILE_PATH);
        for (String line : lines) {
            tasks.add(parseTask(line));
        }
        return tasks;
    }

    public static void saveTasks(ArrayList<Task> tasks) throws IOException {
        Files.createDirectories(FILE_PATH.getParent());

        ArrayList<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toFileString());
        }
        Files.write(FILE_PATH, lines);
    }

    private static Task parseTask(String line) throws IOException {
        String[] parts = line.split(" \\| ");
        String taskType = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        switch (taskType) {
            case "T":
                return new ToDo(description, isDone);
            case "D":
                return new Deadline(description, isDone, parts[3]);
            case "E":
                return new Event(description, isDone, parts[3], parts[4]);
            default:
                throw new IOException("Unknown task type: " + taskType);
        }
    }
}
