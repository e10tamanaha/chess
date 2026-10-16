package service;

import dataaccess.*;
import model.UserData;
import model.AuthData;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    private String createAuthToken() {
        return UUID.randomUUID().toString();
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

        String authToken = createAuthToken();
        AuthData auth = new AuthData(authToken, username);
        userDAO.createUser(request);
        authDAO.createAuth(auth);

        AuthResult authResult = new AuthResult(username, authToken);
        return authResult;
    }

    public AuthResult login(LoginRequest) throws UnauthorizedException, DataAccessException, BadRequestException {

    }
}
