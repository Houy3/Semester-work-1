package repositories.Inter;

import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Period;
import repositories.Repository;

import java.util.List;

public interface PeriodsRepository extends Repository {

    void deleteGroup(Long groupId) throws NotFoundException, DBException;

    Long getNewGroupId() throws DBException;

    List<Period> selectByEventId(Long eventId) throws NullException, ServiceException, DBException;
}
