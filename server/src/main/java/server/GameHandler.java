package server;

import io.javalin.http.Context;
import service.GameService;
import com.google.gson.Gson;
import service.CreateGameRequest;
import service.CreateGameResult;
import service.BadRequestException;
import service.UnauthorizedException;
import dataaccess.DataAccessException;

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
}
