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
import java.util.Map;

public class UserHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(Context ctx) throws AlreadyTakenException, DataAccessException, BadRequestException {
        UserData request = gson.fromJson(ctx.body(), UserData.class);
        AuthResult result = userService.register(request);
        ResponseWriter.success(ctx, result);
    }

    public void login(Context ctx) throws UnauthorizedException, DataAccessException, BadRequestException {
        LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);
        AuthResult result = userService.login(request);
        ResponseWriter.success(ctx, result);
    }

    public void logout(Context ctx) throws UnauthorizedException, DataAccessException {
        String authToken = ctx.header("authorization");
        userService.logout(authToken);
        ResponseWriter.success(ctx, Map.of());
    }
}