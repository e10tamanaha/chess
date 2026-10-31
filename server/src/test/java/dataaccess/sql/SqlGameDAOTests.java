package dataaccess.sql;

import dataaccess.DataAccessException;
import chess.ChessGame;
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
}