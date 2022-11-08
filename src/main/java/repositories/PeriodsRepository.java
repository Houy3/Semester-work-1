package repositories;

import exceptions.DBException;
import exceptions.NotFoundException;

public interface PeriodsRepository extends Repository {

    void deleteGroup(Long groupId) throws NotFoundException, DBException;

    Long getNewGroupId() throws DBException;
}
