package service;

import java.util.List;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    private static final String TOKEN = "validToken";

    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private Authenticator authenticator;
    private GameService gameService;

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        authenticator = new Authenticator(authDAO);
        gameService = new GameService(gameDAO, authenticator);
        authDAO.createAuth(new AuthData(TOKEN, "Ethan"));
    }

    private CreateGameResult createGame(String tokenName, String gameName) throws UnauthorizedException, BadRequestException, DataAccessException {
        return gameService.createGame(tokenName, new CreateGameRequest(gameName));
    }

    private void joinGame(String tokenName, String playerColor, Integer gameID) throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        gameService.joinGame(tokenName, new JoinGameRequest(playerColor, gameID));
    }

    @Test
    void createGameSucceeds() throws Exception {
        String gameName = "My Game";

        CreateGameResult result = createGame(TOKEN, gameName);
        int gameID = result.gameID();
        assertTrue(result.gameID() > 0);

        GameData game = gameDAO.getGame(result.gameID());
        assertNotNull(game);
        assertEquals(gameName, game.gameName());
        assertNull(game.whiteUsername());
        assertNull(game.blackUsername());
        assertNotNull(game.game());
    }

    @Test
    void createGameBadToken() throws Exception {
        assertThrows(UnauthorizedException.class, () -> createGame("invalidToken", "My Game"));
    }

    @Test
    void createGameBadRequest() throws Exception {
        assertThrows(BadRequestException.class, () -> createGame(TOKEN, null));
    }

    @Test
    void listGamesSucceeds() throws Exception {
        createGame(TOKEN, "My Game");
        createGame(TOKEN, "My Game2");
        ListGamesResult games = gameService.listGames(TOKEN);
        assertEquals(2, games.games().size());
        List<String> names = games.games().stream().map(GameData::gameName).toList();
        assertTrue(names.contains("My Game"));
        assertTrue(names.contains("My Game2"));
    }

    @Test
    void listGamesEmpty() throws Exception {
        ListGamesResult games = gameService.listGames(TOKEN);
        assertNotNull(games.games());
        assertEquals(0, games.games().size());
    }

    @Test
    void listGamesBadToken() throws Exception {
        assertThrows(UnauthorizedException.class, () -> gameService.listGames("invalidToken"));
    }

    @Test
    void joinGameSucceeds() throws Exception {
        int gameID = createGame(TOKEN, "My Game").gameID();
        joinGame(TOKEN, "WHITE", gameID);

        GameData game = gameDAO.getGame(gameID);
        assertEquals("Ethan", game.whiteUsername());
        assertNull(game.blackUsername());
        assertEquals("My Game", game.gameName());
    }

    @Test
    void joinGameAsBlackSucceeds() throws Exception {
        authDAO.createAuth(new AuthData("otherToken", "Sam"));
        int gameID = createGame(TOKEN, "My Game").gameID();
        joinGame("otherToken", "WHITE", gameID);
        joinGame(TOKEN, "BLACK", gameID);

        GameData game = gameDAO.getGame(gameID);
        assertEquals("Sam", game.whiteUsername());
        assertEquals("Ethan", game.blackUsername());
    }

    @Test
    void joinGameBadToken() throws Exception {
        int gameID = createGame(TOKEN, "My Game").gameID();
        assertThrows(UnauthorizedException.class, () -> joinGame("invalidToken", "WHITE", gameID));
    }

    @Test
    void joinGameBadColor() throws Exception {
        int gameID = createGame(TOKEN, "My Game").gameID();
        assertThrows(BadRequestException.class, () -> joinGame(TOKEN, "GREEN", gameID));
    }

    @Test
    void joinGameMissingID() {
        assertThrows(BadRequestException.class, () -> joinGame(TOKEN, "WHITE", null));
    }

    @Test
    void joinGameNonexistentGame() {
        assertThrows(BadRequestException.class, () -> joinGame(TOKEN, "WHITE", 999));
    }

    @Test
    void joinGameSeatTaken() throws Exception {
        authDAO.createAuth(new AuthData("otherToken", "Sam"));
        int gameID = createGame(TOKEN, "My Game").gameID();
        joinGame("otherToken", "WHITE", gameID);

        assertThrows(AlreadyTakenException.class, () -> joinGame(TOKEN, "WHITE", gameID));
        assertEquals("Sam", gameDAO.getGame(gameID).whiteUsername());
    }
}
