package service;

import dataaccess.*;
import model.GameData;
import java.util.UUID;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final Authenticator authenticator;

    public GameService(GameDAO gameDAO, AuthDAO authDAO, Authenticator authenticator) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.authenticator = authenticator;
    }
}