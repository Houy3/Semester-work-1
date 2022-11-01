package services.Impl;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import Exceptions.NotUniqueException;
import models.User;
import repositories.AbstractRepository;
import services.AbstractService;
import repositories.Repository;
import repositories.UsersRepository;
import services.UsersService;

import java.util.List;

public class UsersServiceImpl extends AbstractService<User> implements UsersService {

    UsersRepository usersRepository;
    public UsersServiceImpl(AbstractRepository<User> repository) {
        super(repository);
        usersRepository = (UsersRepository) repository;
    }


    public void add(User user) throws DBException, NotUniqueException {
        super.add(user);
        try {
            singIn(user);
        } catch (NotFoundException e) {
            throw new DBException("Аномалия: автоматическая авторизация завершилась ошибкой. ");
        }
    }

    public void singIn(User user) throws NotFoundException, DBException {
        usersRepository.select_user_by_email_and_password(user);
    }

    @Override
    public List<User> getAllUsers() throws DBException {
        return usersRepository.select_all_users();
    }

}
