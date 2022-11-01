package services;

import models.Note;
import models.Task;
import models.Timetable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface NotesService extends Service<Note>{

    default List<Note> getNotesByTimetablesAndDate(List<Timetable> timetables, Boolean isInArchive) {
        List<Note> notes = new ArrayList<>();
        for (Timetable timetable : timetables) {
            notes.addAll(getNotesByTimetableAndDate(timetable, isInArchive));
        }
        return notes;
    }

    List<Note> getNotesByTimetableAndDate(Timetable timetables, Boolean isInArchive);
}
