package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTests {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private ClearService clearService;

    @BeforeEach
    void setUp() {
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    @Test
    void clearRemovesAllData() throws Exception {
        userDAO.createUser(new UserData("Ethan", "password1234", "ethan@mail.com"));
        authDAO.createAuth(new AuthData("validToken", "Ethan"));
        int gameID = gameDAO.createGame("My Game");

        clearService.clear();

        assertNull(userDAO.getUser("Ethan"));
        assertNull(authDAO.getAuth("validToken"));
        assertNull(gameDAO.getGame(gameID));
        assertTrue(gameDAO.listGames().isEmpty());
    }
}
