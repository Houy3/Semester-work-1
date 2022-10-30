package Service.Impl;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import Exceptions.NotUniqueException;
import Service.UsersService;
import models.User;
import repositories.UsersRepository;

import java.util.List;

public class UsersServiceImpl implements UsersService {

    UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void singUp(User user) throws NotUniqueException, DBException {

        List<User> users = usersRepository.getAllUsers();
        for (User user_ : users) {
            if (user.equals(user_)) {
                throw new NotUniqueException();
            }
        }
        usersRepository.add(user);

        try {
            singIn(user);
        } catch (NotFoundException e) {
            throw new DBException("Аномалия: автоматическая авторизация завершилась ошибкой. ");
        }
    }

    public void singIn(User user) throws NotFoundException, DBException {
        usersRepository.find_user_by_email_and_password(user);
    }
}
