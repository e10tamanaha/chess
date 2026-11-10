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
}