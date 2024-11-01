import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        String[] words = line.split(" ");

        // on vérifie le nom de la cli
        if (!"task-cli".equals(words[0])) {
            System.out.println("\"" + words[0] + "\" is not a known cli. Consider using \"task-cli\" instead.");
            return;
        }

        // on vérifie que l'action existe
        // pourrait être remplacé par le default du switch
        if (!TaskCliAction.contains(words[1])) {
            System.out.println("\"" + words[1] + "\" is not a known action.");
            System.out.println("Please consider using one of the following instead :");

            for (TaskCliAction action : TaskCliAction.values()) {
                System.out.println("    - " + action.label);
            }

            return;
        }

        JsonTaskRepository jsonTaskRepository = new JsonTaskRepository("tasks.json");
        TaskCli cli = new TaskCli(jsonTaskRepository);

        switch (TaskCliAction.valueOfLabel(words[1])) {
            case ADD:
                cli.add(words[2]);
                System.out.println("\"" + words[2] + "\" is added.");
                return;
            case DELETE:
            case UPDATE:
                System.out.println("\"" + words[2] + "\" is updated.");
                return;
        }

        System.out.println(words[2]);
    }
}
