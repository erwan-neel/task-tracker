package org.example.cli;

import org.example.exception.InvalidTaskCliActionException;
import org.example.domain.Task;
import org.example.exception.InvalidTaskStatusException;
import org.example.exception.TaskNotFoundException;
import org.example.repository.TaskRepository;

import java.io.IOException;
import java.util.*;

import static java.lang.System.in;

public class TaskCli {
    private final List<Task> tasks;
    private final TaskRepository taskRepository;
    private final Scanner scanner;

    public TaskCli(TaskRepository taskRepository) throws IOException {
        this.taskRepository = taskRepository;
        this.tasks = taskRepository.getTasks();
        this.scanner = new Scanner(in);
    }

    public void execute() throws IOException {
        printHelpMenu();
        String command;
        while(!(command = scanner.next()).equals("exit")) {
            try {
                switch (TaskCliAction.valueOfLabel(command)) {
                    case ADD: add(); break;
                    case DELETE: delete(); break;
                    case LIST: list(); break;
                    case UPDATE: update(); break;
                    case HELP: printHelpMenu(); break;
                    case MARK_IN_PROGRESS,
                         MARK_DONE: break;
                }
            } catch (InvalidTaskCliActionException
                     |TaskNotFoundException
                     |NumberFormatException  e) {
                System.out.println(e.getMessage());
                System.out.println("Type 'help' to see the list of available commands.");
                // just to flush the rest of the line we don't want
                scanner.nextLine();
            }
        }

        System.out.println("Tasks saving in progress...");
        taskRepository.saveTasks(tasks);
        System.out.println("Tasks saving done. See you later ;)");
    }

    private void printHelpMenu() {
        System.out.println("Please select one of the following options:");
        System.out.println("\t- add <description>: Add a new task");
        System.out.println("\t- delete <id>: Delete task with the given <id>");
        System.out.println("\t- list: List all tasks");
        System.out.println("\t- list <status>: List all tasks with the given <status>");
        System.out.println("\t- update <id> <description>: Update task <id> with the given <description>");
        System.out.println("\t- mark-in-progress <id>: Mark the task <id> as in progress");
        System.out.println("\t- mark-done <id>: Mark the task <id> as done");
        System.out.println("\t- help: Display this help menu");
        System.out.println("\t- exit: Exit the program");
    }

    private void update() throws TaskNotFoundException {
        String taskId = "";
        int id;

        try {
            taskId = scanner.next();
            id = Integer.parseInt(taskId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(String.format("'%s' is not a valid id.", taskId));
        }

        String description = scanner.nextLine();
        Task taskToUpdate = tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task with id '%d' not found", id)));
        taskToUpdate.setDescription(description.trim());
        System.out.printf("Task %d has been successfully updated%n", id);
    }

    private void delete() throws TaskNotFoundException {
        int id = scanner.nextInt();
        boolean isTaskRemoved = tasks.removeIf(task -> Objects.equals(task.getId(), id));
        if (isTaskRemoved) {
            System.out.printf("Task %d has been successfully removed", id);
        } else {
            throw new TaskNotFoundException(String.format("Task with id '%d' not found", id));
        }
    }

    private void list() {
        String status = scanner.nextLine();
        if (status.trim().isEmpty()) {
            tasks.forEach(System.out::println);
        } else {
            try {
                listByStatus(Task.Status.valueOfLabel(status.trim()));
            } catch (InvalidTaskStatusException e) {
                System.out.printf("Invalid task status: '%s'%n", status.trim());
                tasks.forEach(System.out::println);
            }
        }
    }

    private void listByStatus(Task.Status status) {
        tasks.stream()
                .filter(task -> status.equals(task.getStatus()))
                .forEach(System.out::println);
    }

    private void add() {
        String description = scanner.nextLine().trim();
        int lastId = 0;
        if (!tasks.isEmpty()) {
            lastId = tasks.get(tasks.size() -1).getId();
        }
        Task newTask = new Task(++lastId, description, Task.Status.TODO,
                new Date(), null);
        tasks.add(newTask);
        System.out.printf("Task added successfully (ID: %d)%n", lastId);
    }
}
