package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;

import java.sql.SQLException;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

final class SqlHelper {
    private SqlHelper() {}

    @FunctionalInterface
    interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    record UpdateResult(int rowsChanged, int generatedId) {}

    static void configureTable(String createStatement) throws DataAccessException {
        DatabaseManager.createDatabase();
        executeUpdate(createStatement, "Error: unable to configure database");
    }

    static UpdateResult executeUpdate(String sql, String errorMessage, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, params);
            int rowsChanged = ps.executeUpdate();
            try (var keys = ps.getGeneratedKeys()) {
                int generatedId = keys.next() ? keys.getInt(1) : 0;
                return new UpdateResult(rowsChanged, generatedId);
            }
        } catch (SQLException e) {
            throw new DataAccessException(errorMessage, e);
        }
    }

    private static void setParams(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) {
                ps.setNull(i + 1, Types.NULL);
            } else {
                ps.setObject(i + 1, params[i]);
            }
        }
    }

    static <T> List<T> executeQuery(String sql, String errorMessage, RowMapper<T> mapper,
                                    Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(sql)) {
            setParams(ps, params);
            try (var rs = ps.executeQuery()) {
                List<T> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
                return results;
            }
        } catch (SQLException e) {
            throw new DataAccessException(errorMessage, e);
        }
    }
}