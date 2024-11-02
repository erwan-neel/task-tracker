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

    public void execute(String... args) throws IOException, InvalidTaskCliActionException {

        if (args.length < 1) {
            System.out.println("Usage: java -jar TaskCli.jar <task name>");
            return;
        }

        switch (Objects.requireNonNull(TaskCliAction.valueOfLabel(args[0]))) {
            case ADD:
                if (args.length < 2) {
                    System.out.println("Usage: java -jar TaskCli.jar add [description]");
                    return;
                }
                add(args[1]);
                break;
            case UPDATE: break;
            case DELETE: break;
            case MARK_IN_PROGRESS: break;
            case MARK_DONE: break;
            case LIST:
                if (args.length > 1) {
                    listByStatus(Task.Status.valueOfLabel(args[1]));
                } else {
                    list();
                }
                break;
            default:
                throw new InvalidTaskCliActionException("\"" + args[0] + "\" is not a known action.");
        }
        taskRepository.saveTasks(tasks);
    }

    private void list() {
        tasks.forEach(System.out::println);
    }

    private void listByStatus(Task.Status status) {
        tasks.stream()
                .filter(task -> status.equals(task.getStatus()))
                .forEach(System.out::println);
    }

    private void add(String description) {
        int lastId = 0;
        if (!tasks.isEmpty()) {
            lastId = tasks.get(tasks.size() -1).getId();
        }
        Task newTask = new Task(++lastId, description.replace("\"", ""), Task.Status.TODO,
                new Date(), null);
        tasks.add(newTask);

        System.out.println("Task added successfully (ID: " + lastId + ")");
    }
}
