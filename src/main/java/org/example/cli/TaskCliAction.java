package org.example.cli;

public enum TaskCliAction {
    ADD("add"),
    UPDATE("update"),
    DELETE("delete"),
    MARK_IN_PROGRESS("mark-in-progress"),
    MARK_DONE("mark-done"),
    LIST("list");

    public final String label;

    TaskCliAction(String label) {
        this.label = label;
    }

    public static boolean contains(String action) {
        for (TaskCliAction taskCliAction : TaskCliAction.values()) {
            if (taskCliAction.label.equals(action)) {
                return true;
            }
        }

        return false;
    }

    public static TaskCliAction valueOfLabel(String label) {
        for (TaskCliAction taskCliAction : TaskCliAction.values()) {
            if (taskCliAction.label.equals(label)) {
                return taskCliAction;
            }
        }

        return null;
    }
}
