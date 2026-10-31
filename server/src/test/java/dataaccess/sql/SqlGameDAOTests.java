package dataaccess.sql;

import dataaccess.DataAccessException;
import java.util.Collection;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SqlGameDAOTests {
    private SqlGameDAO gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        gameDAO = new SqlGameDAO();
        gameDAO.clear();
    }

    @Test
    void createGameAndGetGamePositive() throws DataAccessException {
        String gameName1 = "game1";
        String gameName2 = "game2";
        int gameId1 = gameDAO.createGame(gameName1);
        int gameId2 = gameDAO.createGame(gameName2);
        assertNotEquals(gameId1, gameId2);

        GameData game1 = gameDAO.getGame(gameId1);
        assertNotNull(game1);
        assertEquals(gameName1, game1.gameName());
        assertNull(game1.whiteUsername());
        assertNull(game1.blackUsername());
        assertEquals(new ChessGame(), game1.game());

        GameData game2 = gameDAO.getGame(gameId2);
        assertNotNull(game2);
        assertEquals(gameName2, game2.gameName());
        assertNull(game2.whiteUsername());
        assertNull(game2.blackUsername());
        assertEquals(new ChessGame(), game2.game());
    }

    @Test
    void createGameNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
        assertTrue(gameDAO.listGames().isEmpty());
    }

    @Test
    void getGameNegative() throws DataAccessException {
        assertNull(gameDAO.getGame(999));
    }

    @Test
    void listGamesPositive() throws DataAccessException {
        String gameName1 = "game1";
        String gameName2 = "game2";
        String gameName3 = "game3";
        int gameId1 = gameDAO.createGame(gameName1);
        int gameId2 = gameDAO.createGame(gameName2);
        int gameId3 = gameDAO.createGame(gameName3);
        Collection<GameData> games = gameDAO.listGames();
        assertEquals(3, games.size());
        assertTrue(games.contains(gameDAO.getGame(gameId1)));
        assertTrue(games.contains(gameDAO.getGame(gameId2)));
        assertTrue(games.contains(gameDAO.getGame(gameId3)));
    }

    @Test
    void listGamesEmptyNegative() throws DataAccessException {
        Collection<GameData> games = gameDAO.listGames();
        assertNotNull(games);
        assertTrue(games.isEmpty());
    }

    @Test
    void updateGamePositive() throws DataAccessException, InvalidMoveException {
        String gameName = "game";
        int gameId = gameDAO.createGame(gameName);
        GameData gameData = gameDAO.getGame(gameId);
        ChessGame chessGame = gameData.game();
        chessGame.makeMove(new ChessMove(new ChessPosition(2, 5), new ChessPosition(4, 5), null));
        GameData newGame = new GameData(gameId, "kong", "godzilla", "godzillaVKong", chessGame);
        gameDAO.updateGame(newGame);
        GameData newGameRetrieved = gameDAO.getGame(gameId);
        assertNotNull(newGameRetrieved);
        assertEquals("kong", newGameRetrieved.whiteUsername());
        assertEquals("godzilla", newGameRetrieved.blackUsername());
        assertEquals("godzillaVKong", newGameRetrieved.gameName());
        assertEquals(newGame.game(), newGameRetrieved.game());
        assertEquals(ChessGame.TeamColor.BLACK, newGameRetrieved.game().getTeamTurn());
    }

    @Test
    void updateGameNegative() throws DataAccessException {
        GameData missing = new GameData(999, "kong", "godzilla", "ghostGame", new ChessGame());
        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(missing));
        assertTrue(gameDAO.listGames().isEmpty());
    }

    @Test
    void clearPositive() throws DataAccessException {
        gameDAO.createGame("game1");
        gameDAO.createGame("game2");
        gameDAO.createGame("game3");
        gameDAO.clear();
        assertTrue(gameDAO.listGames().isEmpty());
    }
}