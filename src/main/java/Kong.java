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
        String out = banner + line + "\n" + "Hello, I'm Kong.\nWhat can I do for you?";
        System.out.println(out);

        ArrayList<Task> lst =  new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(line);
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    throw new KongException("Please enter a command.");
                }
                String[] parts = input.split("\\s+", 2);
                String command = parts[0];
                String arg = parts.length > 1 ? parts[1] : "";

                if (command.equals("todo")) {
                    if (arg.isEmpty()) {
                        throw new KongException("Invalid command. A todo command needs to be in the following format: todo <description>");
                    }
                    lst.add(new ToDo(arg));
                } else if (command.equals("deadline")) {
                    Pattern pattern = Pattern.compile("^(.*?)\\s*/by\\s+(.*)$", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(arg);
                    if (matcher.find()) {
                        String desc = matcher.group(1);
                        String by = matcher.group(2);
                        if (desc.isEmpty() || by.isEmpty()) {
                            throw new KongException("Invalid command. A deadline command needs to be in the following format: deadline <description> /by <date>");
                        }
                        lst.add(new Deadline(desc, by));
                    } else {
                        throw new KongException("Invalid command. A deadline command needs to be in the following format: deadline <description> /by <date>");
                    }
                } else if (command.equals("event")) {
                    Pattern pattern = Pattern.compile("^(.*?)\\s*/from\\s+(.*?)\\s*/to\\s+(.*)$", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(arg);
                    if (matcher.find()) {
                        String desc = matcher.group(1);
                        String from = matcher.group(2);
                        String to = matcher.group(3);
                        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                            throw new KongException("Invalid command. An event command needs to be in the following format: event <description> /from <date> /to <date>");
                        }
                        lst.add(new Event(desc, from, to));
                    } else {
                        throw new KongException("Invalid command. An event command needs to be in the following format: event <description> /from <date> /to <date>");
                    }
                } else if (command.equals("list")) {
                    System.out.println("Here are the tasks in your list.");
                    for (int i = 0; i < lst.size(); i++) {
                        System.out.println(String.format("%d. %s", i + 1 , lst.get(i).toString()));
                    }
                } else if (command.equals("mark")) {
                    try {
                        lst.get(Integer.parseInt(arg) - 1).mark();
                    } catch (NumberFormatException e) {
                        throw new KongException("Invalid command. A mark command needs to be followed by a number.");
                    } catch (IndexOutOfBoundsException e) {
                        throw new KongException(String.format("This task number is invalid. You currently have %d tasks in your list.", lst.size()));
                    }
                } else if (command.equals("unmark")) {
                    try {
                        lst.get(Integer.parseInt(arg) - 1).unmark();
                    } catch (NumberFormatException e) {
                        throw new KongException("Invalid command. A unmark command needs to be followed by a number.");
                    } catch (IndexOutOfBoundsException e) {
                        throw new KongException(String.format("This task number is invalid. You currently have %d tasks in your list.", lst.size()));
                    }
                } else if (command.equals("bye")) {
                    System.out.println("BYEBYE!");
                    break;
                } else {
                    throw new KongException("Sorry we do not recognise that command yet.");
                }
            } catch (KongException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
