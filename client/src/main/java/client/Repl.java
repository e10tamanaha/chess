package client;

import java.util.Scanner;

public class Repl {
    private enum State {SIGNED_OUT, SIGNED_IN}

    private final ServerFacade facade;
    private final PreloginClient prelogin;
    private PostloginClient postlogin;
    private final Scanner scanner = new Scanner(System.in);
    private State state = State.SIGNED_OUT;

    public Repl(ServerFacade facade) {
        this.facade = facade;
        this.prelogin = new PreloginClient(facade);
    }

    public void run() {
        System.out.println("Welcome to 240 Chess. Type help to get started.");
        while (true) {
            printPrompt();
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().trim();

            if (state == State.SIGNED_OUT && line.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                break;
            }
            try {
                System.out.println(eval(line));
                updateState();
            } catch (ResponseException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Something went wrong. Please try again.");
            }
        }
    }

    private void printPrompt() {
        System.out.print(state == State.SIGNED_OUT ? "[LOGGED_OUT] >>> " : "[LOGGED_IN] >>> ");
    }

    private String eval(String line) throws ResponseException {
        if (state == State.SIGNED_OUT) {
            return prelogin.eval(line);
        }
        return postlogin.eval(line);
    }

    private void updateState() {
        if (state == State.SIGNED_OUT && prelogin.getAuth() != null) {
            postlogin = new PostloginClient(facade, prelogin.getAuth().authToken());
            state = State.SIGNED_IN;
        } else if (state == State.SIGNED_IN && postlogin.isLoggedOut()) {
            prelogin.clearAuth();
            postlogin = null;
            state = State.SIGNED_OUT;
        }
    }
}