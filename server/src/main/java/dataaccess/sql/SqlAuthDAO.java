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
}