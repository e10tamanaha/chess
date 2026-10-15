package dataaccess;

import model.UserData;

public interface UserDAO {
    void clear() throws DataAccessException;
    UserData getUser(String username);
    void createUser(UserData user) throws DataAccessException;
}