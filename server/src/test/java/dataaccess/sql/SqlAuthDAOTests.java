package dataaccess.sql;

import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SqlAuthDAOTests {
    private SqlAuthDAO authDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        authDAO = new SqlAuthDAO();
        authDAO.clear();
    }

    @Test
    void createAuthAndGetAuthPositive() throws DataAccessException {
        String authToken1 = "token1";
        String authToken2 = "token2";
        AuthData auth1 = new AuthData(authToken1, "magurosushi");
        AuthData auth2 = new AuthData(authToken2, "unisushi");
        authDAO.createAuth(auth1);
        authDAO.createAuth(auth2);
        assertEquals(auth1, authDAO.getAuth(authToken1));
        assertEquals(auth2, authDAO.getAuth(authToken2));
    }

    @Test
    void createAuthDuplicateNegative() throws DataAccessException {
        String authToken = "token";
        authDAO.createAuth(new AuthData(authToken, "myusername"));
        assertThrows(DataAccessException.class,
            () -> authDAO.createAuth(new AuthData(authToken, "differentusername")));
    }
}