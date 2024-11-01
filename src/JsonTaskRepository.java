import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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
    }

    @Override
    public List<Task> getTasks() throws IOException {
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
