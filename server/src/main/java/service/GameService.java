package service;

import dataaccess.*;
import java.util.Collection;
import model.GameData;
import model.AuthData;

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

    public void joinGame(String authToken, JoinGameRequest request) throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        AuthData authData = authenticator.authenticate(authToken);

        if (request == null || request.gameID() == null || !isValidColor(request.playerColor())) {
            throw new BadRequestException("Error: bad request");
        }

        GameData game = gameDAO.getGame(request.gameID());

        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }

        boolean isWhite = request.playerColor().equals("WHITE");
        String currentPlayer = isWhite ? game.whiteUsername() : game.blackUsername();

        if (currentPlayer != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        String username = authData.username();
        String whiteUsername = isWhite ? username : game.whiteUsername();
        String blackUsername = isWhite ? game.blackUsername() : username;

        GameData updated = new GameData(game.gameID(), whiteUsername, blackUsername, game.gameName(), game.game());
        gameDAO.updateGame(updated);
    }

    private boolean isValidColor(String color) {
        return "WHITE".equals(color) || "BLACK".equals(color);
    }
}