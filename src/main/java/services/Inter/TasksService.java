package services.Inter;

import exceptions.DBException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Task;
import services.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface TasksService extends Service {

    List<Task> getTasksByTimetablesIdAndDate(Collection<Long> timetablesId, Date day) throws DBException, ServiceException, NullException;
    List<Task> getTasksByTimetableIdAndDate(Long timetableId, Date day) throws DBException, ServiceException, NullException;

}
