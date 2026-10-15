package dataaccess;

import model.GameData;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<String, GameData> game_data = new HashMap<>();

    @Override
    public void clear() {
        game_data.clear();
    }
}