import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskCli {

    private List<Task> tasks;
    private final TaskRepository taskRepository;

    public TaskCli(TaskRepository taskRepository) throws IOException {
        this.taskRepository = taskRepository;
        this.tasks = taskRepository.getTasks();
    }

    public int add(String task) throws IOException {
        tasks.add(new Task(3, "toto", Task.Status.DONE, null, null));
        return 0;
    }
}
