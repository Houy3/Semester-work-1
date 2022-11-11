package repositories.Impl;

import exceptions.DBException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Event;
import models.Period;
import repositories.Inter.EventsRepository;
import repositories.RepositoryImpl;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class EventsRepositoryImpl extends RepositoryImpl implements EventsRepository {

    public EventsRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Event> selectEventsByTimetablesIdAndDate(Collection<Long> timetablesId, Date day) throws DBException, ServiceException, NullException {
        List<Event> events = new ArrayList<>();
        for (Long timetableId : timetablesId) {
            events.addAll(selectEventsByTimetableIdAndDate(timetableId, day));
        }
        return events;
    }

    private static final String SELECT_ALL_EVENTS_BY_TIMETABLE_ID_AND_DAY =
            """        
            select e.*, p.*
            from events e
               , notes n
               , periods p
            where e.note_id = n.id
              and n.timetable_id = ?
              and e.note_id = p.event_id
              and p.start_time < (date_trunc('day', ?::timestamp) + interval '1 day')
              and (date_trunc('day', ?::timestamp) <= p.end_time)
            order by p.start_time
            """;
    @Override
    public List<Event> selectEventsByTimetableIdAndDate(Long timetableId, Date day) throws NullException, ServiceException, DBException {
        if (timetableId == null) {
            throw new NullException("timetableId");
        }
        if (day == null) {
            throw new NullException("day");
        }

        List<Event> events = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_EVENTS_BY_TIMETABLE_ID_AND_DAY)) {

            int i = 1;
            preparedStatement.setLong(i++, timetableId);
            preparedStatement.setTimestamp(i++, new Timestamp(day.getTime()));
            preparedStatement.setTimestamp(i++, new Timestamp(day.getTime()));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Event last = new Event();
                last.setNoteId(-1L);
                while (resultSet.next()) {
                    Event event = new Event();
                    objectInsertion(event, resultSet);
                    Period period = new Period();
                    objectInsertion(period, resultSet);
                    if (last.equals(event)) {
                        last.addPeriod(period);
                    } else {
                        event.addPeriod(period);
                        events.add(event);
                        last = event;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }

        return events;
    }
}
