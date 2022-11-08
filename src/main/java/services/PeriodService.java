package services;

import exceptions.DBException;
import exceptions.NotFoundException;

public interface PeriodService extends Service {

    void deleteGroup(Long groupId) throws NotFoundException, DBException;

}
