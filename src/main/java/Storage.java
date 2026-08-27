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
        if (!Files.isRegularFile(FILE_PATH)) {
            throw new IOException("The data path is not a regular file.");
        }

        List<String> lines = Files.readAllLines(FILE_PATH);
        for (String line : lines) {
            Task task = parseTask(line);
            if (task != null) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static void saveTasks(ArrayList<Task> tasks) throws IOException {
        Path folderPath = FILE_PATH.getParent();
        if (folderPath != null) {
            Files.createDirectories(folderPath);
        }

        ArrayList<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toFileString());
        }
        Files.write(FILE_PATH, lines);
    }

    private static Task parseTask(String line) {
        if (line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split("\\s*\\|\\s*", -1);
        String taskType = getPart(parts, 0);
        String doneStatus = getPart(parts, 1);
        String description = getPart(parts, 2);
        if (taskType == null || doneStatus == null || description == null || description.isEmpty()) {
            return null;
        }

        Boolean isDone = parseDoneStatus(doneStatus);
        if (isDone == null) {
            return null;
        }

        switch (taskType) {
            case "T":
                if (parts.length != 3) {
                    return null;
                }
                return new ToDo(description, isDone);
            case "D":
                String by = getPart(parts, 3);
                if (parts.length != 4 || by == null || by.isEmpty()) {
                    return null;
                }
                return new Deadline(description, isDone, by);
            case "E":
                String from = getPart(parts, 3);
                String to = getPart(parts, 4);
                if (parts.length != 5 || from == null || from.isEmpty() || to == null || to.isEmpty()) {
                    return null;
                }
                return new Event(description, isDone, from, to);
            default:
                return null;
        }
    }

    private static String getPart(String[] parts, int index) {
        if (index >= parts.length) {
            return null;
        }
        return parts[index].trim();
    }

    private static Boolean parseDoneStatus(String doneStatus) {
        if (doneStatus.equals("1")) {
            return true;
        }
        if (doneStatus.equals("0")) {
            return false;
        }
        return null;
    }
}
