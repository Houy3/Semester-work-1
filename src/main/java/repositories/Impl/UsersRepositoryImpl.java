package repositories.Impl;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import models.User;
import repositories.UsersRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersRepositoryImpl implements UsersRepository {

    DataSource dataSource;

    public UsersRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final String INSERT_USER = "insert into users(email, password_hash, access_rights_id) values (?, ?, ?);\n";
    public void add(User user) throws DBException {

        Long accessRights = get_access_rights_id_by_name(User.AccessRights.REGULAR);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)) {

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPasswordHash());
            preparedStatement.setLong(3, accessRights);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new DBException("Аномалия: изменилось " + affectedRows + " строк. ");
            }

        } catch (SQLException e) {
            throw new DBException(e);
        }
    }


    //SQL
    private static final String SELECT_ALL_FROM_USERS = "SELECT u.id, u.email, ar.name access_rights FROM users u left join access_rights ar on u.access_rights_id = ar.id";
    public List<User> getAllUsers() throws DBException{
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FROM_USERS)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setEmail(resultSet.getString("email"));
                    try {
                        user.setAccessRights(User.AccessRights.valueOf(resultSet.getString("access_rights")));
                    } catch (IllegalArgumentException e) {
                        throw new DBException("Неизвестные для модели права доступа " + resultSet.getString("email"));
                    }
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }

        return users;
    }

    private static final String SELECT_ID_AND_ACCESS_RIGHTS_FROM_USERS_BY_EMAIL_AND_PASSWORD =
            "SELECT u.id, ar.name access_rights FROM users u left join access_rights ar on u.access_rights_id = ar.id " +
            "WHERE u.email = ? AND u.password_hash = ?";
    public void find_user_by_email_and_password(User user) throws DBException, NotFoundException {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_AND_ACCESS_RIGHTS_FROM_USERS_BY_EMAIL_AND_PASSWORD)) {

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPasswordHash());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getLong("id"));
                    try {
                        user.setAccessRights(User.AccessRights.valueOf(resultSet.getString("access_rights")));
                    } catch (IllegalArgumentException e) {
                        throw new DBException("Неизвестные для модели права доступа " + resultSet.getString("email") + ". ");
                    }
                } else {
                    throw new NotFoundException();
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }



    private static final String SELECT_ID_FROM_ACCESS_RIGHTS_BY_NAME = "select ar.id from access_rights ar where ar.name = ?";
    private Long get_access_rights_id_by_name(User.AccessRights accessRights) throws DBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_FROM_ACCESS_RIGHTS_BY_NAME)) {

            preparedStatement.setString(1, accessRights.name());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                } else {
                    throw new DBException("Неизвестные для БД права доступа " + accessRights.name());
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
