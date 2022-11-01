package services.Impl;

import Exceptions.DBException;
import Exceptions.NotUniqueException;
import models.Note;
import models.Timetable;
import repositories.AbstractRepository;
import services.AbstractService;
import repositories.NoteRepository;
import repositories.Repository;
import services.NotesService;

import java.util.List;

public class NoteServiceImpl extends AbstractService<Note> implements NotesService {

    NoteRepository noteRepository;

    public NoteServiceImpl(AbstractRepository<Note> repository) {
        super(repository);
        noteRepository = (NoteRepository) repository;
    }

    @Override
    public List<Note> getNotesByTimetableAndDate(Timetable timetables, Boolean isInArchive) {
        //TODO
        return null;
    }
}
