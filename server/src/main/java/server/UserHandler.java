package server;

import io.javalin.http.Context;
import service.UserService;
import com.google.gson.Gson;
import model.UserData;
import service.AuthResult;
import service.LoginRequest;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.UnauthorizedException;
import dataaccess.DataAccessException;

public class UserHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    private void successContext(Context ctx, AuthResult result) {
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(gson.toJson(result));
    }

    public void register(Context ctx) throws AlreadyTakenException, DataAccessException, BadRequestException {
        UserData request = gson.fromJson(ctx.body(), UserData.class);
        AuthResult result = userService.register(request);
        successContext(ctx, result);
    }

    public void login(Context ctx) throws UnauthorizedException, DataAccessException, BadRequestException {
        LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);
        AuthResult result = userService.login(request);
        successContext(ctx, result);
    }
}