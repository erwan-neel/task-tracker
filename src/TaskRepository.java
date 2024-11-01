import java.io.IOException;
import java.util.List;

public interface TaskRepository {
    void saveTasks(List<Task> task) throws IOException;

    List<Task> getTasks() throws IOException;
}
