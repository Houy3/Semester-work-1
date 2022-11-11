package repositories.Impl;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.User;
import models.forms.UserSignUpForm;
import repositories.Inter.UsersRepository;
import repositories.RepositoryImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersRepositoryImpl extends RepositoryImpl implements UsersRepository {

    public UsersRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    private static final String SELECT_ALL_FROM_USERS_BY_EMAIL_AND_PASSWORD =
            """
            select * 
            from users u 
            where u.email = ? 
              and u.password_hash = ?
            """;
    public User selectUserBySignUpForm(UserSignUpForm userSignUpForm) throws DBException, NotFoundException, NullException, ServiceException {
        if (userSignUpForm.getEmail() == null) {
            throw new NullException("email");
        }
        if (userSignUpForm.getPasswordHash() == null) {
            throw new NullException("password");
        }

        User user = new User();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FROM_USERS_BY_EMAIL_AND_PASSWORD)) {

            int i = 1;
            preparedStatement.setString(i++, userSignUpForm.getEmail());
            preparedStatement.setString(i++, userSignUpForm.getPasswordHash());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    objectInsertion(user, resultSet);
                } else {
                    throw new NotFoundException();
                }
                if (resultSet.next()) {
                    throw new DBException("Аномалия: было найдено несколько пользователей при аутентификации. ");
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return user;
    }

    private static final String SELECT_ALL_FROM_USERS =
            """
            select * 
            from users
            """;
    public List<User> selectAllUsers() throws DBException, ServiceException {
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FROM_USERS)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User();
                    objectInsertion(user, resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }

        return users;
    }
}
