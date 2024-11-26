package org.example.domain;

import org.example.exception.InvalidTaskStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    private final int id;
    private String description;
    private Status status;
    private final Date createdAt;
    private Date updatedAt;

    public Task(int id, String description, Status status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.description = setDescription(description);
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        update();
    }

    public String setDescription(String description) {
        this.description = description.replace("\"", "");
        update();

        return this.description;
    }

    private void update() {
        this.updatedAt = new Date();
    }

    @Override
    public String toString() {
        String updateValue = updatedAt == null ? "" : updatedAt.toString();

        return "{"
                + "\"id\":" + "\"" + id + "\""
                + ",\"description\":" + "\"" + description + "\""
                + ",\"status\":" + "\"" + status.label + "\""
                + ",\"createdAt\":" + "\"" + createdAt + "\""
                + ",\"updatedAt\":" + "\"" + updateValue + "\""
                + "}";
    }

    public static Task fromString(String task) {
        String[] keyValuePairs = task.replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .split(",");

        int id = 0;
        String description = null;
        Status status = null;
        Date createdAt = null;
        Date updatedAt = null;

        for (String keyValuePair : keyValuePairs) {
            String key = keyValuePair.substring(0, keyValuePair.indexOf(":")).trim();
            String value = keyValuePair.substring(keyValuePair.indexOf(":") + 1).trim();
            switch (key) {
                case "id" -> id = Integer.parseInt(value);
                case "description" -> description = value;
                case "status" -> {
                    try {
                        status = Status.valueOfLabel(value);
                    } catch (InvalidTaskStatusException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
                case "createdAt" -> {
                    try {
                        createdAt = DATE_FORMATTER.parse(value);
                    } catch (ParseException e) {
                        createdAt = new Date();
                    }
                }
                case "updatedAt" -> {
                    if (!value.isEmpty()) {
                        try {
                            updatedAt = DATE_FORMATTER.parse(value);
                        } catch (ParseException e) {
                            updatedAt = new Date();
                        }
                    }
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + key);
            }
        }

        return new Task(id, description, status, createdAt, updatedAt);
    }

    public enum Status {
        TODO("todo"),
        IN_PROGRESS("in-progress"),
        DONE("done");

        public final String label;

        Status(String label) {
            this.label = label;
        }

        public static Status valueOfLabel(String label) throws InvalidTaskStatusException {
            for (Status status : Status.values()) {
                if (status.label.equals(label)) {
                    return status;
                }
            }

            throw new InvalidTaskStatusException(String.format("'%s' is not a valid task status.", label));
        }
    }
}
