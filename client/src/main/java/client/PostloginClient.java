package client;

import model.GameData;
import java.util.List;

public class PostloginClient {
    private final ServerFacade facade;
    private final String authToken;
    private boolean loggedOut = false;
    private List<GameData> games = List.of();

    public PostloginClient(ServerFacade facade, String authToken) {
        this.facade = facade;
        this.authToken = authToken;
    }

    public String eval(String input) throws ResponseException {
        Command cmd = Command.parse(input);
        return switch(cmd.name()) {
            case "create" -> create(cmd.params());
            case "logout" -> logout();
            case "help" -> help();
            default -> "Unknown command. Type help to see available commands.";
        };
    }

    private String create(String[] params) throws ResponseException {
        if (params.length == 0) {
            return "Type in: create <NAME>";
        }

        String name = String.join(" ", params);
        facade.createGame(authToken, name);
        return "Game: " + name + " was created successfully";
    }

    public boolean isLoggedOut() {
        return loggedOut;
    }

    private String logout() throws ResponseException {
        facade.logout(authToken);
        loggedOut = true;
        return "Logged out. Type help to see available commands.";
    }

    private String help() {
        return """
                create <NAME> - create a new game
                list - list all games
                play <NUMBER> <WHITE|BLACK> - join a game as a color
                observe <NUMBER> - watch a game
                logout - sign out
                help - show available commands""";
    }
}