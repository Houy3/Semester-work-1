package services.Impl;

import exceptions.*;
import models.Note;
import models.Timetable;
import repositories.Inter.NotesRepository;
import services.Inter.NotesService;
import services.ServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class NotesServiceImpl extends ServiceImpl implements NotesService {

    NotesRepository notesRepository;

    public NotesServiceImpl(NotesRepository notesRepository) {
        super(notesRepository);
        this.notesRepository = notesRepository;
    }

    @Override
    public void add(Object object) throws NotUniqueException, NullException, ServiceException, DBException {
        Note note;
        try {
            note = (Note) object;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        note.setLastChangeTime(new Date());
        super.add(object);
    }

    @Override
    public void change(Object object, String uniqueFieldName) throws NotUniqueException, NullException, ServiceException, NotFoundException, DBException {
        Note note;
        try {
            note = (Note) object;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        note.setLastChangeTime(new Date());
        super.change(object, uniqueFieldName);
    }

    @Override
    public List<Note> getNotesByTimetablesId(Collection<Long> timetablesId) throws DBException, ServiceException, NullException {
        List<Note> notes = new ArrayList<>();
        for (Long timetableId : timetablesId) {
            notes.addAll(getNotesByTimetableId(timetableId));
        }
        return notes;
    }
    @Override
    public List<Note> getNotesByTimetableId(Long timetableId) throws DBException, ServiceException, NullException {
        return notesRepository.selectNotesByTimetableId(timetableId);
    }

    @Override
    public Timetable.AccessRights getAccessRightsOnNote(Long noteId, Long userId) throws DBException, ServiceException, NotFoundException, NullException {
        return  notesRepository.selectAccessRightsOnNoteByNoteIdAndUserId(noteId, userId);
    }

}
