package dataaccess.sql;

import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SqlUserDAOTests {
    private SqlUserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new SqlUserDAO();
        userDAO.clear();
    }

    @Test
    void createUserAndGetUserPositive() throws DataAccessException {
        String username1 = "Ethan";
        UserData user1 = new UserData(username1, "toinfinityandbeyond", "ethanmail@mail.com");
        userDAO.createUser(user1);
        String username2 = "Elise";
        UserData user2 = new UserData(username2, "lifeislikeaboxofchocolates", "elisemail@mail.com");
        userDAO.createUser(user2);
        assertEquals(user1, userDAO.getUser(username1));
        assertEquals(user2, userDAO.getUser(username2));
    }

    @Test
    void createUserDuplicateNegative() throws DataAccessException {
        String username = "Ethan";
        UserData user1 = new UserData(username, "toinfinityandbeyond", "ethanmail@mail.com");
        UserData user2 = new UserData(username, "differentpassword", "differentemail@mail.com");
        userDAO.createUser(user1);
        assertThrows(DataAccessException.class, () -> userDAO.createUser(user2));
    }

    @Test
    void getUserNegative() throws DataAccessException {
        assertNull(userDAO.getUser("neverexisted"));
    }

    @Test
    void clearPositive() throws DataAccessException {
        String username1 = "Ethan";
        String username2 = "Elise";
        String username3 = "Eli";
        userDAO.createUser(new UserData(username1, "ohagi", "ethan@mail.com"));
        userDAO.createUser(new UserData(username2, "strawberryshortcake", "elise@mail.com"));
        userDAO.createUser(new UserData(username3, "softserveicecream", "eli@mail.com"));
        userDAO.clear();
        assertNull(userDAO.getUser(username1));
        assertNull(userDAO.getUser(username2));
        assertNull(userDAO.getUser(username3));
    }
}
