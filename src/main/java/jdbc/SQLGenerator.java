package jdbc;

import exceptions.SQLGeneratorException;
import jdbc.SQLAnnotations.Column;
import jdbc.SQLAnnotations.Table;
import jdbc.SQLAnnotations.Unique;
import jdbc.queries.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SQLGenerator {


    public static InsertQuery insert(Class tClass, Field uniqueField) throws SQLGeneratorException {
        uniqueFieldCheck(tClass, uniqueField);

        StringBuilder query = new StringBuilder();
        query.append("insert into ").append(getTableName(tClass));

        List<Field> fields = getColumnFields(tClass);
        List<Field> uniqueFields = getUniqueColumnFields(tClass, uniqueField);
        if (!((Table) tClass.getAnnotation(Table.class)).isInsertIncludeUniqueField()) {
            fields.removeAll(uniqueFields);
        }
        List<Column> columns = getColumns(fields);

        //вставка столбцов в запрос
        query.append("(");
        appendRepeat(", ", query, columns, 0);
        query.append(") values(");
        appendRepeat("?, ", query, columns.size(), 1);
        query.append(")");

        //вставка returning id

        query.append(" returning ");
        appendRepeat(", ", query, getColumns(uniqueFields), 0);


        return new InsertQuery(query.toString(), fields, uniqueFields);
    }

    public static UpdateQuery update(Class tClass, Field uniqueField) throws SQLGeneratorException {
        uniqueFieldCheck(tClass, uniqueField);

        StringBuilder query = new StringBuilder();
        query.append("update ").append(getTableName(tClass)).append(" set ");

        List<Field> fields = getColumnFields(tClass);
        List<Field> uniqueFields = getUniqueColumnFields(tClass, uniqueField);

        appendRepeat(" = ?, ", query, getColumns(fields), 4);
        query.append(" where ");
        appendRepeat(" = ? and ", query, getColumns(uniqueFields), 4);

        return new UpdateQuery(query.toString(), fields);
    }

    public static DeleteQuery delete(Class tClass, Field uniqueField) throws SQLGeneratorException {
        StringBuilder query = new StringBuilder();

        List<Field> uniqueFields = getUniqueColumnFields(tClass, uniqueField);

        query.append("delete from ").append(getTableName(tClass)).append(" where ");
        appendRepeat(" = ? and ", query, getColumns(uniqueFields), 4);

        return new DeleteQuery(query.toString(), uniqueFields);
    }

    public static SelectByIdQuery selectByUniqueField(Class tClass, Field uniqueField) throws SQLGeneratorException {
        uniqueFieldCheck(tClass, uniqueField);
        StringBuilder query = new StringBuilder();

        query.append("select * from ").append(getTableName(tClass)).append(" where ");

        List<Field> fields = getColumnFields(tClass);
        List<Field> uniqueFields = getUniqueColumnFields(tClass, uniqueField);

        appendRepeat(" = ? and ", query, getColumns(uniqueFields), 4);

        return new SelectByIdQuery(query.toString(), fields, uniqueFields);
    }

    public static Optional<SelectUniqueCheck> selectUniqueCheck(Class tClass) throws SQLGeneratorException {
        if (tClass == null) {
            throw new SQLGeneratorException("Класс не передан. ");
        }

        StringBuilder query = new StringBuilder();

        query.append("select * from ").append(getTableName(tClass)).append(" where ");;

        Set<Integer> groupsIn = new HashSet<>();

        List<Field> uniqueFields = new ArrayList<>();
        for (Field field : getUniqueColumnFields(tClass)) {
            int group = field.getAnnotation(Unique.class).group();
            if (group == -1) {
                query.append(field.getAnnotation(Column.class).name()).append(" = ? or ");
                uniqueFields.add(field);
                continue;
            }
            if (groupsIn.contains(group)) {
                continue;
            }
            List<Field> uniqueFieldsInOneGroup = getUniqueColumnFields(tClass, field);
            query.append("(");
            appendRepeat(" = ? and ", query, getColumns(uniqueFieldsInOneGroup), 4);
            query.append(") or ");
            groupsIn.add(group);
            uniqueFields.addAll(uniqueFieldsInOneGroup);
        }
        query.delete(query.length() - 4, query.length());

        if (uniqueFields.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new SelectUniqueCheck(query.toString(), uniqueFields));
    }

    private static void appendRepeat(String syn, StringBuilder query, List<Column> from, int leaveLeft) {
        from.forEach(column -> query.append(column.name()).append(syn));
        query.delete(query.lastIndexOf(syn) + leaveLeft,query.lastIndexOf(syn) + syn.length());
    }

    private static void appendRepeat(String syn, StringBuilder query, int count, int leaveLeft) {
        query.append(syn.repeat(count));
        query.delete(query.lastIndexOf(syn) + leaveLeft,query.lastIndexOf(syn) + syn.length());
    }

    /** Возвращает имя таблицы **/
    private static String getTableName(Class tClass) throws SQLGeneratorException {
        Table table = (Table) tClass.getAnnotation(Table.class);
        if (table == null) {
            throw new SQLGeneratorException("У модели " + tClass.getName() + " не заполнено имя таблицы. ");
        }
        return table.name();
    }

    /** Возвращает все поля, помеченные аннотацией Column **/
    private static List<Field> getColumnFields(Class tClass) throws SQLGeneratorException {

        List<Field> fields = Arrays.stream(tClass.getDeclaredFields())
                .filter(field -> field.getAnnotation(Column.class) != null)
                .collect(Collectors.toList());

        if (fields.isEmpty()) {
            throw new SQLGeneratorException("У модели " + tClass.getName() + " не заполнены столбцы. ");
        }

        return fields;
    }

    /** Для каждого поля возвращает аннотацию Column **/
    private static List<Column> getColumns(List<Field> fields) {
        return fields.stream().map(f -> f.getAnnotation(Column.class)).collect(Collectors.toList());
    }

    /** Возвращает все поля, помеченные аннотациями Column и Unique, учитывая group() в Unique **/
    private static List<Field> getUniqueColumnFields(Class tClass) throws SQLGeneratorException {
        return getColumnFields(tClass).stream()
                .filter(f -> f.getAnnotation(Unique.class) != null)
                .collect(Collectors.toList());
    }

    /** Возвращает все поля, помеченные аннотациями Column и Unique, в той же group() для Unique **/
    private static List<Field> getUniqueColumnFields(Class tClass, Field uniqueField) throws SQLGeneratorException {
        List<Field> uniqueFields = new ArrayList<>();

        int group = uniqueField.getAnnotation(Unique.class).group();
        if (group == -1) {
            uniqueFields.add(uniqueField);
        } else {
            for (Field field : getColumnFields(tClass)) {
                if (field.getAnnotation(Unique.class) == null) {
                    continue;
                }
                if (field.getAnnotation(Unique.class).group() == group) {
                    uniqueFields.add(field);
                }
            }
        }

        return uniqueFields;
    }


    /** Проверка, что класс не null
                , что уникальное поле не null
                , что уникальное поле принадлежит объекту
                и что уникальное поле помечено аннотациями Column или Unique */
    private static void uniqueFieldCheck(Class tClass, Field uniqueField) throws SQLGeneratorException {
        if (tClass == null) {
            throw new SQLGeneratorException("Класс не передан. ");
        }
        if (uniqueField == null) {
            throw new SQLGeneratorException("Поле не передано. ");
        }
        if (!List.of(tClass.getDeclaredFields()).contains(uniqueField) ) {
            throw new SQLGeneratorException("Поле " + uniqueField.getName() + " у объекта " + tClass.getName() + " не найдено." );
        }
        //аннотацией " + Unique.class.getName() + ". "
        if (uniqueField.getAnnotation(Column.class) == null) {
            throw new SQLGeneratorException("Поле " + uniqueField.getName() + " у объекта " + tClass.getName() + " не помечено аннотацией " + Column.class.getName() + ". ");
        }
        if (uniqueField.getAnnotation(Unique.class) == null) {
            throw new SQLGeneratorException("Поле " + uniqueField.getName() + " у объекта " + tClass.getName() + " не помечено аннотацией " + Unique.class.getName() + ". ");
        }
    }


}
