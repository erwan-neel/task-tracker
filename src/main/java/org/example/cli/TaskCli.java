package org.example.cli;

import org.example.exception.InvalidTaskCliActionException;
import org.example.domain.Task;
import org.example.repository.TaskRepository;

import java.io.IOException;
import java.util.*;

public class TaskCli {

    private List<Task> tasks;
    private final TaskRepository taskRepository;

    public TaskCli(TaskRepository taskRepository) throws IOException {
        this.taskRepository = taskRepository;
        this.tasks = taskRepository.getTasks();
    }

    public String execute(String... args) throws IOException, InvalidTaskCliActionException {
        String outputMsg = "";

        switch (Objects.requireNonNull(TaskCliAction.valueOfLabel(args[0]))) {
            case ADD:
                int outputId = add(args[1]);
                outputMsg = "Task added successfully (ID: " + outputId + ")";
                break;
            case UPDATE:
                break;
            case DELETE:
                break;
            case MARK_IN_PROGRESS:
                break;
            case MARK_DONE:
                break;
            case LIST:
                break;
            default:
                throw new InvalidTaskCliActionException("\"" + args[0] + "\" is not a known action.");
        }

        taskRepository.saveTasks(tasks);

        return outputMsg;
    }

    private int add(String task) throws IOException {
        int lastId = 0;
        if (!tasks.isEmpty()) {
            lastId = tasks.get(tasks.size() -1).getId();
        }
        tasks.add(new Task(++lastId, task.replace("\"", ""), Task.Status.IN_PROGRESS, new Date(), null));

        return lastId;
    }
}
