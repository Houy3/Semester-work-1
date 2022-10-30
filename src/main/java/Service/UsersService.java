package Service;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import Exceptions.NotUniqueException;
import models.User;

public interface UsersService {

    void singUp(User user) throws NotUniqueException, DBException;

    void singIn(User user) throws NotFoundException, DBException;
}
