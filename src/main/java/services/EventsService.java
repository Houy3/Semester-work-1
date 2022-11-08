package services;

import exceptions.DBException;
import exceptions.NotUniqueException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Event;
import models.Period;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface EventsService extends Service {


    List<Event> getEventsByTimetablesIdAndDate(Collection<Long> timetablesId, Date day) throws DBException, ServiceException, NullException;
    List<Event> getEventsByTimetableIdAndDate(Long timetableId, Date day) throws DBException, ServiceException, NullException;

}
