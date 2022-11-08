package services;

import exceptions.DBException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Timetable;

import java.util.Map;

public interface TimetablesService extends Service {

    Map<Timetable, Timetable.AccessRights> getTimetablesForUserId(Long userId) throws DBException, ServiceException, NullException;

}
