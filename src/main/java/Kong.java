import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Kong {
    public static void main(String[] args) {
        String banner = " _  __                 \n"
                + "| |/ /___  _ __   __ _ \n"
                + "| ' // _ \\| '_ \\ / _` |\n"
                + "| . \\ (_) | | | | (_| |\n"
                + "|_|\\_\\___/|_| |_|\\__, |\n"
                + "                 |___/ \n";

        String line = "________________________________________";
        String out = banner + line + "\n" + "Hello, I'm Kong.\nWhat can I do for you?\n" + line;
        System.out.println(out);

        ArrayList<Task> lst =  new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            String[] parts = input.split("\\s+", 2);
            String command = parts[0];
            String arg = parts[1];

            if (command.equals("todo")) {
                lst.add(new ToDo(arg));
            } else if (command.equals("deadline")) {
                Pattern pattern = Pattern.compile("^(.*?)\\s*/by\\s+(.*)$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(arg);

                if (matcher.find()) {
                    String desc = matcher.group(1);
                    String by = matcher.group(2);
                    lst.add(new Deadline(desc, by));
                } else {
                    System.out.println("Invalid command");
                }
            } else if (command.equals("event")) {
                Pattern pattern = Pattern.compile("^(.*?)\\s*/from\\s+(.*?)\\s*/to\\s+(.*)$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(arg);

                if (matcher.find()) {
                    String desc = matcher.group(1);
                    String from = matcher.group(2);
                    String to = matcher.group(3);
                    lst.add(new Event(desc, from, to));
                } else {
                    System.out.println("Invalid Command");
                }
            } else if (command.equals("list")) {
                System.out.println("Here are the tasks in your list.");
                for (int i = 0; i < lst.size(); i ++) {
                    System.out.println(String.format("%d. %s", i, lst.get(i).toString()));
                }
            } else if (command.equals("mark")) {
                lst.get(Integer.parseInt(arg) - 1).mark();
            } else if (command.equals("unmark")) {
                lst.get(Integer.parseInt(arg) - 1).unmark();
            } else if (command.equals("bye")) {
                System.out.println("BYEBYE!");
                break;
            } else {
                System.out.println("Please enter a valid command.");
            }
        }
    }
}
