package repositories.Impl;

import exceptions.DBException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Task;
import repositories.Inter.TasksRepository;
import repositories.RepositoryImpl;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class TasksRepositoryImpl extends RepositoryImpl implements TasksRepository {
    public TasksRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Task> selectTasksByTimetablesIdAndDate(Collection<Long> timetablesId, Date date) throws DBException, ServiceException, NullException {
        List<Task> tasks = new ArrayList<>();
        for (Long timetableId : timetablesId) {
           tasks.addAll(selectTasksByTimetableIdAndDate(timetableId, date));
        }
        return tasks;
    }



    private static final String SELECT_ALL_TASKS_BY_TIMETABLE_ID_AND_DAY =
            """        
            select t.*
            from tasks t
               , notes n
            where t.note_id = n.id
              and n.timetable_id = ?
              and t.notification_start_date <= ?
              and (? < t.deadline_time or t.is_done = false)
            order by t.notification_start_date
            """;

    @Override
    public List<Task> selectTasksByTimetableIdAndDate(Long timetableId, Date day) throws NullException, ServiceException, DBException {
        if (timetableId == null) {
            throw new NullException("timetableId");
        }
        if (day == null) {
            throw new NullException("day");
        }

        List<Task> tasks = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TASKS_BY_TIMETABLE_ID_AND_DAY)) {

            int i = 1;
            preparedStatement.setLong(i++, timetableId);
            preparedStatement.setTimestamp(i++, new Timestamp(day.getTime()));
            preparedStatement.setTimestamp(i++, new Timestamp(day.getTime()));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Task task = new Task();
                    objectInsertion(task, resultSet);
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }

        return tasks;
    }
}
