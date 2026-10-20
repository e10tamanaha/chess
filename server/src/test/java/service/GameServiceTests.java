package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private Authenticator authenticator;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        authenticator = new Authenticator(authDAO);
        gameService = new GameService(gameDAO, authenticator);
    }

    @Test
    void createGameSucceeds() throws Exception {
        String tokenName = "validToken";
        String gameName = "My Game";
        authDAO.createAuth(new AuthData(tokenName, "Ethan"));

        CreateGameResult result = gameService.createGame(tokenName, new CreateGameRequest(gameName));
        int gameID = result.gameID();
        assertTrue(result.gameID() > 0);
        
        GameData game = gameDAO.getGame(result.gameID());
        assertNotNull(game);
        assertEquals(game.gameName(), gameName);
        assertNull(game.whiteUsername());
        assertNull(game.blackUsername());
        assertNotNull(game.game());
    }

    @Test
    void createGameBadToken() throws Exception {
        assertThrows(UnauthorizedException.class, () -> gameService.createGame("invalidToken", new CreateGameRequest("My Game")));
    }

    @Test
    void createGameBadRequest() throws Exception {
        authDAO.createAuth(new AuthData("validToken", "Ethan"));
        assertThrows(BadRequestException.class, () -> gameService.createGame("validToken", new CreateGameRequest(null)));
    }
}