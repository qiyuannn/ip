package kong.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kong.task.Deadline;
import kong.task.Event;
import kong.task.Task;
import kong.task.Todo;

/**
 * Loads and saves tasks using Kong's pipe-delimited text format.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage backed by the specified file.
     *
     * @param filePath path to the task data file
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Loads valid tasks from disk, skipping malformed lines.
     *
     * @return tasks found in the data file, or an empty list if it does not exist
     * @throws IOException if the data path cannot be read as a regular file
     */
    public ArrayList<Task> loadTasks() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }
        if (!Files.isRegularFile(filePath)) {
            throw new IOException("The data path is not a regular file.");
        }

        return Files.readAllLines(filePath).stream()
                .map(Storage::parseTask)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Replaces the data file with the current tasks, creating parent folders as needed.
     *
     * @param tasks tasks to save
     * @throws IOException if the data file cannot be written
     */
    public void saveTasks(ArrayList<Task> tasks) throws IOException {
        Path folderPath = filePath.getParent();
        if (folderPath != null) {
            Files.createDirectories(folderPath);
        }

        List<String> lines = tasks.stream()
                .map(Task::toFileString)
                .toList();
        Files.write(filePath, lines);
    }

    /**
     * Parses one stored line, rejecting unknown, incomplete, or malformed records.
     *
     * @param line stored task record
     * @return parsed task, or {@code null} if the record is invalid
     */
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
                return parseTodo(parts, description, isDone);
            case "D":
                return parseDeadline(parts, description, isDone);
            case "E":
                return parseEvent(parts, description, isDone);
            default:
                return null;
        }
    }

    /** Parses a todo record after its common fields have been validated. */
    private static Task parseTodo(String[] parts, String description, boolean isDone) {
        if (parts.length != 3) {
            return null;
        }
        return new Todo(description, isDone);
    }

    /** Parses a deadline record after its common fields have been validated. */
    private static Task parseDeadline(String[] parts, String description, boolean isDone) {
        if (parts.length != 4) {
            return null;
        }

        LocalDate dueDate = parseDate(getPart(parts, 3));
        if (dueDate == null) {
            return null;
        }
        return new Deadline(description, isDone, dueDate);
    }

    /** Parses an event record after its common fields have been validated. */
    private static Task parseEvent(String[] parts, String description, boolean isDone) {
        if (parts.length != 5) {
            return null;
        }

        LocalDate startDate = parseDate(getPart(parts, 3));
        LocalDate endDate = parseDate(getPart(parts, 4));
        if (startDate == null || endDate == null) {
            return null;
        }
        return new Event(description, isDone, startDate, endDate);
    }

    /**
     * Safely retrieves and trims a record field.
     *
     * @param parts record fields
     * @param index requested field index
     * @return trimmed field, or {@code null} if it is absent
     */
    private static String getPart(String[] parts, int index) {
        if (index >= parts.length) {
            return null;
        }
        return parts[index].trim();
    }

    /**
     * Converts the stored completion marker into a Boolean.
     *
     * @param doneStatus stored marker
     * @return parsed status, or {@code null} for an invalid marker
     */
    private static Boolean parseDoneStatus(String doneStatus) {
        if (doneStatus.equals("1")) {
            return true;
        }
        if (doneStatus.equals("0")) {
            return false;
        }
        return null;
    }

    /**
     * Parses an ISO date without allowing malformed records to stop loading.
     *
     * @param dateText stored date text
     * @return parsed date, or {@code null} if missing or invalid
     */
    private static LocalDate parseDate(String dateText) {
        if (dateText == null || dateText.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
