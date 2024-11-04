package org.example;

import org.example.cli.TaskCli;
import org.example.exception.InvalidTaskCliActionException;
import org.example.repository.JsonTaskRepository;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InvalidTaskCliActionException {
        JsonTaskRepository jsonTaskRepository = new JsonTaskRepository("tasks.json");
        TaskCli cli = new TaskCli(jsonTaskRepository);
        cli.execute();
    }
}
