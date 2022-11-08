package services.Impl;

import exceptions.DBException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Timetable;
import repositories.Repository;
import repositories.TimetablesRepository;
import services.TimetablesService;

import java.util.Map;

public class TimetablesServiceImpl extends ServiceImpl implements TimetablesService {

    TimetablesRepository timetablesRepository;

    public TimetablesServiceImpl(TimetablesRepository timetablesRepository) {
        super(timetablesRepository);
        this.timetablesRepository =  timetablesRepository;
    }

    @Override
    public Map<Timetable, Timetable.AccessRights> getTimetablesForUserId(Long userId) throws DBException, ServiceException, NullException {
        return timetablesRepository.selectTimetablesByUserId(userId);
    }

}
