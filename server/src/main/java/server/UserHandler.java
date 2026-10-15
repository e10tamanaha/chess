package server;

import io.javalin.http.Context;
import service.UserService;
import com.google.gson.Gson;
import model.UserData;
import service.RegisterResult;
import service.AlreadyTakenException;
import service.BadRequestException;
import dataaccess.DataAccessException;

public class UserHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(Context ctx) throws AlreadyTakenException, DataAccessException, BadRequestException {
        UserData request = gson.fromJson(ctx.body(), UserData.class);
        RegisterResult result = userService.register(request);
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(gson.toJson(result));
    }
}