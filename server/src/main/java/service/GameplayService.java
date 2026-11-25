package service;

import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;

public class GameplayService {
    private final GameDAO gameDAO;
    private final Authenticator authenticator;

    public GameplayService(GameDAO gameDAO, Authenticator authenticator) {
        this.gameDAO = gameDAO;
        this.authenticator = authenticator;
    }

    public GameplayContext connect(String authToken, Integer gameID) throws UnauthorizedException, BadRequestException, DataAccessException {
        return loadContext(authToken, gameID);
    }

    private GameplayContext loadContext(String authToken, Integer gameID) throws UnauthorizedException, BadRequestException, DataAccessException {
        AuthData auth = authenticator.authenticate(authToken);

        if (gameID == null) {
            throw new BadRequestException("Error: bad request");
        }

        GameData gameData = gameDAO.getGame(gameID);

        if (gameData == null) {
            throw new BadRequestException("Error: bad request");
        }

        String username = auth.username();
        PlayerRole role = roleFor(username, gameData);

        return new GameplayContext(username, gameData, role);
    }

    private PlayerRole roleFor(String username, GameData gameData) {
        if (username.equals(gameData.whiteUsername())) {
            return PlayerRole.WHITE;
        } else if (username.equals(gameData.blackUsername())) {
            return PlayerRole.BLACK;
        } else {
            return PlayerRole.OBSERVER;
        }
    }
}