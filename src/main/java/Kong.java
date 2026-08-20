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

        ArrayList<String> lst =  new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            System.out.println(line);

            switch (input) {
                case "":
                    System.out.println("Please enter a valid command.");
                    System.out.println(line);
                    continue;
                case "bye":
                    System.out.println("Bye. Hope to see you again soon!");
                    scanner.close();
                    return;
                case "list":
                    for (int i = 0; i < lst.size(); i++) {
                        System.out.println(String.format("%d. %s", i + 1, lst.get(i)));
                    }
                    System.out.println(line);
                    continue;
                default:
                    lst.add(input);
                    System.out.println("added: " + input);
                    System.out.println(line);
            }
        }
    }
}
