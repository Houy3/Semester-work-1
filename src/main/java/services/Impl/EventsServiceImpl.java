package services.Impl;

import exceptions.*;
import models.Event;
import models.Period;
import repositories.Inter.EventsRepository;
import services.Inter.EventsService;
import services.Inter.PeriodService;
import services.ServiceImpl;

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
    public void change(Object object, String uniqueFieldName) throws NotUniqueException, NullException, ServiceException, NotFoundException, DBException {
        super.change(object, uniqueFieldName);

        Event event = (Event) object;
        for (Period period : periodService.getByEventId(event.getNoteId())) {
            periodService.delete(period, "id");
        }
        for (Period period : event.getPeriods()) {
            periodService.add(period);
        }
    }

    @Override
    public void getByUniqueField(Object object, String uniqueFieldName) throws NotFoundException, ServiceException, DBException, NullException {
        super.getByUniqueField(object, uniqueFieldName);

        Event event = (Event) object;
        for (Period period : periodService.getByEventId(event.getNoteId())) {
            event.addPeriod(period);
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
