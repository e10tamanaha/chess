package service;

import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;

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

    public MoveResult makeMove(String authToken, Integer gameID, ChessMove move) throws UnauthorizedException, BadRequestException, DataAccessException {
        GameplayContext context = loadContext(authToken, gameID);
        PlayerRole role = context.role();

        if (move == null) {
            throw new BadRequestException("Error: bad request");
        }

        if (role == PlayerRole.OBSERVER) {
            throw new BadRequestException("Error: observers cannot make moves");
        }

        ChessGame game = context.gameData().game();

        if (game.isGameOver()) {
            throw new BadRequestException("Error: the game is over");
        }

        if (role.teamColor() != game.getTeamTurn()) {
            throw new BadRequestException("Error: it is not your turn");
        }

        try {
            game.makeMove(move);
        } catch (InvalidMoveException exception) {
            throw new BadRequestException("Error: invalid move", exception);
        }

        GameStatus status = getStatus(game);

        if (status == GameStatus.CHECKMATE || status == GameStatus.STALEMATE) {
            game.setGameOver(true);
        }

        gameDAO.updateGame(context.gameData());
        return new MoveResult(context, status);
    }

    public GameplayContext resign(String authToken, Integer gameID) throws UnauthorizedException, BadRequestException, DataAccessException {
        GameplayContext context = loadContext(authToken, gameID);

        if (context.role() == PlayerRole.OBSERVER) {
            throw new BadRequestException("Error: observers cannot resign");
        }

        ChessGame game = context.gameData().game();

        if (game.isGameOver()) {
            throw new BadRequestException("Error: the game is over");
        }

        game.setGameOver(true);
        gameDAO.updateGame(context.gameData());

        return context;
    }

    public GameplayContext leave(String authToken, Integer gameID) throws UnauthorizedException, BadRequestException, DataAccessException {
        GameplayContext context = loadContext(authToken, gameID);
        GameData gameData = context.gameData();

        if (context.role() == PlayerRole.WHITE) {
            GameData updated = new GameData(
                gameData.gameID(),
                null,
                gameData.blackUsername(),
                gameData.gameName(),
                gameData.game()
            );
            gameDAO.updateGame(updated);
        } else if (context.role() == PlayerRole.BLACK) {
            GameData updated = new GameData(
                gameData.gameID(),
                gameData.whiteUsername(),
                null,
                gameData.gameName(),
                gameData.game()
            );
            gameDAO.updateGame(updated);
        }

        return context;
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

    private GameStatus getStatus(ChessGame game) {
        ChessGame.TeamColor turnColor = game.getTeamTurn();

        if (game.isInCheckmate(turnColor)) {
            return GameStatus.CHECKMATE;
        } else if (game.isInStalemate(turnColor)) {
            return GameStatus.STALEMATE;
        } else if (game.isInCheck(turnColor)) {
            return GameStatus.CHECK;
        } else {
            return GameStatus.NONE;
        }
    }
}