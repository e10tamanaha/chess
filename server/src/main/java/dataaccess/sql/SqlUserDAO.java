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
}