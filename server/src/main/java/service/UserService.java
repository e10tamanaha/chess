package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.UserData;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
    }

    public UserData getUser(String username) {
        UserData user = userDAO.getUser(username);
    }
}
