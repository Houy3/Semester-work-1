package repositories;

import Exceptions.DBException;
import Exceptions.NotFoundException;
import Exceptions.SQLGeneratorException;
import jdbc.SQLAnnotations.*;
import jdbc.SQLAnnotations.Enum;
import jdbc.SQLGenerator;
import jdbc.queries.DeleteQuery;
import jdbc.queries.InsertQuery;
import jdbc.queries.UpdateQuery;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractRepository<T> implements Repository<T> {

    protected DataSource dataSource;

    public AbstractRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(T t) throws DBException {
        notNullCheck(t);

        InsertQuery insertQuery;
        try {
            insertQuery = SQLGenerator.insert(t.getClass());
        } catch (SQLGeneratorException e) {
            throw new DBException(e);
        }
        String query = insertQuery.getQuery();
        List<Field> fields = insertQuery.getFields();
        Field fieldId = insertQuery.getFieldId();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            parameterInsertion(t, fields, preparedStatement);

            if (fieldId.getAnnotation(Id.class).isInsertReturnId()) {
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    fieldId.setAccessible(true);
                    try {
                        fieldId.set(t, resultSet.getLong("id"));
                    } catch (IllegalAccessException ignored) {}
                    fieldId.setAccessible(false);

                } else {
                    throw new DBException("Аномалия: вставка расписания не произошла. ");
                }
                if (resultSet.next()) {
                    throw new DBException("Аномалия: произошло больше 1 вставки. ");
                }

            } else {
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows != 1) {
                    throw new DBException("Аномалия: изменилось " + affectedRows + " строк. ");
                }
            }

        } catch (SQLException e) {
            throw new DBException(e);
        }

    }

    @Override
    public void update(T t) throws NotFoundException, DBException {
        notNullCheck(t);
        //TODO

        UpdateQuery updateQuery;
        try {
            updateQuery = SQLGenerator.update(t.getClass());
        } catch (SQLGeneratorException e) {
            throw new DBException(e);
        }
        String query = updateQuery.getQuery();
        List<Field> fields = updateQuery.getFields();
        Field fieldId = updateQuery.getFieldId();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            parameterInsertion(t, fields, preparedStatement, fieldId);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException();
            }
            if (affectedRows != 1) {
                throw new DBException("Аномалия: изменилось " + affectedRows + " строк. ");
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void delete(T t) throws NotFoundException, DBException {
        //TODO

        DeleteQuery deleteQuery;
        try {
            deleteQuery = SQLGenerator.delete(t.getClass());
        } catch (SQLGeneratorException e) {
            throw new DBException(e);
        }
        String query = deleteQuery.getQuery();
        Field fieldId = deleteQuery.getFieldId();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            idInsertion(t, 1, preparedStatement, fieldId);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException();
            }
            if (affectedRows != 1) {
                throw new DBException("Аномалия: изменилось " + affectedRows + " строк. ");
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void select_by_id(T t) throws NotFoundException, DBException {
        //TODO
    }


    public void notNullCheck(T t)  throws DBException {
        if (t == null) {
            throw new DBException("Пустой объект. ");
        }
        for (Field field : t.getClass().getDeclaredFields()) {
            NotNull notNull = field.getAnnotation(NotNull.class);
            if (notNull == null) {
                continue;
            }

            field.setAccessible(true);
            try {
                Object obj = field.get(t);
                if (obj == null) {
                    throw new DBException("Необходимые поля не заполнены. ");
                }
            } catch (IllegalAccessException ignored) {
            }
            field.setAccessible(false);
        }
    }

    private void parameterInsertion(T t, List<Field> fields, PreparedStatement preparedStatement) throws SQLException, DBException {
        int i = 1;
        for (Field field : fields) {
            field.setAccessible(true);
            try {

                if (field.getAnnotation(Enum.class) != null) {
                    preparedStatement.setString(i++, field.get(t).toString());
                    continue;
                }

                switch (field.getType().getSimpleName()) {
                    case "Integer" -> preparedStatement.setInt(i++, (Integer) field.get(t));
                    case "Long" -> preparedStatement.setLong(i++, (Long) field.get(t));
                    case "Float" -> preparedStatement.setFloat(i++, (Float) field.get(t));
                    case "Double" -> preparedStatement.setDouble(i++, (Double) field.get(t));
                    case "Boolean" -> preparedStatement.setBoolean(i++, (Boolean) field.get(t));
                    case "String" -> preparedStatement.setString(i++, (String) field.get(t));
                    case "Date" -> preparedStatement.setTimestamp(i++, new Timestamp(((Date) field.get(t)).getTime()));
                    default -> throw new DBException("Неизвестный тип " + field.getType().getSimpleName()  + " у класса " + t.getClass().getName());
                }

            } catch (IllegalAccessException ignored) {}
            field.setAccessible(false);
        }
    }

    private void parameterInsertion(T t, List<Field> fields, PreparedStatement preparedStatement, Field fieldId) throws SQLException, DBException {
        parameterInsertion(t, fields, preparedStatement);
        idInsertion(t, fields.size() + 1, preparedStatement, fieldId);
    }

    private void idInsertion(T t, int where, PreparedStatement preparedStatement, Field fieldId) throws SQLException {
        fieldId.setAccessible(true);
        try {
            preparedStatement.setLong(where, (Long) fieldId.get(t));
        } catch (IllegalAccessException ignored) {}
        fieldId.setAccessible(false);
    }
}
