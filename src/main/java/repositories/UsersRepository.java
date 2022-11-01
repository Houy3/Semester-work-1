package repositories;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import models.User;

import java.util.List;

public interface UsersRepository extends Repository<User> {

    List<User> select_all_users() throws DBException;

    void select_user_by_email_and_password(User user) throws DBException, NotFoundException;

}
