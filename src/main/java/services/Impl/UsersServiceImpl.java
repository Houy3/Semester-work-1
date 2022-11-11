package services.Impl;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.User;
import models.forms.UserSignUpForm;
import repositories.Inter.UsersRepository;
import services.Inter.UsersService;
import services.ServiceImpl;

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
