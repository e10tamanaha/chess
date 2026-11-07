package client;

import com.google.gson.Gson;
import java.net.http.HttpClient;

import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.util.Map;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();

    private final String serverUrl;

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    private static HttpRequest.BodyPublisher bodyPublisher(Object body) {
        return body == null ? BodyPublishers.noBody() : BodyPublishers.ofString(GSON.toJson(body));
    }

    private <T> T makeRequest(String method, String path, Object body, String authToken, 
        Class<T> responseClass, Map<Integer, String> errorMessages) throws ResponseException {
        try {
            var builder = HttpRequest.newBuilder(URI.create(serverUrl + path))
                .method(method, bodyPublisher(body));
            if (authToken != null) {
                builder.header("authorization", authToken);
            }

            HttpResponse<String> response = HTTP_CLIENT.send(builder.build(), BodyHandlers.ofString());

            int status = response.statusCode();
            if (status < 200 || status >= 300) {
                String message = errorMessages.getOrDefault(status, "The server ran into a problem. Please try again");
                throw new ResponseException(message, status);
            }

            return responseClass == null ? null : GSON.fromJson(response.body(), responseClass);
        } catch (IOException | InterruptedException e) {
            throw new ResponseException("Unable to reach the server", e);
        }
    }

    public AuthData register(String username, String password, String email) throws ResponseException {
        var body = new UserData(username, password, email);
        return makeRequest("POST", "/user", body, null, AuthData.class, Map.of(
            400, "Please provide a username, password, and email",
            403, "That username is already taken"));
    }

    public void clear() throws ResponseException {
        makeRequest("DELETE", "/db", null, null, null, Map.of());
    }
}