package service;

import dataaccess.*;
import model.AuthData;

public class Authenticator {
    private final AuthDAO authDAO;

    public Authenticator(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthData authenticate(String authToken) throws UnauthorizedException, DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);

        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        return authData;
    }
}