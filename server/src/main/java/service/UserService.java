package service;

import dataaccess.*;
import model.UserData;
import model.AuthData;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final Authenticator authenticator;

    public UserService(UserDAO userDAO, AuthDAO authDAO, Authenticator authenticator) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.authenticator = authenticator;
    }

    private String createAuthToken() {
        return UUID.randomUUID().toString();
    }

    private String addAuthData(String username) throws DataAccessException {
        String authToken = createAuthToken();
        AuthData auth = new AuthData(authToken, username);
        authDAO.createAuth(auth);

        return authToken;
    }

    public AuthResult register(UserData request) throws AlreadyTakenException, DataAccessException, BadRequestException {
        if (request == null || request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        
        String username = request.username();
        UserData user = userDAO.getUser(username);
        
        if (user != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        userDAO.createUser(new UserData(username, hashedPassword, request.email()));

        String authToken = addAuthData(username);

        AuthResult registerResult = new AuthResult(username, authToken);
        return registerResult;
    }

    public AuthResult login(LoginRequest request) throws UnauthorizedException, DataAccessException, BadRequestException {
        if (request == null || request.username() == null || request.password() == null) {
            throw new BadRequestException("Error: bad request");
        }

        String username = request.username();
        String password = request.password();
        UserData user = userDAO.getUser(username);

        if (user == null || !BCrypt.checkpw(password, user.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String authToken = addAuthData(username);
        AuthResult loginResult = new AuthResult(username, authToken);
        return loginResult;
    }

    public void logout(String authToken) throws UnauthorizedException, DataAccessException {
        authenticator.authenticate(authToken);
        authDAO.deleteAuth(authToken);
    }
}
