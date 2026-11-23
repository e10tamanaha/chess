package server.websocket;

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

    public void add(int gameID, Session session) {
        connections.computeIfAbsent(gameID, (id) -> ConcurrentHashMap.newKeySet()).add(session);
    }

    public void remove(int gameID, Session session) {
        Set<Session> sessions = connections.get(gameID);
        if (sessions == null) {
            return;
        }
        sessions.remove(session);
    }

    public void removeSession(Session session) {
        for (Set<Session> sessions : connections.values()) {
            sessions.remove(session);
        }
    }
}