package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, UserData> auth = new HashMap<>();

    @Override
    public void clear() {
        auth.clear();
    }
}