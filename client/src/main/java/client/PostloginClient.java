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
            case "list" -> list();
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

    private String playerName(String username) {
        return username == null ? "-" : username;
    }

    private String list() throws ResponseException {
        games = facade.listGames(authToken);

        if (games.isEmpty()) {
            return "No games yet. Type create <NAME> to make one.";
        }

        String list = "";
        for (int i = 1; i < games.size() + 1; i++) {
            list += i + ". " + games.get(i - 1).gameName() + " white: " + 
                playerName(games.get(i - 1).whiteUsername()) + " black: " + 
                playerName(games.get(i - 1).blackUsername()) + "\n";
        }

        return list.trim();
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