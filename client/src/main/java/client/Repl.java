package client;

import java.util.Scanner;

public class Repl {
    private enum State {SIGNED_OUT, SIGNED_IN}

    private final ServerFacade facade;
    private final Scanner scanner = new Scanner(System.in);
    private State state = State.SIGNED_OUT;

    public Repl(ServerFacade facade) {
        this.facade = facade;
    }

    public void run() {
        System.out.println("Welcome to 240 Chess. Type help to get started.");
        while (true) {
            printPrompt();
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                break;
            }
            try {
                System.out.println(eval(line));
            } catch (Exception e) {
                System.out.println("Something went wrong. Please try again.");
            }
        }
    }

    private void printPrompt() {
        System.out.print(state == State.SIGNED_OUT ? "[LOGGED_OUT] >>> " : "[LOGGED_IN] >>> ");
    }

    private String eval(String line) {
        if (line.equalsIgnoreCase("help")) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - create an account
                    login <USERNAME> <PASSWORD> - sign in
                    quit - exit the program
                    help - show available commands""";
        }
        return "Unknown command. Type help to see available commands.";
    }
}