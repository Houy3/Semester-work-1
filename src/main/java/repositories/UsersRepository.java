package repositories;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.User;
import models.forms.UserSignUpForm;

import java.util.List;

public interface UsersRepository extends Repository {

    List<User> selectAllUsers() throws DBException, ServiceException;

    User selectUserBySignUpForm(UserSignUpForm userSignUpForm) throws DBException, NotFoundException, NullException, ServiceException;

}
