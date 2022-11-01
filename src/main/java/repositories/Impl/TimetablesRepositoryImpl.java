package repositories.Impl;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import models.Timetable;
import models.User;
import repositories.AbstractRepository;
import repositories.TimetablesRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TimetablesRepositoryImpl extends AbstractRepository<Timetable> implements TimetablesRepository {

    public TimetablesRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }


    private static final String INSERT_USER_TIMETABLE =
            "insert into users_timetables(user_id, timetable_id, access_rights) values (?,?,?)";

    @Override
    public void insert_access_rights(Timetable timetable, User user, Timetable.AccessRights accessRights) throws NotFoundException, DBException {
        if (timetable.getId() == null ||
            user.getId() == null      ||
            accessRights == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_TIMETABLE)) {

            int i = 1;
            preparedStatement.setLong(i++, user.getId());
            preparedStatement.setLong(i++, timetable.getId());
            preparedStatement.setString(i++, accessRights.name());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new DBException("Аномалия: изменилось " + affectedRows + " строк. ");
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static final String UPDATE_USER_TIMETABLE_BY_ID =
            "update users_timetables set access_rights = ? where user_id = ? AND timetable_id = ?";
    @Override
    public void update_access_rights(Timetable timetable, User user, Timetable.AccessRights accessRights) throws NotFoundException, DBException {
        if (timetable.getId() == null ||
            user.getId() == null      ||
            accessRights == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_TIMETABLE_BY_ID)) {

            int i = 1;
            preparedStatement.setString(i++, accessRights.name());
            preparedStatement.setLong(i++, user.getId());
            preparedStatement.setLong(i++, timetable.getId());

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

    private static final String DELETE_TIMETABLE_BY_ID =
            "delete from timetables where id = ?";

    @Override
    public void delete(Timetable timetable) throws NotFoundException, DBException {
        if (timetable.getId() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TIMETABLE_BY_ID)) {

            int i = 1;
            preparedStatement.setLong(i++, timetable.getId());

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
    private static final String DELETE_USER_TIMETABLE_BY_ID =
            "delete from users_timetables where user_id = ? AND timetable_id = ?";

    @Override
    public void delete_access_rights(Timetable timetable, User user) throws NotFoundException, DBException {
        if (timetable.getId() == null ||
            user.getId() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_TIMETABLE_BY_ID)) {

            int i = 1;
            preparedStatement.setLong(i++, user.getId());
            preparedStatement.setLong(i++, timetable.getId());

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

    //SQL
    private static final String SELECT_TIMETABLE_BY_ID =
            "select name from timetables where id = ?";
    @Override
    public void select_by_id(Timetable timetable) throws DBException, NotFoundException {
        if (timetable.getId() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TIMETABLE_BY_ID)) {

            int i = 1;
            preparedStatement.setLong(i++, timetable.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    timetable.setName(resultSet.getString("name"));
                } else {
                    throw new NotFoundException();
                }
                if (resultSet.next()) {
                    throw new DBException("Аномалия: было найдено несколько расписаний. ");
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static final String SELECT_ALL_USERS_ID_BY_TIMETABLE =
            "select user_id, access_rights from users_timetables where timetable_id = ?";

    @Override
    public Map<User, Timetable.AccessRights> select_users_id_by_timetable(Timetable timetable) throws DBException {
        if (timetable.getId() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        Map<User, Timetable.AccessRights> users = new HashMap<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS_ID_BY_TIMETABLE)) {

            int i = 1;
            preparedStatement.setLong(i++, timetable.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("user_id"));
                    Timetable.AccessRights accessRights;
                    try {
                        accessRights =  Timetable.AccessRights.valueOf(resultSet.getString("access_rights"));
                    } catch (IllegalArgumentException e) {
                        throw new DBException("Неизвестные для модели " + Timetable.class.getName() + " права доступа на таблицу: " + resultSet.getString("access_rights"));
                    }
                    users.put(user, accessRights);
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }

        return users;
    }

    private static final String SELECT_ALL_TIMETABLES_BY_USER =
            "select timetable_id, access_rights from users_timetables where user_id = ?";

    @Override
    public Map<Timetable, Timetable.AccessRights> select_timetables_by_user(User user) throws DBException {
        if (user.getId() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        Map<Timetable, Timetable.AccessRights> timetables = new HashMap<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TIMETABLES_BY_USER)) {

            int i = 1;
            preparedStatement.setLong(i++, user.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Timetable timetable = new Timetable();
                    timetable.setId(resultSet.getLong("timetable_id"));
                    Timetable.AccessRights accessRights;
                    try {
                        accessRights = Timetable.AccessRights.valueOf(resultSet.getString("access_rights"));
                    } catch (IllegalArgumentException e) {
                        throw new DBException("Неизвестные для модели " + Timetable.class.getName() + " права доступа на таблицу: " + resultSet.getString("access_rights"));
                    }
                    timetables.put(timetable, accessRights);
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }

        for (Timetable timetable : timetables.keySet()) {
            try {
                select_by_id(timetable);
            } catch (NotFoundException e) {
                throw new DBException("Аномалия: на несуществующую таблицу завязаны пользователи. ");
            }
        }

        return timetables;
    }
}
