package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;

public class ConnectionManager {
    private final Map<Integer, Set<Session>> connections;
    private static final Gson GSON = new Gson();

    public ConnectionManager() {
        connections = new ConcurrentHashMap<>();
    }
}