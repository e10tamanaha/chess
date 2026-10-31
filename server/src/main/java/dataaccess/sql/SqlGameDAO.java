package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import chess.ChessGame;
import com.google.gson.Gson;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlGameDAO implements GameDAO {
    private static final String CREATE_STATEMENT = """
        CREATE TABLE IF NOT EXISTS game (
            gameID INT NOT NULL AUTO_INCREMENT,
            whiteUsername VARCHAR(255),
            blackUsername VARCHAR(255),
            gameName VARCHAR(255) NOT NULL,
            game TEXT NOT NULL,
            PRIMARY KEY (gameID)
        )
        """;

    public SqlGameDAO() throws DataAccessException {
        SqlHelper.configureTable(CREATE_STATEMENT);
    }

    @Override
    public void clear() throws DataAccessException {
        SqlHelper.executeUpdate("TRUNCATE TABLE game", "Error: unable to clear game table");
    }

    private static GameData readGame(ResultSet rs) throws SQLException {
        ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
        return new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"),
                rs.getString("blackUsername"), rs.getString("gameName"), game);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var games = SqlHelper.executeQuery(
            "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID = ?",
            "Error: unable to get game",
            SqlGameDAO::readGame,
            gameID
        );

        return games.isEmpty() ? null : games.get(0);
    }
}