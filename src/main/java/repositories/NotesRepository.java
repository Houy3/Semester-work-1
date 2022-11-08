package repositories;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Note;

import java.util.List;

public interface NotesRepository extends Repository{

    List<Note> selectNotesByTimetableId(Long timetableId) throws NullException, ServiceException, NotFoundException, DBException;

}
