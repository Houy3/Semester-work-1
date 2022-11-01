package repositories;

import exceptions.*;
import jdbc.SQLAnnotations.*;
import jdbc.SQLGenerator;
import jdbc.queries.*;
import models.Timetable;
import models.User;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

public class RepositoryImpl implements Repository {

    protected DataSource dataSource;

    public RepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Object object) throws DBException, NotNullException {
        notNullCheck(object, NotNull.class);

        InsertQuery insertQuery;
        try {
            insertQuery = SQLGenerator.insert(object.getClass());
        } catch (SQLGeneratorException e) {
            throw new DBException(e);
        }
        String query = insertQuery.getQuery();
        List<Field> fields = insertQuery.getFields();
        List<Field> fieldsId = insertQuery.getFieldsId();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            parameterInsertion(object, fields, preparedStatement);

            if (!fieldsId.get(0).getAnnotation(Id.class).isInsertIncludeId()) {
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    for (Field fieldId : fieldsId) {
                        setValueTo(object, fieldId, resultSet.getLong("id"));
                    }
                } else {
                    throw new DBException("Аномалия: вставка " + object.getClass().getName() + " не произошла. ");
                }
                if (resultSet.next()) {
                    throw new DBException("Аномалия: произошло больше 1 вставки " + object.getClass().getName() + ". ");
                }

            } else {
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows != 1) {
                    throw new DBException("Аномалия: изменилось " + affectedRows + " строк при вставке " + object.getClass().getName() + ". ");
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void update(Object object) throws NotFoundException, DBException, NotNullException {
        notNullCheck(object, NotNull.class);
        notNullCheck(object, Id.class);

        UpdateQuery updateQuery;
        try {
            updateQuery = SQLGenerator.update(object.getClass());
        } catch (SQLGeneratorException e) {
            throw new DBException(e);
        }
        String query = updateQuery.getQuery();
        List<Field> fields = updateQuery.getFields();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            parameterInsertion(object, fields, preparedStatement);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException();
            }
            if (affectedRows != 1) {
                throw new DBException("Аномалия: изменилось " + affectedRows + " строк при изменении " + object.getClass().getName() + ". ");
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void delete(Object object) throws NotFoundException, DBException, NotNullException {
        notNullCheck(object, Id.class);

        DeleteQuery deleteQuery;
        try {
            deleteQuery = SQLGenerator.delete(object.getClass());
        } catch (SQLGeneratorException e) {
            throw new DBException(e);
        }
        String query = deleteQuery.getQuery();
        List<Field> fieldsId = deleteQuery.getFieldsId();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            parameterInsertion(object, fieldsId, preparedStatement);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException();
            }
            if (affectedRows != 1) {
                throw new DBException("Аномалия: изменилось " + affectedRows + " строк при удалении " + object.getClass().getName() + ". ");
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void select_by_id(Object object) throws NotFoundException, DBException, NotNullException {
        notNullCheck(object, Id.class);

        SelectByIdQuery selectByIdQuery;
        try {
            selectByIdQuery = SQLGenerator.selectById(object.getClass());
        } catch (SQLGeneratorException e) {
            throw new DBException(e);
        }
        String query = selectByIdQuery.getQuery();
        List<Field> fields = selectByIdQuery.getFields();
        List<Field> fieldsId = selectByIdQuery.getFieldsId();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            parameterInsertion(object, fieldsId, preparedStatement);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    dataInsertion(object, fields, resultSet);
                } else {
                    throw new NotFoundException();
                }
                if (resultSet.next()) {
                    throw new DBException("Аномалия: было найдено несколько " + object.getClass().getName() + ". ");
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void uniqueCheck(Object object) throws NotUniqueException, DBException, NotNullException {
        notNullCheck(object, Unique.class);

        SelectUniqueCheck selectUniqueCheck;
        try {
            Optional<SelectUniqueCheck> selectUniqueCheck2 = SQLGenerator.selectUniqueCheck(object.getClass());
            if (selectUniqueCheck2.isPresent()) {
                selectUniqueCheck = selectUniqueCheck2.get();
            } else {
                return;
            }
        } catch (SQLGeneratorException e) {
            throw new DBException(e);
        }
        String query = selectUniqueCheck.getQuery();
        List<Field> fields = selectUniqueCheck.getFields();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            parameterInsertion(object, fields, preparedStatement);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    for (Field field : fields) {
                        if (getValueFrom(object,field).equals(resultSet.getObject(field.getAnnotation(Column.class).name()))) {
                            throw new NotUniqueException(field.getName());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
    //-------------------------------------------------------

    //User
    private static final String SELECT_ID_AND_ACCESS_RIGHTS_FROM_USERS_BY_EMAIL_AND_PASSWORD =
            "select * from users u where u.email = ? and u.password_hash = ?";

    public User select_user_by_email_and_password(String email, String password) throws DBException, NotFoundException, NotNullException {
        if (email == null) {
            throw new NotNullException("email");
        }
        if (password == null) {
            throw new NotNullException("password");
        }

        User user = new User();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_AND_ACCESS_RIGHTS_FROM_USERS_BY_EMAIL_AND_PASSWORD)) {

            int i = 1;
            preparedStatement.setString(i++, email);
            preparedStatement.setString(i++, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    List<Field> fields = Arrays.stream(user.getClass().getDeclaredFields()).filter(f -> f.getAnnotation(Column.class) != null).collect(Collectors.toList());
                    dataInsertion(user, fields, resultSet);
                } else {
                    throw new NotFoundException();
                }
                if (resultSet.next()) {
                    throw new DBException("Аномалия: было найдено несколько пользователей при аутентификации. ");
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return user;
    }

    private static final String SELECT_ALL_FROM_USERS =
            "SELECT * FROM users";
    public List<User> select_all_users() throws DBException {
        List<User> users = new ArrayList<>();
        List<Field> fields = Arrays.stream(User.class.getDeclaredFields()).filter(f -> f.getAnnotation(Column.class) != null).collect(Collectors.toList());

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FROM_USERS)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User();
                    dataInsertion(user, fields, resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }

        return users;
    }

    //Timetable
    private static final String SELECT_ALL_USERS_ID_BY_TIMETABLE =
            "select user_id, access_rights from users_timetables where timetable_id = ?";

    @Override
    public Map<User, Timetable.AccessRights> select_users_id_by_timetable(Timetable timetable) throws DBException {
//        if (timetable.getId() == null) {
//
//            throw new DBException("Необходимые поля не заполнены");
//        }

        Map<User, Timetable.AccessRights> users = new HashMap<>();

//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS_ID_BY_TIMETABLE)) {
//
//            int i = 1;
//            preparedStatement.setLong(i++, timetable.getId());
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
//                    User user = new User();
//                    user.setId(resultSet.getLong("user_id"));
//                    Timetable.AccessRights accessRights;
//                    try {
//                        accessRights =  Timetable.AccessRights.valueOf(resultSet.getString("access_rights"));
//                    } catch (IllegalArgumentException e) {
//                        throw new DBException("Неизвестные для модели " + Timetable.class.getName() + " права доступа на таблицу: " + resultSet.getString("access_rights"));
//                    }
//                    users.put(user, accessRights);
//                }
//            }
//        } catch (SQLException e) {
//            throw new DBException(e);
//        }

        return users;
    }

    private static final String SELECT_ALL_TIMETABLES_BY_USER =
            "select timetable_id, access_rights from users_timetables where user_id = ?";

    @Override
    public Map<Timetable, Timetable.AccessRights> select_timetables_by_user(User user) throws DBException {
        if (user.getId() == null) {

            throw new DBException("Необходимые поля не заполнены");
        }

        Map<Timetable, Timetable.AccessRights> timetables = new HashMap<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TIMETABLES_BY_USER)) {

            int i = 1;
            preparedStatement.setLong(i++, user.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Timetable timetable = new Timetable();
                    timetable.setId(resultSet.getLong("timetable_id"));
                    Timetable.AccessRights accessRights;
                    try {
                        accessRights = Timetable.AccessRights.valueOf(resultSet.getString("access_rights"));
                    } catch (IllegalArgumentException e) {
                        throw new DBException("Неизвестные для модели " + Timetable.class.getName() + " права доступа на таблицу: " + resultSet.getString("access_rights"));
                    }
                    timetables.put(timetable, accessRights);
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }

        for (Timetable timetable : timetables.keySet()) {
            try {
                select_by_id(timetable);
            } catch (NotFoundException e) {
                throw new DBException("Аномалия: на несуществующую таблицу завязаны пользователи. ");
            } catch (NotNullException e) {
                throw new DBException("Аномалия: на таблицу не привязался айди. ");
            }
        }

        return timetables;
    }








    //-------------------------------------------------------
    private void dataInsertion(Object object, List<Field> fields, ResultSet resultSet) throws SQLException, DBException {
        for (Field field : fields) {

            if (field.getType().isEnum()) {
                boolean flag = true;
                for (Method method : object.getClass().getMethods()) {
                    EnumSetter enumSetter = method.getAnnotation(EnumSetter.class);
                    if (enumSetter == null) {
                        continue;
                    }
                    if (enumSetter.field_name().equals(field.getName())) {
                        String value = resultSet.getString(field.getAnnotation(Column.class).name());
                        try {
                            method.setAccessible(true);
                            method.invoke(object, value);
                            method.setAccessible(false);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            throw new DBException("Неизвестная для модели " + object.getClass().getName() + " enum " + value);
                        }
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    throw new DBException("Для модели" + object.getClass().getName() + " не найден " + EnumSetter.class.getName() + " на поле " + field.getName());
                }
                continue;
            }

            String columnName = field.getAnnotation(Column.class).name();
            switch (field.getType().getSimpleName()) {
                case "Integer" -> setValueTo(object, field, resultSet.getInt(columnName));
                case "Long" -> setValueTo(object, field, resultSet.getLong(columnName));
                case "Float" -> setValueTo(object, field, resultSet.getFloat(columnName));
                case "Double" -> setValueTo(object, field, resultSet.getDouble(columnName));
                case "Boolean" -> setValueTo(object, field, resultSet.getBoolean(columnName));
                case "String" -> setValueTo(object, field, resultSet.getString(columnName));
                case "Date" -> setValueTo(object, field, new Date(resultSet.getTimestamp(columnName).getTime())) ;
                default -> throw new DBException("Неизвестный тип " + field.getType().getSimpleName()  + " у класса " + object.getClass().getName());
            }

        }
    }

    private void notNullCheck(Object object, Class annotation) throws DBException, NotNullException {
        if (object == null) {
            throw new DBException("Пустой объект. ");
        }

        for (Field field : Arrays.stream(object.getClass().getDeclaredFields())
                                 .filter(f -> f.getAnnotation(Column.class) != null && f.getAnnotation(annotation) != null)
                                 .collect(Collectors.toList())) {
            if (getValueFrom(object, field) == null) {
                throw new NotNullException(field.getName());
            }
        }

    }

    protected void parameterInsertion(Object object, List<Field> fields, PreparedStatement preparedStatement) throws SQLException, DBException {
        int i = 1;
        for (Field field : fields) {

            if (field.getType().isEnum()) {
                preparedStatement.setString(i++, getValueFrom(object, field).toString());
                continue;
            }

            switch (field.getType().getSimpleName()) {
                case "Integer" -> preparedStatement.setInt(i++, (Integer) getValueFrom(object, field));
                case "Long" -> preparedStatement.setLong(i++, (Long) getValueFrom(object, field));
                case "Float" -> preparedStatement.setFloat(i++, (Float) getValueFrom(object, field));
                case "Double" -> preparedStatement.setDouble(i++, (Double) getValueFrom(object, field));
                case "Boolean" -> preparedStatement.setBoolean(i++, (Boolean) getValueFrom(object, field));
                case "String" -> preparedStatement.setString(i++, (String) getValueFrom(object, field));
                case "Date" -> preparedStatement.setTimestamp(i++, new Timestamp(((Date) getValueFrom(object, field)).getTime()));
                default -> throw new DBException("Неизвестный тип " + field.getType().getSimpleName()  + " у класса " + object.getClass().getName());
            }
        }
    }

    private Object getValueFrom(Object object, Field field) throws DBException {
        Object value;
        if (field.canAccess(object)) {
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                throw new DBException(e);
            }
        } else {
            field.setAccessible(true);
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                throw new DBException(e);
            }
            field.setAccessible(false);
        }
        return value;
    }

    private void setValueTo(Object object, Field field, Object value) throws DBException {
        if (field.canAccess(object)) {
            try {
                field.set(object, value);
            } catch (IllegalAccessException e) {
                throw new DBException(e);
            }
        } else {
            field.setAccessible(true);
            try {
                field.set(object, value);
            } catch (IllegalAccessException e) {
                throw new DBException(e);
            }
            field.setAccessible(false);
        }
    }
}
