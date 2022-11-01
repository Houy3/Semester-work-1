package repositories.Impl;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import models.Task;
import repositories.AbstractRepository;
import repositories.TasksRepository;

import javax.sql.DataSource;
import java.sql.*;

public class TasksRepositoryImpl extends AbstractRepository<Task> implements TasksRepository {

    public TasksRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }


    private static final String DELETE_TASK_BY_NOTE_ID =
            "delete from tasks where note_id = ?";

    @Override
    public void delete(Task task) throws NotFoundException, DBException {
        if (task.getNoteId() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TASK_BY_NOTE_ID)) {

            int i = 1;
            preparedStatement.setLong(i++, task.getNoteId());

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

    private static final String SELECT_ALL_FROM_TASKS_BY_NOTE_ID =
            "SELECT * FROM tasks WHERE note_id = ?";

    @Override
    public void select_by_id(Task task) throws NotFoundException, DBException {
        if (task.getNoteId() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FROM_TASKS_BY_NOTE_ID)) {

            int i = 1;
            preparedStatement.setLong(i++, task.getNoteId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    task.setNotificationStartTime(new Date(resultSet.getTimestamp("notification_start_time").getTime()));
                    task.setDeadlineTime(new Date(resultSet.getTimestamp("deadline_time").getTime()));
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

}
