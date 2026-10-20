package service;

import dataaccess.*;

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
}