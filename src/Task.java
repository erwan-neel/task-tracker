import java.util.Date;

public class Task {
    private int id;
    private String description;
    private Status status;
    private Date createdAt;
    private Date updatedAt;

    public Task(int id, String description, Status status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    enum Status {
        TODO("todo"),
        IN_PROGRESS("in-progress"),
        DONE("done");

        public final String label;

        private Status(String label) {
            this.label = label;
        }

        public static Status valueOfLabel(String label) {
            for (Status status : Status.values()) {
                if (status.label.equals(label)) {
                    return status;
                }
            }
            return null;
        }
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
                case "status": status = Status.valueOfLabel(value); break;
                case "created_at": createdAt = new Date(value); break;
                case "updated_at": updatedAt = new Date(value); break;
            }
        }

        return new Task(id, description, status, createdAt, updatedAt);
    }
}
