package client;

import org.junit.jupiter.api.*;
import server.Server;
import model.AuthData;
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
}
