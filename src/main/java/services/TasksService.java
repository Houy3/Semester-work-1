package services;

import models.Task;
import models.Timetable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface TasksService extends Service<Task>{


    default List<Task> getTasksByTimetablesAndDate(List<Timetable> timetables, Date date) {
        List<Task> tasks = new ArrayList<>();
        for (Timetable timetable : timetables) {
            tasks.addAll(getTasksByTimetableAndDate(timetable, date));
        }
        return tasks;
    }

    List<Task> getTasksByTimetableAndDate(Timetable timetables, Date date);
}
