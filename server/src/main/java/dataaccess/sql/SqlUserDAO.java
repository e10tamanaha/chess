package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

public class SqlUserDAO implements UserDAO {
    private static final String CREATE_STATEMENT = """
        CREATE TABLE IF NOT EXISTS user (
            username VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            PRIMARY KEY (username)
        )
        """;

    public SqlUserDAO() throws DataAccessException {
        SqlHelper.configureTable(CREATE_STATEMENT);
    }

    @Override
    public void clear() throws DataAccessException {
        SqlHelper.executeUpdate("TRUNCATE TABLE user", "Error: unable to clear user table");
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var users = SqlHelper.executeQuery("SELECT username, password, email FROM user WHERE username = ?",
            "Error: unable to get user",
            rs -> new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email")),
            username);
        return users.isEmpty()? null : users.get(0);
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        SqlHelper.executeUpdate("INSERT INTO user (username, password, email) VALUES (?, ?, ?)",
            "Error: unable to create user", user.username(), user.password(), user.email());
    }
}