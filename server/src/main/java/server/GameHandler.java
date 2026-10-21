package server;

import java.util.Map;
import io.javalin.http.Context;
import service.GameService;
import com.google.gson.Gson;
import service.CreateGameRequest;
import service.CreateGameResult;
import service.ListGamesResult;
import service.JoinGameRequest;
import service.BadRequestException;
import service.UnauthorizedException;
import dataaccess.DataAccessException;
import service.AlreadyTakenException;

public class GameHandler {
    private final GameService gameService;
    private final Gson gson = new Gson();

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void createGame(Context ctx) throws UnauthorizedException, DataAccessException, BadRequestException {
        String authToken = ctx.header("authorization");
        CreateGameRequest request = gson.fromJson(ctx.body(), CreateGameRequest.class);
        CreateGameResult result = gameService.createGame(authToken, request);
        ResponseWriter.success(ctx, result);
    }

    public void listGames(Context ctx) throws UnauthorizedException, DataAccessException {
        String authToken = ctx.header("authorization");
        ListGamesResult result = gameService.listGames(authToken);
        ResponseWriter.success(ctx, result);
    }

    public void joinGame(Context ctx) throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        String authToken = ctx.header("authorization");
        JoinGameRequest request = gson.fromJson(ctx.body(), JoinGameRequest.class);
        gameService.joinGame(authToken, request);
        ResponseWriter.success(ctx, Map.of());
    }
}
