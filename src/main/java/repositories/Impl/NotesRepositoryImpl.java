package repositories.Impl;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Note;
import models.Timetable;
import models.User;
import models.forms.UserSignUpForm;
import repositories.NotesRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotesRepositoryImpl extends RepositoryImpl implements NotesRepository {

    public NotesRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }


    private static final String SELECT_ALL_FROM_NOTES_BY_TIMETABLE_ID =
            """
            select * 
            from notes 
            where timetable_id = ?
            """;
    @Override
    public List<Note> selectNotesByTimetableId(Long timetableId) throws NullException, ServiceException, NotFoundException, DBException {
        if (timetableId == null) {
            throw new NullException("timetableId");
        }

        List<Note> notes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FROM_NOTES_BY_TIMETABLE_ID)) {

            int i = 1;
            preparedStatement.setLong(i++, timetableId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Note note = new Note();
                    objectInsertion(note, resultSet);
                    notes.add(note);
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return notes;
    }
}
