package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;
import java.io.IOException;
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

    private void deliver(Session session, ServerMessage message) {
        String json = GSON.toJson(message);
        synchronized (session) {
            if (!session.isOpen()) {
                return;
            }

            try {
                session.getRemote().sendString(json);
            } catch (IOException exception) {
                System.err.println("Error: " + exception.getMessage());
            }
        }
    }

    public void sendToSession(Session session, ServerMessage message) {
        deliver(session, message);
    }

    public void broadcastExcept(int gameID, Session excludedSession, ServerMessage message) {
        Set<Session> sessions = connections.get(gameID);
        if (sessions == null) {
            return;
        }
        for (Session recipient : sessions) {
            if (!recipient.equals(excludedSession)) {
                deliver(recipient, message);
            }
        }
    }

    public void broadcastToGame(int gameID, ServerMessage message) {
        broadcastExcept(gameID, null, message);
    }
}