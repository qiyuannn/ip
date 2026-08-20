import java.util.Scanner;
import java.util.ArrayList;

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
            System.out.println(line);

            if (input.isEmpty()) {
                System.out.println("Please enter a valid command.");
                System.out.println(line);
            } else if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                scanner.close();
                return;
            } else if (input.equals("list")) {
                System.out.println("Here are the tasts in your list.");
                for (int i = 0; i < lst.size(); i++) {
                    System.out.println(String.format("%d. %s", i + 1, lst.get(i).toString()));
                }
                System.out.println(line);
            } else if (input.startsWith("mark")) {
                String[] parts = input.split("\\s+", 2);
                if (parts.length < 2) {
                    System.out.println("Please enter a valid task number.");
                } else {
                    try {
                        int ix = Integer.parseInt(parts[1]) - 1;
                        if (ix < 0 || ix >= lst.size()) {
                            System.out.println("Task number out of range. Please enter a number between 1 and " + lst.size() + ".");
                        } else {
                            System.out.println(lst.get(ix).mark());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid task number.");
                    }
                }
                System.out.println(line);
            } else if (input.startsWith("unmark")) {
                String[] parts = input.split("\\s+", 2);
                if (parts.length < 2) {
                    System.out.println("Please enter a valid task number.");
                } else {
                    try {
                        int ix = Integer.parseInt(parts[1]) - 1;
                        if (ix < 0 || ix >= lst.size()) {
                            System.out.println("Task number out of range. You currently have " + lst.size() + " items in your list.");
                        } else {
                            System.out.println(lst.get(ix).unmark());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid task number.");
                    }
                }
                System.out.println(line);
            } else {
                lst.add(new Task(input));
                System.out.println("added: " + input);
                System.out.println(line);
            }
        }
    }
}
