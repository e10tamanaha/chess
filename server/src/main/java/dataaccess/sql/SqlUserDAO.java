package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.SQLException;

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
        DatabaseManager.createDatabase();
        executeUpdate(CREATE_STATEMENT, "Error: unable to configure user table");
    }

    private static void executeUpdate(String sql, String errorMessage) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(errorMessage, e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        executeUpdate("TRUNCATE TABLE user", "Error: unable to clear user table");
    }
}