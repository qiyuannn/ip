package kong;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class KongTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void getResponseExecutesCommandAndKeepsUpdatedTaskList() {
        Kong kong = new Kong(temporaryDirectory.resolve("data/tasks.txt").toString());

        String addResponse = kong.getResponse("todo read book");
        String listResponse = kong.getResponse("list");

        assertTrue(addResponse.contains("Got it. I've added this task"));
        assertTrue(addResponse.contains("[T][ ] read book"));
        assertTrue(listResponse.contains("1. [T][ ] read book"));
    }

    @Test
    void getResponseReportsParsingErrorWithoutRequestingExit() {
        Kong kong = new Kong(temporaryDirectory.resolve("data/tasks.txt").toString());

        String response = kong.getResponse("todo");

        assertTrue(response.contains("todo <description>"));
        assertFalse(kong.isExitRequested());
    }

    @Test
    void getResponseRecordsExitRequest() {
        Kong kong = new Kong(temporaryDirectory.resolve("data/tasks.txt").toString());

        String response = kong.getResponse("bye");

        assertTrue(response.contains("BYEBYE!"));
        assertTrue(kong.isExitRequested());
    }
}
