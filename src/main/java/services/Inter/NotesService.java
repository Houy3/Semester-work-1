package services.Inter;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Note;
import models.Timetable;
import services.Service;

import java.util.Collection;
import java.util.List;

public interface NotesService extends Service {

    List<Note> getNotesByTimetablesId(Collection<Long> timetablesId) throws DBException, ServiceException, NullException;
    List<Note> getNotesByTimetableId(Long timetableId) throws DBException, ServiceException, NullException;

    Timetable.AccessRights getAccessRightsOnNote(Long noteId, Long userId) throws DBException, ServiceException, NotFoundException, NullException;
}
