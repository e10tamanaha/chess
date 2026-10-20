package server;

import io.javalin.http.Context;
import com.google.gson.Gson;

public class ResponseWriter {
    private static final Gson GSON = new Gson();
    
    private ResponseWriter() {}

    public static void successContext(Context ctx, Object result) {
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(GSON.toJson(result));
    }
}