package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import model.AuthData;

public class SqlAuthDAO implements AuthDAO {
    private static final String CREATE_STATEMENT = """
        CREATE TABLE IF NOT EXISTS auth (
            authToken VARCHAR(255) NOT NULL,
            username VARCHAR(255) NOT NULL,
            PRIMARY KEY (authToken)
        )
        """;

    public SqlAuthDAO() throws DataAccessException {
        SqlHelper.configureTable(CREATE_STATEMENT);
    }

    @Override
    public void clear() throws DataAccessException {
        SqlHelper.executeUpdate("TRUNCATE TABLE auth", "Error: unable to clear auth table");
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var auths = SqlHelper.executeQuery("SELECT authToken, username FROM auth WHERE authToken = ?",
            "Error: unable to get auth",
            rs -> new AuthData(rs.getString("authToken"), rs.getString("username")),
            authToken);
        return auths.isEmpty()? null : auths.get(0);
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        SqlHelper.executeUpdate("INSERT INTO auth (authToken, username) VALUES (?, ?)",
            "Error: unable to create auth", authData.authToken(), authData.username());
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        SqlHelper.executeUpdate("DELETE FROM auth WHERE authToken = ?",
            "Error: unable to delete auth", authToken);
    }
}