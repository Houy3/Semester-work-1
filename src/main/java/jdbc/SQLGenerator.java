package jdbc;

import Exceptions.SQLGeneratorException;
import jdbc.SQLAnnotations.Column;
import jdbc.SQLAnnotations.Id;
import jdbc.SQLAnnotations.Table;
import jdbc.queries.DeleteQuery;
import jdbc.queries.InsertQuery;
import jdbc.queries.SelectByIdQuery;
import jdbc.queries.UpdateQuery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SQLGenerator {


    public static InsertQuery insert(Class tClass) throws SQLGeneratorException {
        StringBuilder query = new StringBuilder();

        query.append("insert into ").append(getTableName(tClass));

        ColumnsStorage columnsStorage = getColumns(tClass, Query.INSERT);
        List<Field> fields = columnsStorage.fields;
        Field fieldId = columnsStorage.fieldId;
        List<Column> columns = columnsStorage.columns;


        //вставка столбцов в запрос
        query.append("(");
        appendRepeat(", ", query, columns, 0);
        query.append(") values(");
        appendRepeat("?, ", query, columns.size(), 1);
        query.append(")");


        //вставка returning id
        if (fieldId == null) {
            throw new SQLGeneratorException("У модели " + tClass.getName() + " не найден айди. ");
        }
        if (fieldId.getAnnotation(Id.class).isInsertReturnId()) {
            query.append(" returning ").append(fieldId.getAnnotation(Column.class).name());
        }

        return new InsertQuery(query.toString(), fields, fieldId);
    }

    public static UpdateQuery update(Class tClass) throws SQLGeneratorException {
        StringBuilder query = new StringBuilder();

        query.append("update ").append(getTableName(tClass)).append(" set ");

        ColumnsStorage columnsStorage = getColumns(tClass, Query.NOT_INSERT);
        List<Field> fields = columnsStorage.fields;
        Field fieldId = columnsStorage.fieldId;
        List<Column> columns = columnsStorage.columns;

        appendRepeat(" = ?, ", query, columns, 4);
        query.append(" where ").append(fieldId.getAnnotation(Column.class).name())
                .append(" = ?");

        return new UpdateQuery(query.toString(), fields, fieldId);
    }

    public static DeleteQuery delete(Class tClass) throws SQLGeneratorException {
        StringBuilder query = new StringBuilder();

        Field fieldId = getFieldId(tClass);
        query.append("delete from ").append(getTableName(tClass))
                .append(" where ").append(fieldId.getAnnotation(Column.class).name()).append(" = ?");

        return new DeleteQuery(query.toString(), fieldId);
    }

    public static SelectByIdQuery selectById(Class tClass) throws SQLGeneratorException {
        StringBuilder query = new StringBuilder();

        query.append("select ");

        ColumnsStorage columnsStorage = getColumns(tClass, Query.NOT_INSERT);
        List<Field> fields = columnsStorage.fields;
        Field fieldId = columnsStorage.fieldId;
        List<Column> columns = columnsStorage.columns;

        appendRepeat(", ", query, columns, 0);

        query.append(" from ").append(getTableName(tClass))
                .append(" where ").append(fieldId.getAnnotation(Column.class).name()).append(" = ?");

        return new SelectByIdQuery(query.toString(), fields, fieldId);
    }





    private static void appendRepeat(String syn, StringBuilder query, List<Column> from, int leaveLeft) {
        from.forEach(column -> query.append(column.name()).append(syn));
        query.delete(query.lastIndexOf(syn) + leaveLeft,query.lastIndexOf(syn) + syn.length());
    }

    private static void appendRepeat(String syn, StringBuilder query, int count, int leaveLeft) {
        query.append(syn.repeat(count));
        query.delete(query.lastIndexOf(syn) + leaveLeft,query.lastIndexOf(syn) + syn.length());
    }

    private static String getTableName(Class tClass) throws SQLGeneratorException {
        Table table = (Table) tClass.getAnnotation(Table.class);
        if (table == null) {
            throw new SQLGeneratorException("У модели " + tClass.getName() + " не заполнено имя таблицы. ");
        }
        return table.name();
    }

    private static Field getFieldId(Class tClass) throws SQLGeneratorException {
        Field fieldId = null;
        for (Field field : tClass.getDeclaredFields()) {
            if (field.getAnnotation(Column.class) == null) {
                continue;
            }

            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                if (fieldId != null) {
                    throw new SQLGeneratorException("У модели " + tClass.getName() + " найдено несколько айди. ");
                }
                fieldId = field;
            }
        }
        return fieldId;
    }

    private static ColumnsStorage getColumns(Class tClass, Query query) throws SQLGeneratorException {

        List<Field> fields = new ArrayList<>();
        Field fieldId = null;
        List<Column> columns = new ArrayList<>();
        for (Field field : tClass.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                continue;
            }

            Id id = field.getAnnotation(Id.class);
            if (id == null || (id.isInsertIncludeId() && query == Query.INSERT)) {
                columns.add(column);
                fields.add(field);
            }

            if (id != null) {
                if (fieldId != null) {
                    throw new SQLGeneratorException("У модели " + tClass.getName() + " найдено несколько айди. ");
                }
                fieldId = field;
            }
        }

        if (columns.isEmpty()) {
            throw new SQLGeneratorException("У модели " + tClass.getName() + " не заполнены столбцы. ");
        }

        return new ColumnsStorage(fields, fieldId, columns);
    }


    private enum Query {
        INSERT,
        NOT_INSERT
    }

    private static class ColumnsStorage {
        List<Field> fields;
        Field fieldId;
        List<Column> columns;

        public ColumnsStorage(List<Field> fields, Field fieldId, List<Column> columns) {
            this.fields = fields;
            this.fieldId = fieldId;
            this.columns = columns;
        }
    }
}
