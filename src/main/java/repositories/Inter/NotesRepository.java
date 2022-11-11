package repositories.Inter;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Note;
import models.Timetable;
import repositories.Repository;

import java.util.List;

public interface NotesRepository extends Repository {

    List<Note> selectNotesByTimetableId(Long timetableId) throws NullException, ServiceException, DBException;

    List<Note> selectNotesByTimetablesId(List<Long> timetablesId) throws NullException, ServiceException, DBException;


    Timetable.AccessRights selectAccessRightsOnNoteByNoteIdAndUserId(Long noteId, Long userId) throws NullException, ServiceException, DBException, NotFoundException;
}
