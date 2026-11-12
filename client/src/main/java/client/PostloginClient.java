package client;

import chess.ChessGame;
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
            case "play" -> play(cmd.params());
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

    private String play(String[] params) throws ResponseException {
        if (params.length != 2) {
            return "Type in: play <NUMBER> <WHITE|BLACK>";
        }
       
        if (games.isEmpty()) {
            return "Type list to see the games first";
        }

        int number;
        try {
            number = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            return "Game number must be a number";
        }

        if (number < 1 || number > games.size()) {
            return "No game with that number. Type list to see the games.";
        }

        ChessGame.TeamColor color = switch (params[1].toLowerCase()) {
            case "white" -> ChessGame.TeamColor.WHITE;
            case "black" -> ChessGame.TeamColor.BLACK;
            default -> null;
        };
        if (color == null) {
            return "Color must be WHITE or BLACK";
        }

        GameData game = games.get(number - 1);
        facade.joinGame(authToken, game.gameID(), color);
        return "Joined " + game.gameName() + " as " + params[1].toLowerCase() + ".";
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