package service;

import dataaccess.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import model.UserData;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    void registerSucceeds() throws Exception {
        UserData user = new UserData("Ethan", "password1234", "ethan@mail.com");
        AuthResult result = userService.register(user);
        assertEquals("Ethan", result.username());
        assertNotNull(result.authToken());
        assertEquals(user, userDAO.getUser("Ethan"));
    }

    @Test
    void usernameTaken() throws Exception {
        userService.register(new UserData("Ethan", "password1234", "ethan@mail.com"));
        assertThrows(AlreadyTakenException.class, () -> userService.register(new UserData("Ethan", "differentpassword", "differentemail@mail.com")));
    }

    @Test
    void badRequest() {
        UserData user = new UserData("Ethan", null, "ethan@mail.com");
        assertThrows(BadRequestException.class, () -> userService.register(user));
        assertNull(userDAO.getUser(user.username()));
    }
}