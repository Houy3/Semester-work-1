package services.Impl;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import Exceptions.NotUniqueException;
import models.Task;
import models.Timetable;
import repositories.AbstractRepository;
import repositories.Repository;
import repositories.TasksRepository;
import services.AbstractService;
import services.TasksService;

import java.util.Date;
import java.util.List;

public class TaskServiceImpl extends AbstractService<Task> implements TasksService {

    TasksRepository tasksRepository;

    public TaskServiceImpl(AbstractRepository<Task> repository) {
        super(repository);
        tasksRepository = (TasksRepository) repository;
    }

    @Override
    public List<Task> getTasksByTimetableAndDate(Timetable timetables, Date date) {
        return null;
        //TODO
    }
}
