package service;

import dataaccess.*;
import java.util.Collection;
import model.GameData;

public class GameService {
    private final GameDAO gameDAO;
    private final Authenticator authenticator;

    public GameService(GameDAO gameDAO, Authenticator authenticator) {
        this.gameDAO = gameDAO;
        this.authenticator = authenticator;
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request) throws UnauthorizedException, BadRequestException, DataAccessException {
        authenticator.authenticate(authToken);

        if (request == null || request.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }

        int gameID = gameDAO.createGame(request.gameName());
        return new CreateGameResult(gameID);
    }

    public ListGamesResult listGames(String authToken) throws UnauthorizedException, DataAccessException {
        authenticator.authenticate(authToken);
        Collection<GameData> games = gameDAO.listGames();
        return new ListGamesResult(games);
    }
}