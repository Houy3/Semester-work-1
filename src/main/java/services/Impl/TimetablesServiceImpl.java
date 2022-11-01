package services.Impl;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import models.Timetable;
import models.User;
import repositories.AbstractRepository;
import repositories.Repository;
import repositories.TimetablesRepository;
import services.AbstractService;
import services.TimetablesService;

import java.util.Map;

public class TimetablesServiceImpl extends AbstractService<Timetable> implements TimetablesService {

    TimetablesRepository timetablesRepository;

    public TimetablesServiceImpl(AbstractRepository<Timetable> repository) {
        super(repository);
        this.timetablesRepository = (TimetablesRepository) repository;
    }

    @Override
    public void addAccessRights(Timetable timetable, User user, Timetable.AccessRights accessRights) throws NotFoundException, DBException {
        timetablesRepository.insert_access_rights(timetable, user, accessRights);
    }

    @Override
    public void changeAccessRights(Timetable timetable, User user, Timetable.AccessRights accessRights) throws NotFoundException, DBException {
        timetablesRepository.update_access_rights(timetable, user, accessRights);
    }

    @Override
    public void deleteAccessRights(Timetable timetable, User user, Timetable.AccessRights accessRights) throws NotFoundException, DBException {
        timetablesRepository.delete_access_rights(timetable, user);
    }

    @Override
    public Map<User, Timetable.AccessRights> getUsersIdForTimetable(Timetable timetable) throws DBException {
        return timetablesRepository.select_users_id_by_timetable(timetable);
    }

    @Override
    public Map<Timetable, Timetable.AccessRights> getTimetablesForUser(User user) throws DBException {
        return timetablesRepository.select_timetables_by_user(user);
    }
}
