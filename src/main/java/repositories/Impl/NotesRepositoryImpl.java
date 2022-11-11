package repositories.Impl;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Note;
import models.Timetable;
import models.User;
import repositories.Inter.NotesRepository;
import repositories.RepositoryImpl;
import services.Inter.TimetablesService;

import javax.sql.DataSource;
import java.sql.*;
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
            order by last_change_time desc
            """;
    @Override
    public List<Note> selectNotesByTimetableId(Long timetableId) throws NullException, ServiceException, DBException {
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

    @Override
    public List<Note> selectNotesByTimetablesId(List<Long> timetablesId) throws NullException, ServiceException, DBException {
        List<Note> notes = new ArrayList<>();
        for (Long timetableId : timetablesId) {
            notes.addAll(selectNotesByTimetableId(timetableId));
        }
        return notes;
    }

    private static final String SELECT_ACCESS_RIGHTS_FROM_NOTES_BY_USER_ID =
            """
            select ut.access_rights
            from notes n
                ,users_timetables ut
            where n.id = ?
              and n.timetable_id = ut.timetable_id
              and ut.user_id = ?
            """;
    @Override
    public Timetable.AccessRights selectAccessRightsOnNoteByNoteIdAndUserId(Long noteId, Long userId) throws NullException, ServiceException, DBException, NotFoundException {
        if (noteId == null) {
            throw new NullException("noteId");
        }
        if (userId == null) {
            throw new NullException("userId");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACCESS_RIGHTS_FROM_NOTES_BY_USER_ID)) {

            int i = 1;
            preparedStatement.setLong(i++, noteId);
            preparedStatement.setLong(i++, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    try {
                        Timetable.AccessRights accessRights = Timetable.AccessRights.valueOf(resultSet.getString("access_rights"));
                        return accessRights;
                    } catch (IllegalArgumentException e) {
                        throw new ServiceException("Неизвестные права доступа у модели " + Timetable.class.getName() + ". ");
                    }
                } else {
                    throw new NotFoundException();
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
