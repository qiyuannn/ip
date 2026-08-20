import java.util.Scanner;

public class Kong {
    public static void main(String[] args) {
        String banner = " _  __                 \n"
                + "| |/ /___  _ __   __ _ \n"
                + "| ' // _ \\| '_ \\ / _` |\n"
                + "| . \\ (_) | | | | (_| |\n"
                + "|_|\\_\\___/|_| |_|\\__, |\n"
                + "                 |___/ \n";

        String line = "________________________________________\n";
        String out = banner + line + "Hello, I'm Kong.\nWhat can I do for you?\n" + line;
        System.out.println(out);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println(line + "Bye. Hope to see you again soon!");
                break;
            }
            System.out.println(line + input + "\n" +lineh);
        }
        scanner.close();
    }
}
