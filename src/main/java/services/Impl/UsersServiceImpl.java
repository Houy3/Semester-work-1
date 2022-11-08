package services.Impl;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.User;
import models.forms.UserSignUpForm;
import repositories.Repository;
import repositories.UsersRepository;
import services.UsersService;

import java.util.ArrayList;
import java.util.List;

public class UsersServiceImpl extends ServiceImpl implements UsersService {

    private UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        super(usersRepository);
        this.usersRepository = usersRepository;
    }

    @Override
    public User singIn(UserSignUpForm userSignUpForm) throws NotFoundException, DBException, NullException, ServiceException {
        return usersRepository.selectUserBySignUpForm(userSignUpForm);
    }

    @Override
    public List<User> getAllUsers() throws DBException, ServiceException {
        return usersRepository.selectAllUsers();
    }
}
