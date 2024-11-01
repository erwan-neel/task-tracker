import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InvalidTaskCliActionException {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        String[] words = line.split(" ");

        if (!"task-cli".equals(words[0])) {
            System.out.println("\"" + words[0] + "\" is not a known cli. Consider using \"task-cli\" instead.");
            return;
        }

        JsonTaskRepository jsonTaskRepository = new JsonTaskRepository("tasks.json");
        TaskCli cli = new TaskCli(jsonTaskRepository);
        String outputMsg = cli.execute(Arrays.copyOfRange(words, 1, words.length));

        System.out.println(outputMsg);
    }
}
