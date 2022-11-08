package services;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.User;
import models.forms.UserSignUpForm;

import java.util.List;

public interface UsersService extends Service {

    User singIn(UserSignUpForm userSignUpForm) throws NotFoundException, DBException, NullException, ServiceException;

    List<User> getAllUsers() throws DBException, ServiceException;

}
