package repositories.Impl;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import models.User;
import repositories.AbstractRepository;
import repositories.UsersRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersRepositoryImpl extends AbstractRepository<User> implements UsersRepository {

    public UsersRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }




    //SQL
    private static final String SELECT_ALL_FROM_USERS =
            "SELECT * FROM users";

    public List<User> select_all_users() throws DBException {
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FROM_USERS)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setName(resultSet.getString("name"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setNickname(resultSet.getString("nickname"));
                    try {
                        user.setAccessRights(User.AccessRights.valueOf(resultSet.getString("access_rights")));
                    } catch (IllegalArgumentException e) {
                        throw new DBException("Неизвестные для модели БД права доступа: " + resultSet.getString("access_rights"));
                    }

                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }

        return users;
    }

    private static final String SELECT_ALL_FROM_USERS_BY_ID =
            "SELECT u.* FROM users u WHERE u.id = ?";

    public void select_by_id(User user) throws DBException, NotFoundException {

        if (user.getId() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FROM_USERS_BY_ID)) {

            int i = 1;
            preparedStatement.setLong(i++, user.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user.setEmail(resultSet.getString("email"));
                    user.setName(resultSet.getString("name"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setNickname(resultSet.getString("nickname"));
                    try {
                        user.setAccessRights(User.AccessRights.valueOf(resultSet.getString("access_rights")));
                    } catch (IllegalArgumentException e) {
                        throw new DBException("Неизвестные для модели " + user.getClass().getName() + " права доступа: " + resultSet.getString("access_rights") + ". ");
                    }
                } else {
                    throw new NotFoundException();
                }
                if (resultSet.next()) {
                    throw new DBException("Аномалия: было найдено несколько пользователей. ");
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static final String SELECT_ID_AND_ACCESS_RIGHTS_FROM_USERS_BY_EMAIL_AND_PASSWORD =
            "SELECT u.* FROM users u " +
                    "WHERE u.email = ? AND u.password_hash = ?";

    public void select_user_by_email_and_password(User user) throws DBException, NotFoundException {

        if (user.getEmail() == null ||
                user.getPasswordHash() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_AND_ACCESS_RIGHTS_FROM_USERS_BY_EMAIL_AND_PASSWORD)) {

            int i = 1;
            preparedStatement.setString(i++, user.getEmail());
            preparedStatement.setString(i++, user.getPasswordHash());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setNickname(resultSet.getString("nickname"));
                    try {
                        user.setAccessRights(User.AccessRights.valueOf(resultSet.getString("access_rights")));
                    } catch (IllegalArgumentException e) {
                        throw new DBException("Неизвестные для модели " + user.getClass().getName() + " права доступа: " + resultSet.getString("access_rights") + ". ");
                    }
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
    }


    private static final String DELETE_USER_BY_ID = "delete from users where id = ?";

    @Override
    public void delete(User user) throws DBException, NotFoundException {
        if (user.getId() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID)) {

            int i = 1;
            preparedStatement.setLong(i++, user.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new NotFoundException();
            }

            if (affectedRows != 1) {
                throw new DBException("Аномалия: изменилось " + affectedRows + " строк. ");
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

}
