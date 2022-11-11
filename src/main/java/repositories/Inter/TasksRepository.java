package repositories.Inter;

import exceptions.DBException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Task;
import repositories.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface TasksRepository extends Repository {

    List<Task> selectTasksByTimetablesIdAndDate(Collection<Long> timetablesId, Date day) throws DBException, ServiceException, NullException;
    List<Task> selectTasksByTimetableIdAndDate(Long timetableId, Date day) throws NullException, ServiceException, DBException;

}
