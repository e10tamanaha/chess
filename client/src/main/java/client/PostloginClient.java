package client;

import model.GameData;
import java.util.List;

public class PostloginClient {
    private final ServerFacade facade;
    private String authToken;
    private List<GameData> games = List.of();

    public PreloginClient(ServerFacade facade, String authToken) {
        this.facade = facade;
        this.authToken = authToken;
    }

    public String eval(String input) throws ResponseException {
        
    }
}