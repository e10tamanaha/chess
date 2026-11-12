package client;

import model.AuthData;

public class PreloginClient {
    private final ServerFacade facade;
    private AuthData auth = null;

    public PreloginClient(ServerFacade facade) {
        this.facade = facade;
    }

    public AuthData getAuth() {
        return auth;
    }

    public String eval(String input) throws ResponseException {
        Command cmd = Command.parse(input);

        return switch (cmd.name()) {
            case "register" -> register(cmd.params());
            case "login" -> login(cmd.params());
            case "help" -> help();
            default -> "Unknown command. Type help to see available commands.";
        };
    }

    private String register(String[] params) throws ResponseException {
        if (params.length != 3) {
            return "Type in: register <USERNAME> <PASSWORD> <EMAIL>";
        }
        auth = facade.register(params[0], params[1], params[2]);
        return "Registered and logged in as " + auth.username() + ".";
    }

    private String login(String[] params) throws ResponseException {
        if (params.length != 2) {
            return "Type in: login <USERNAME> <PASSWORD>";
        }
        auth = facade.login(params[0], params[1]);
        return "Logged in as " + auth.username() + ".";
    }

    private String help() {
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - create an account
                login <USERNAME> <PASSWORD> - sign in
                quit - exit the program
                help - show available commands""";
    }

    public void clearAuth() {
        auth = null;
    }
}
