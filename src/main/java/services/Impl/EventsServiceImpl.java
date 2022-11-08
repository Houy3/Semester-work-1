package services.Impl;

import exceptions.DBException;
import exceptions.NotUniqueException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Event;
import models.Period;
import repositories.EventsRepository;
import repositories.PeriodsRepository;
import services.EventsService;
import services.PeriodService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class EventsServiceImpl extends ServiceImpl implements EventsService {

    EventsRepository eventsRepository;
    PeriodService periodService;

    public EventsServiceImpl(EventsRepository eventsRepository,
                             PeriodService periodService) {
        super(eventsRepository);
        this.eventsRepository = eventsRepository;
        this.periodService = periodService;
    }

    @Override
    public void add(Object object) throws NotUniqueException, NullException, ServiceException, DBException {
        super.add(object);

        Event event = (Event) object;
        for (Period period : event.getPeriods()) {
            periodService.add(period);
        }
    }

    @Override
    public List<Event> getEventsByTimetablesIdAndDate(Collection<Long> timetablesId, Date day) throws DBException, ServiceException, NullException {
        List<Event> events = new ArrayList<>();
        for (Long timetableId : timetablesId) {
            events.addAll(getEventsByTimetableIdAndDate(timetableId, day));
        }
        return events;
    }
    @Override
    public List<Event> getEventsByTimetableIdAndDate(Long timetableId, Date day) throws DBException, ServiceException, NullException {
        return eventsRepository.selectEventsByTimetableIdAndDate(timetableId, day);
    }


}
