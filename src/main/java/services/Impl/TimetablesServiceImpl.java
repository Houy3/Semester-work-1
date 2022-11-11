package services.Impl;

import exceptions.DBException;
import exceptions.NullException;
import exceptions.ServiceException;
import models.Timetable;
import repositories.Inter.TimetablesRepository;
import services.Inter.TimetablesService;
import services.ServiceImpl;

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
