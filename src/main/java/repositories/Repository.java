package repositories;

import exceptions.*;
import models.Timetable;
import models.User;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface Repository {

    void insert(Object object) throws DBException, NullException, ServiceException, NotUniqueException;

    void update(Object object, Field uniqueField) throws NotFoundException, DBException, NullException, ServiceException, NotUniqueException;

    void delete(Object object, Field uniqueField) throws NotFoundException, DBException, NullException, ServiceException;

    void selectByUniqueField(Object object, Field uniqueField) throws NotFoundException, DBException, NullException, ServiceException;

    //----------------------------------------------------------

}
