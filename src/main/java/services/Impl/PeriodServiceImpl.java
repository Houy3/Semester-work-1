package services.Impl;

import exceptions.*;
import models.Period;
import repositories.Inter.PeriodsRepository;
import services.Inter.PeriodService;
import services.ServiceImpl;

import java.util.Calendar;
import java.util.List;

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
            throw new IllegalArgumentException("End date cannot be less than start date. ");
        }

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(period.getStartTime());
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar1.add(Calendar.DAY_OF_MONTH, 1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(period.getEndTime());
        if (calendar1.compareTo(calendar2) < 0) {
            throw new IllegalArgumentException("The event start and end dates must be on the same day.");
        }

        period.setGroupId(periodsRepository.getNewGroupId());
        if (period.getRepeatability() == null) {
            super.add(period);

        } else {
            if (period.getRepeatabilityEndTime() == null) {
                throw new NullException("repeatabilityEndTime");
            }
            //обрезаю часы, минуты и тп, так как нужен только день
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(period.getRepeatabilityEndTime());
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            period.setRepeatabilityEndTime(calendar.getTime());


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

    @Override
    public List<Period> getByEventId(Long eventId) throws DBException, ServiceException, NullException {
        return periodsRepository.selectByEventId(eventId);
    }
}
