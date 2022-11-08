package repositories.Impl;

import exceptions.DBException;
import exceptions.NullException;
import exceptions.ServiceException;
import jdbc.SQLAnnotations.Column;
import models.Timetable;
import repositories.TimetablesRepository;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TimetablesRepositoryImpl extends RepositoryImpl implements TimetablesRepository {

    public TimetablesRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    private static final String SELECT_ALL_TIMETABLES_BY_USER_ID =
            """
            select t.*, ut.access_rights 
            from users_timetables ut
                ,timetables t
            where t.id = ut.timetable_id
              and user_id = ?""";
    @Override
    public Map<Timetable, Timetable.AccessRights> selectTimetablesByUserId(Long userId) throws DBException, NullException, ServiceException {
        if (userId == null) {
            throw new NullException("userId");
        }

        Map<Timetable, Timetable.AccessRights> timetables = new HashMap<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TIMETABLES_BY_USER_ID)) {

            int i = 1;
            preparedStatement.setLong(i++, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Timetable timetable = new Timetable();
                    objectInsertion(timetable, resultSet);
                    try {
                        timetables.put(timetable, Timetable.AccessRights.valueOf(resultSet.getString("access_rights")));
                    } catch (IllegalArgumentException e) {
                        throw new ServiceException("Неизвестные для модели " + Timetable.class.getName() + " права доступа на таблицу: " + resultSet.getString("access_rights"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }

        return timetables;
    }
}
