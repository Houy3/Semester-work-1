package repositories.Inter;

import exceptions.DBException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Timetable;
import repositories.Repository;

import java.util.Map;

public interface TimetablesRepository extends Repository {

    Map<Timetable, Timetable.AccessRights> selectTimetablesByUserId(Long user_id) throws DBException, NullException, ServiceException;

}
