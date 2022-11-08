package services.Impl;

import exceptions.*;
import models.Period;
import repositories.PeriodsRepository;
import repositories.Repository;
import services.PeriodService;

import java.util.Calendar;

public class PeriodServiceImpl extends ServiceImpl implements PeriodService {

    PeriodsRepository periodsRepository;

    public PeriodServiceImpl(PeriodsRepository periodsRepository) {
        super(periodsRepository);
        this.periodsRepository = periodsRepository;
    }

    @Override
    public void add(Object object) throws NotUniqueException, NullException, ServiceException, DBException {
        Period period;
        try {
            period = (Period) object;
        } catch (Exception e) {
            throw new ServiceException(e);
        }

        if (period.getStartTime().compareTo(period.getEndTime()) > 0) {
            throw new ServiceException("Дата окончания меньше даты начала. ");
        }

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(period.getStartTime());
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
        calendar1.add(Calendar.DAY_OF_MONTH, 1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(period.getEndTime());
        if (calendar1.compareTo(calendar2) >= 0) {
            throw new ServiceException("Даты начала и окончания события должны быть в одном дне.");
        }

        period.setGroupId(periodsRepository.getNewGroupId());
        if (period.getRepeatability() == null) {
            super.add(period);

        } else {
            if (period.getRepeatabilityEndTime() == null) {
                throw new ServiceException("У объекта " + Period.class.getName() + " не задано ограничение на повтор. ");
            }
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(period.getStartTime());
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(period.getEndTime());

            while (period.getEndTime().compareTo(period.getRepeatabilityEndTime()) <= 0) {
                super.add(period);
                switch (period.getRepeatability()) {
                    case DAY -> {calendarStart.add(Calendar.DAY_OF_MONTH, 1);
                        calendarEnd.add(Calendar.DAY_OF_MONTH, 1);}
                    case WEEK -> {calendarStart.add(Calendar.WEEK_OF_YEAR, 1);
                        calendarEnd.add(Calendar.WEEK_OF_YEAR, 1);}
                    case MONTH -> {calendarStart.add(Calendar.MONTH, 1);
                        calendarEnd.add(Calendar.MONTH, 1);}
                    case YEAR -> {calendarStart.add(Calendar.YEAR, 1);
                        calendarEnd.add(Calendar.YEAR, 1);}
                    default -> throw new ServiceException("У модели " + Period.class.getName() + " обнаружена неизвестная enum " + period.getRepeatability().name() + ". ");
                }
                period.setStartTime(calendarStart.getTime());
                period.setEndTime(calendarEnd.getTime());
            }
        }
    }


    @Override
    public void deleteGroup(Long groupId) throws NotFoundException, DBException {
        periodsRepository.deleteGroup(groupId);
    }
}
