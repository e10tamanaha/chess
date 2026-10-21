package server;

import io.javalin.*;
import com.google.gson.Gson;
import java.util.Map;

import dataaccess.*;
import service.ClearService;
import service.UserService;
import service.GameService;
import service.Authenticator;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.UnauthorizedException;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        Authenticator authenticator = new Authenticator(authDAO);

        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);
        UserService userService = new UserService(userDAO, authDAO, authenticator);
        GameService gameService = new GameService(gameDAO, authenticator);

        javalin.delete("/db", new ClearHandler(clearService));

        UserHandler userHandler = new UserHandler(userService);
        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);

        GameHandler gameHandler = new GameHandler(gameService);
        javalin.post("/game", gameHandler::createGame);
        javalin.get("/game", gameHandler::listGames);
        javalin.put("/game", gameHandler::joinGame);

        javalin.exception(Exception.class, (e, ctx) -> {
            ctx.status(500);
            ctx.contentType("application/json");
            ctx.result(new Gson().toJson(Map.of("message", "Error: " + e.getMessage())));
        });

        javalin.exception(AlreadyTakenException.class, (e, ctx) -> {
            ctx.status(403);
            ctx.contentType("application/json");
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        });

        javalin.exception(BadRequestException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.contentType("application/json");
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        });

        javalin.exception(UnauthorizedException.class, (e, ctx) -> {
            ctx.status(401);
            ctx.contentType("application/json");
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
