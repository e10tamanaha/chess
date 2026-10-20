package dataaccess;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
}