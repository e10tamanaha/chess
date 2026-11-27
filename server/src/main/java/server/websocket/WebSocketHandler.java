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
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import service.GameplayService;
import service.GameplayContext;
import service.PlayerRole;
import service.UnauthorizedException;
import service.BadRequestException;
import service.MoveResult;
import service.GameStatus;
import chess.ChessNotation;
import chess.ChessGame;
import chess.ChessMove;
import dataaccess.DataAccessException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler{
    private final ConnectionManager manager;
    private static final Gson GSON = new Gson();
    private final GameplayService gameplayService;

    public WebSocketHandler(ConnectionManager manager, GameplayService gameplayService) {
        this.manager = manager;
        this.gameplayService = gameplayService;
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
        } catch (BadRequestException | UnauthorizedException exception) {
            manager.sendToSession(session, new ErrorMessage(exception.getMessage()));
        } catch (Exception exception) {
            System.err.println("Error: " + exception);
            manager.sendToSession(session, new ErrorMessage("Error: invalid command"));
        }
    }

    private void routeCommand(Session session, String json) throws UnauthorizedException, BadRequestException, DataAccessException {
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

    private String joinMessage(GameplayContext context) {
        String username = context.username();
        PlayerRole role = context.role();

        if (role == PlayerRole.OBSERVER) {
            return username + " joined the game as an observer";
        } 

        return username + " joined the game as " + role;
    }

    private void connect(Session session, UserGameCommand command) throws UnauthorizedException, BadRequestException, DataAccessException {
        GameplayContext context = gameplayService.connect(command.getAuthToken(), command.getGameID());
        int gameID = context.gameData().gameID();
        manager.add(gameID, session);
        manager.sendToSession(session, new LoadGameMessage(context.gameData().game()));
        manager.broadcastExcept(gameID, session, new NotificationMessage(joinMessage(context)));
    }

    private void leave(Session session, UserGameCommand command) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void resign(Session session, UserGameCommand command) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void makeMove(Session session, MakeMoveCommand command) throws UnauthorizedException, BadRequestException, DataAccessException {
        MoveResult result = gameplayService.makeMove(command.getAuthToken(), command.getGameID(), command.getMove());
        GameplayContext context = result.context();
        int gameID = context.gameData().gameID();
        ChessGame game = context.gameData().game();
        manager.broadcastToGame(gameID, new LoadGameMessage(game));
        manager.broadcastExcept(gameID, session, new NotificationMessage(moveSentence(context.username(), command.getMove())));

        if (result.status() != GameStatus.NONE) {
            manager.broadcastToGame(gameID, new NotificationMessage(statusSentence(result)));
        }
    }

    private String moveSentence(String username, ChessMove move) {
        return username + " moved " + ChessNotation.formatMove(move);
    }

    private String statusSentence(MoveResult result) {
        String opponent = result.context().role() == PlayerRole.WHITE ? 
            result.context().gameData().blackUsername() : result.context().gameData().whiteUsername();
        
        return switch (result.status()) {
            case CHECK -> opponent + " is in check";
            case CHECKMATE -> opponent + " is in checkmate";
            case STALEMATE -> "Stalemate has been reached. The game is a draw";
            case NONE -> throw new IllegalStateException("Status has to be check, checkmate, or stalemate");
        };
    }

    @Override
    public void handleClose(WsCloseContext context) {
        manager.removeSession(context.session);
    }
}