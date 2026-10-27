package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;

import java.sql.SQLException;
import java.sql.Types;

final class SqlHelper {
    private SqlHelper() {}

    static void configureTable(String createStatement) throws DataAccessException {
        DatabaseManager.createDatabase();
        executeUpdate(createStatement, "Error: unable to configure database");
    }

    static void executeUpdate(String sql, String errorMessage, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    if (params[i] == null) {
                        ps.setNull(i + 1, Types.NULL);
                    } else {
                        ps.setObject(i + 1, params[i]);
                    }
                }
                ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(errorMessage, e);
        }
    }
}