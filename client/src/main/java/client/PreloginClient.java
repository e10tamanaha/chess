package client;

import model.AuthData;

public class PreloginClient {
    private ServerFacade facade;
    private AuthData auth = null;

    public PreloginClient(ServerFacade facade) {
        this.facade = facade;
    }

    public String eval(String input) throws ResponseException {
        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        return switch (command) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "help" -> help();
            default -> "Unknown command. Type help to see available commands.";
        };
    }
}