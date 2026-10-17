package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthData> auth = new HashMap<>();

    @Override
    public void clear() {
        auth.clear();
    }

    @Override
    public AuthData getAuth(String authToken) {
        return auth.get(authToken);
    }

    @Override
    public void createAuth(AuthData authData) {
        auth.put(authData.authToken(), authData);
    }

    @Override
    public void deleteAuth(String authToken) {
        auth.remove(authToken);
    }
}