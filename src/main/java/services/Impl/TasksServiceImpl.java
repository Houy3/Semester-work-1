package services.Impl;

import exceptions.DBException;
import exceptions.NotUniqueException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Task;
import repositories.TasksRepository;
import services.TasksService;

import java.util.*;

public class TasksServiceImpl extends ServiceImpl implements TasksService {

    TasksRepository tasksRepository;

    public TasksServiceImpl(TasksRepository tasksRepository) {
        super(tasksRepository);
        this.tasksRepository = tasksRepository;
    }

    @Override
    public void add(Object object) throws NotUniqueException, NullException, ServiceException, DBException {
        Task task;
        try {
            task = (Task) object;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        if (task.getNotificationStartDate().compareTo(task.getDeadlineTime()) > 0) {
            throw new ServiceException("Дата окончания меньше даты начала. ");
        }
        //обрезаю часы, минуты и тп, так как нужен только день
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(task.getNotificationStartDate());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        task.setNotificationStartDate(calendar.getTime());

        super.add(task);
    }

    @Override
    public List<Task> getTasksByTimetablesIdAndDate(Collection<Long> timetablesId, Date day) throws DBException, ServiceException, NullException {

        List<Task> tasks = new ArrayList<>();
        for (Long timetableId : timetablesId) {
            tasks.addAll(getTasksByTimetableIdAndDate(timetableId, day));
        }
        return tasks;
    }

    @Override
    public List<Task> getTasksByTimetableIdAndDate(Long timetableId, Date day) throws DBException, ServiceException, NullException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        return tasksRepository.selectTasksByTimetableIdAndDate(timetableId, calendar.getTime());
    }

}
