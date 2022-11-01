package services;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import models.User;
import repositories.Repository;

import java.util.List;


public interface UsersService extends Service<User> {


    void singIn(User user) throws NotFoundException, DBException;

    List<User> getAllUsers() throws DBException;

}
