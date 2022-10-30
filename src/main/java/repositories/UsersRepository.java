package repositories;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import models.User;

import java.util.List;

public interface UsersRepository {

    void add(User user) throws DBException;

    List<User> getAllUsers() throws DBException;

    void find_user_by_email_and_password(User user) throws DBException, NotFoundException;
}
