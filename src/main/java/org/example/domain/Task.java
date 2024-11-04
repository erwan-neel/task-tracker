package org.example.domain;

import org.example.exception.InvalidTaskStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {
    private final int id;
    private String description;
    private final Status status;
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
        return "{"
                + "\"id\":" + "\"" + id + "\""
                + ",\"description\":" + "\"" + description + "\""
                + ",\"status\":" + "\"" + status.label + "\""
                + ",\"createdAt\":" + "\"" + createdAt + "\""
                + ",\"updatedAt\":" + "\"" + updatedAt + "\""
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
                case "id": id = Integer.parseInt(value); break;
                case "description": description = value; break;
                case "status":
                    try {
                        status = Status.valueOfLabel(value);
                    } catch (InvalidTaskStatusException e) {
                        throw new IllegalArgumentException(e);
                    } break;
                case "createdAt":
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                        createdAt = formatter.parse(value); break;
                    } catch (ParseException e) {
                        createdAt = new Date();
                    } break;
                case "updatedAt":
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                        updatedAt = formatter.parse(value); break;
                    } catch (ParseException e) {
                        updatedAt = new Date();
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + key);
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
