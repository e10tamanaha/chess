package client;

import model.GameData;
import java.util.List;

public class PostloginClient {
    private final ServerFacade facade;
    private String authToken;
    private List<GameData> games = List.of();

    public PostloginClient(ServerFacade facade, String authToken) {
        this.facade = facade;
        this.authToken = authToken;
    }

    public String eval(String input) throws ResponseException {
        Command cmd = Command.parse(input);
        return switch(cmd.name()) {
            case "help" -> help();
            case "logout" -> logout(cmd.params());
            default -> "Unknown command. Type help to see available commands.";
        };
    }

    private String help() {
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - create an account
                login <USERNAME> <PASSWORD> - sign in
                quit - exit the program
                help - show available commands""";
    }
}