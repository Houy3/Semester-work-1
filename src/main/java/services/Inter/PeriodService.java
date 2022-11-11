package services.Inter;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Period;
import services.Service;

import java.util.List;

public interface PeriodService extends Service {

    void deleteGroup(Long groupId) throws NotFoundException, DBException;

    List<Period> getByEventId(Long eventId) throws DBException, ServiceException, NullException;

}
