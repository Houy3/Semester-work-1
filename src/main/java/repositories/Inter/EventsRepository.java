package repositories.Inter;

import exceptions.DBException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Event;
import repositories.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface EventsRepository extends Repository {

    List<Event> selectEventsByTimetablesIdAndDate(Collection<Long> timetablesId, Date date) throws DBException, ServiceException, NullException;
    List<Event> selectEventsByTimetableIdAndDate(Long timetableId, Date date) throws NullException, ServiceException, DBException;

}
