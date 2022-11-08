package services.Impl;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Note;
import repositories.NotesRepository;
import repositories.Repository;
import services.NotesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotesServiceImpl extends ServiceImpl implements NotesService {

    NotesRepository notesRepository;

    public NotesServiceImpl(NotesRepository notesRepository) {
        super(notesRepository);
        this.notesRepository = notesRepository;
    }

//    @Override
//    public List<Note> getNotesByTimetablesId(List<Long> timetablesId, Boolean isInArchive) {
//        List<Note> notes = new ArrayList<>();
//        for (Long timetableId : timetablesId) {
//            notes.addAll(getNotesByTimetableId(timetableId, isInArchive));
//        }
//        return notes;
//    }
//    @Override
//    public List<Note> getNotesByTimetableId(Long timetableId, Boolean isInArchive) {
//        return null;
//    }

    @Override
    public List<Note> getNotesByTimetablesId(Collection<Long> timetablesId) throws DBException, ServiceException, NotFoundException, NullException {
        List<Note> notes = new ArrayList<>();
        for (Long timetableId : timetablesId) {
            notes.addAll(getNotesByTimetableId(timetableId));
        }
        return notes;
    }
    @Override
    public List<Note> getNotesByTimetableId(Long timetableId) throws DBException, ServiceException, NotFoundException, NullException {
        return notesRepository.selectNotesByTimetableId(timetableId);
    }

}
