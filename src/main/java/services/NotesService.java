package services;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Note;

import java.util.Collection;
import java.util.List;

public interface NotesService extends Service {

    List<Note> getNotesByTimetablesId(Collection<Long> timetablesId) throws DBException, ServiceException, NotFoundException, NullException;
    List<Note> getNotesByTimetableId(Long timetableId) throws DBException, ServiceException, NotFoundException, NullException;

}
