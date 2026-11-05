package client;

public class ResponseException extends Exception {
    private final int statusCode;

    public ResponseException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ResponseException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
    }

    public int statusCode() {
        return statusCode;
    }
}