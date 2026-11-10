package client;

import org.junit.jupiter.api.*;
import server.Server;
import model.AuthData;
import model.GameData;
import chess.ChessGame;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void clearDatabase() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerPositive() throws ResponseException {
        AuthData result = facade.register("player1", "password", "p1@email.com");
        assertNotNull(result);
        assertTrue(result.authToken().length() > 10);
        assertEquals("player1", result.username());
    }

    @Test
    public void registerDuplicateNegative() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        assertThrows(ResponseException.class, () -> facade.register("player1", "password2", "p1@hotmail.com"));
    }

    @Test
    public void loginPositive() throws ResponseException {
        AuthData registerResult = facade.register("player1", "password", "p1@email.com");
        AuthData loginResult = facade.login("player1", "password");
        assertNotNull(loginResult);
        assertEquals("player1", loginResult.username());
        assertNotEquals(registerResult.authToken(), loginResult.authToken());
    }

    @Test
    public void loginNegative() throws ResponseException {
        facade.register("player1", "password", "p1@email.com");
        assertThrows(ResponseException.class, () -> facade.login("player1", "wrongpassword"));
    }

    @Test
    public void logoutPositive() throws ResponseException {
        AuthData result = facade.register("player1", "password", "p1@email.com");
        assertDoesNotThrow(() -> facade.logout(result.authToken()));
        assertThrows(ResponseException.class, () -> facade.logout(result.authToken()));
    }

    @Test
    public void logoutNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> facade.logout("fake-token"));
    }

    @Test
    public void createGamePositive() throws ResponseException {
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        int gameID1 = facade.createGame(auth.authToken(), "game1");
        int gameID2 = facade.createGame(auth.authToken(), "game2");
        assertTrue(gameID1 > 0);
        assertNotEquals(gameID1, gameID2);
    }

    @Test
    public void createGameNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> facade.createGame("fake-token", "game1"));
    }

    @Test
    public void listGamesPositive() throws ResponseException {
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        int gameID1 = facade.createGame(auth.authToken(), "game1");
        int gameID2 = facade.createGame(auth.authToken(), "game2");
        List<GameData> gamesList = facade.listGames(auth.authToken());
        assertEquals(2, gamesList.size());
        assertEquals(gameID1, gamesList.get(0).gameID());
        assertEquals("game1", gamesList.get(0).gameName());
        assertEquals(gameID2, gamesList.get(1).gameID());
        assertEquals("game2", gamesList.get(1).gameName());
    }

    @Test
    public void listGamesNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> facade.listGames("fake_token"));
    }

    @Test
    public void joinGamesPositive() throws ResponseException {
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        int gameID = facade.createGame(auth.authToken(), "game1");

        assertDoesNotThrow(() -> facade.joinGame(auth.authToken(), gameID, ChessGame.TeamColor.WHITE));

        GameData game = facade.listGames(auth.authToken()).get(0);
        assertEquals("player1", game.whiteUsername());
        assertNull(game.blackUsername());
    }

    @Test
    public void joinGamesNegative() throws ResponseException {
        AuthData auth1 = facade.register("player1", "password", "p1@email.com");
        AuthData auth2 = facade.register("player2", "password", "p2@email.com");
        int gameID = facade.createGame(auth1.authToken(), "game1");
        facade.joinGame(auth1.authToken(), gameID, ChessGame.TeamColor.WHITE);

        assertThrows(ResponseException.class,
            () -> facade.joinGame(auth2.authToken(), gameID, ChessGame.TeamColor.WHITE));

        GameData game = facade.listGames(auth1.authToken()).get(0);
        assertEquals("player1", game.whiteUsername());
    }
}
