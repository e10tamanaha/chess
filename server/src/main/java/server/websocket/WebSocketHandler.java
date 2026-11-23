package server.websocket;

import com.google.gson.Gson;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler{
    private final ConnectionManager manager;
    private static final Gson GSON = new Gson();

    public WebSocketHandler(ConnectionManager manager) {
        this.manager = manager;
    }

    @Override
    public void handleConnect(WsConnectContext context) {
        context.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext context) {
        Session session = context.session;
        String json = context.message();

        try {
            routeCommand(session, json);
        } catch (Exception exception) {
            System.err.println("Error: " + exception);
            manager.sendToSession(session, new ErrorMessage("Error: invalid command"));
        }
    }

    private void routeCommand(Session session, String json) {
        UserGameCommand command = GSON.fromJson(json, UserGameCommand.class);

        if (command == null || command.getCommandType() == null) {
            throw new IllegalArgumentException("Invalid command");
        }

        switch (command.getCommandType()) {
            case CONNECT -> connect(session, command);
            case LEAVE -> leave(session, command);
            case RESIGN -> resign(session, command);
            case MAKE_MOVE -> makeMove(session, GSON.fromJson(json, MakeMoveCommand.class));
        }
    }

    private void connect(Session session, UserGameCommand command) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void leave(Session session, UserGameCommand command) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void resign(Session session, UserGameCommand command) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void makeMove(Session session, MakeMoveCommand command) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void handleClose(WsCloseContext context) {
        manager.removeSession(context.session);
    }
}