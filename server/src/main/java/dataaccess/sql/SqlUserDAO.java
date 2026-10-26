package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.SQLException;

public class SqlUserDAO implements UserDAO {
    private static final String CREATE_STATEMENT = """
        CREATE TABLE IF NOT EXISTS user (
            -- your columns here: username, password, email
        )
        """;

        public SqlUserDAO throws DataAccessException {
            DatabaseManager.createDatabase();
            try (var conn = DatabaseManager.getConnection();
                var ps = conn.prepareStatement(CREATE_STATEMENT)) {
                    ps.executeUpdate();
            } catch (SQLException e) {
                    throw new DataAccessException("Error: unable to configure user table", e);
            }
        }
}