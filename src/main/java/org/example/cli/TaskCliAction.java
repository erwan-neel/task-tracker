package org.example.cli;

import org.example.exception.InvalidTaskCliActionException;

public enum TaskCliAction {
    ADD("add"),
    UPDATE("update"),
    DELETE("delete"),
    MARK_IN_PROGRESS("mark-in-progress"),
    MARK_DONE("mark-done"),
    LIST("list"),
    HELP("help");

    public final String label;

    TaskCliAction(String label) {
        this.label = label;
    }

    public static TaskCliAction valueOfLabel(String label) throws InvalidTaskCliActionException {
        for (TaskCliAction taskCliAction : TaskCliAction.values()) {
            if (taskCliAction.label.equals(label)) {
                return taskCliAction;
            }
        }

        throw new InvalidTaskCliActionException(String.format("'%s' is not a valid action.", label));
    }
}
