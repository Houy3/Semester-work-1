package repositories;

import exceptions.DBException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Timetable;

import java.util.Map;

public interface TimetablesRepository extends Repository {

    Map<Timetable, Timetable.AccessRights> selectTimetablesByUserId(Long user_id) throws DBException, NullException, ServiceException;

}
