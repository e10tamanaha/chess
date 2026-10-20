package dataaccess;

import model.GameData;
import java.util.HashMap;
import java.util.Map;
import chess.ChessGame;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> gameData = new HashMap<>();
    private int nextID = 1;

    @Override
    public void clear() {
        gameData.clear();
        nextID = 1;
    }

    @Override
    public int createGame(String gameName) {
        int gameID = nextID;
        nextID++;
        gameData.put(gameID, new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) {
        return gameData.get(gameID);
    }
}