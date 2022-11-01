package repositories.Impl;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import models.Note;
import repositories.AbstractRepository;
import repositories.NoteRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotesRepositoryImpl extends AbstractRepository<Note> implements NoteRepository {

    public NotesRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    private static final String INSERT_NOTE =
            "insert into notes(timetable_id, name, body, is_in_archive) " +
                    "values (?, ?, ?, ?) returning id";
    @Override
    public void insert(Note note) throws DBException {
        if (note.getTimetableId() == null ||
            note.getName() == null ||
            note.getBody() == null ||
            note.getInArchive() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NOTE)) {

            int i = 1;
            preparedStatement.setLong(i++, note.getTimetableId());
            preparedStatement.setString(i++, note.getName());
            preparedStatement.setString(i++, note.getBody());
            preparedStatement.setBoolean(i++, note.getInArchive());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                note.setId(resultSet.getLong("id"));
            } else {
                throw new DBException("Аномалия: вставка расписания не произошла. ");
            }
            if (resultSet.next()) {
                throw new DBException("Аномалия: произошло больше 1 вставки. ");
            }

        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static final String UPDATE_NOTE_BY_ID =
            "update notes set timetable_id = ?, name = ?, body = ?, is_in_archive = ? where id = ?";

    @Override
    public void update(Note note) throws NotFoundException, DBException {
        if (note.getId() == null ||
            note.getTimetableId() == null ||
            note.getName() == null ||
            note.getBody() == null ||
            note.getInArchive() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_NOTE_BY_ID)) {

            int i = 1;
            preparedStatement.setLong(i++, note.getTimetableId());
            preparedStatement.setString(i++, note.getName());
            preparedStatement.setString(i++, note.getBody());
            preparedStatement.setBoolean(i++, note.getInArchive());
            preparedStatement.setLong(i++, note.getId());

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
    private static final String DELETE_NOTE_BY_ID =
            "delete from notes where id = ?";

    @Override
    public void delete(Note note) throws NotFoundException, DBException {
        if (note.getId() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_NOTE_BY_ID)) {

            int i = 1;
            preparedStatement.setLong(i++, note.getId());

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

    private static final String SELECT_ALL_FROM_NOTES_BY_ID =
            "SELECT * FROM notes u WHERE u.id = ?";
    @Override
    public void select_by_id(Note note) throws NotFoundException, DBException {
        if (note.getId() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FROM_NOTES_BY_ID)) {

            int i = 1;
            preparedStatement.setLong(i++, note.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    note.setTimetableId(resultSet.getLong("timetable_id"));
                    note.setName(resultSet.getString("name"));
                    note.setBody(resultSet.getString("body"));
                    note.setInArchive(resultSet.getBoolean("is_in_archive"));
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
}
