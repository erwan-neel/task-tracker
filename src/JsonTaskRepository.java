import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JsonTaskRepository implements TaskRepository {

    private final Path filePath;

    public JsonTaskRepository(String jsonFilePath) throws IOException {
        filePath = Path.of(jsonFilePath);
    }

    @Override
    public void saveTasks(List<Task> tasks) throws IOException {
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < tasks.size(); i++) {
            String jsonTask = tasks.get(i).toString() + ",";
            if (i == tasks.size() - 1) {
                jsonTask = jsonTask.substring(0, jsonTask.length() - 1);
            }
            sb.append(jsonTask);
            sb.append("\n");
        }

        sb.append("]");
        Files.writeString(filePath, sb.toString());
    }

    @Override
    public List<Task> getTasks() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        String jsonTasks = Files.readString(filePath);
        String[] stringTasks = jsonTasks.replace("\n", "")
                .replace("[", "")
                .replace("]", "")
                .split("},");

        return Arrays.stream(stringTasks)
                .map(String::trim)
                .map(string -> string.replace("{", ""))
                .map(Task::fromString)
                .collect(Collectors.toList());
    }
}
