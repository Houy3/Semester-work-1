package repositories.Impl;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Timetable;
import repositories.PeriodsRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PeriodsRepositoryImpl extends RepositoryImpl implements PeriodsRepository {

    public PeriodsRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }


    private static final String DELETE_PERIODS_BY_GROUP_ID =
            """
            delete from periods where group_id = ?
            """;
    @Override
    public void deleteGroup(Long group) throws NotFoundException, DBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PERIODS_BY_GROUP_ID)) {

            preparedStatement.setLong(1, group);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException();
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static final String SELECT_FREE_GROUP_ID =
            """
            insert into usedGroups(id) values (default) returning id;
            """;
    @Override
    public Long getNewGroupId() throws DBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FREE_GROUP_ID)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getLong("id");
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }

    }
}
