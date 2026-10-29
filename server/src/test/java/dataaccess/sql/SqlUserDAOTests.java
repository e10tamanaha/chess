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
}
