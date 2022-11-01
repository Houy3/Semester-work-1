package jdbc;

import exceptions.SQLGeneratorException;
import jdbc.SQLAnnotations.Column;
import jdbc.SQLAnnotations.Id;
import jdbc.SQLAnnotations.Table;
import jdbc.SQLAnnotations.Unique;
import jdbc.queries.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SQLGenerator {


    public static InsertQuery insert(Class tClass) throws SQLGeneratorException {
        StringBuilder query = new StringBuilder();

        query.append("insert into ").append(getTableName(tClass));


        List<Field> fields = getColumnFieldsWithoutIdFields(tClass);
        List<Field> fieldsId = getFieldsId(tClass);
        Id mainId = fieldsId.get(0).getAnnotation(Id.class);
        if (mainId.isInsertIncludeId()) {
            fields.addAll(0, fieldsId);
        }
        List<Column> columns = getColumns(fields);

        //вставка столбцов в запрос
        query.append("(");
        appendRepeat(", ", query, columns, 0);
        query.append(") values(");
        appendRepeat("?, ", query, columns.size(), 1);
        query.append(")");

        //вставка returning id
        if (!mainId.isInsertIncludeId()) {
            query.append(" returning ");
            appendRepeat(", ", query, getColumns(fieldsId), 0);
        }

        return new InsertQuery(query.toString(), fields, fieldsId);
    }

    public static UpdateQuery update(Class tClass) throws SQLGeneratorException {
        StringBuilder query = new StringBuilder();

        query.append("update ").append(getTableName(tClass)).append(" set ");


        List<Field> fields = getColumnFieldsWithoutIdFields(tClass);
        List<Field> fieldsId = getFieldsId(tClass);

        appendRepeat(" = ?, ", query, getColumns(fields), 4);
        query.append(" where ");
        appendRepeat(" = ? and ", query, getColumns(fieldsId), 4);

        fields.addAll(fieldsId);
        return new UpdateQuery(query.toString(), fields);
    }

    public static DeleteQuery delete(Class tClass) throws SQLGeneratorException {
        StringBuilder query = new StringBuilder();

        List<Field> fieldsId = getFieldsId(tClass);
        query.append("delete from ").append(getTableName(tClass)).append(" where ");
        appendRepeat(" = ? and ", query, getColumns(fieldsId), 4);

        return new DeleteQuery(query.toString(), fieldsId);
    }

    public static SelectByIdQuery selectById(Class tClass) throws SQLGeneratorException {
        StringBuilder query = new StringBuilder();

        query.append("select ");

        List<Field> fields = getColumnFieldsWithoutIdFields(tClass);
        List<Field> fieldsId = getFieldsId(tClass);

        appendRepeat(", ", query, getColumns(fields), 0);

        query.append(" from ").append(getTableName(tClass)).append(" where ");
        appendRepeat(" = ? and ", query, getColumns(fieldsId), 4);

        return new SelectByIdQuery(query.toString(), fields, fieldsId);
    }

    public static Optional<SelectUniqueCheck> selectUniqueCheck(Class tClass) throws SQLGeneratorException {
        StringBuilder query = new StringBuilder();

        query.append("select * from ").append(getTableName(tClass)).append(" where ");;

        List<Field> fields =  getUniqueColumnFields(tClass);
        List<Column> columns = getColumns(fields);

        if (columns.isEmpty()) {
            return Optional.empty();
        } else {
            appendRepeat(" = ? or ", query, columns, 4);
        }

        return Optional.of(new SelectUniqueCheck(query.toString(), fields));
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

    private static List<Field> getFieldsId(Class tClass) throws SQLGeneratorException {

        List<Field> fieldsId = Arrays.stream(tClass.getDeclaredFields())
         .filter(field -> field.getAnnotation(Column.class) != null && field.getAnnotation(Id.class) != null)
         .collect(Collectors.toList());

        if (fieldsId.isEmpty()) {
            throw new SQLGeneratorException("У модели " + tClass.getName() + " не найдено айди. ");
        }
        if (fieldsId.size() > 1) {
            throw new SQLGeneratorException("У модели " + tClass.getName() + " найдено несколько айди. ");
        }

        String secondIdFieldName = fieldsId.get(0).getAnnotation(Id.class).secondIdField();
        if (!secondIdFieldName.equals("")) {
            try {
                fieldsId.add(tClass.getDeclaredField(secondIdFieldName));
            } catch (NoSuchFieldException e) {
                throw new SQLGeneratorException("У модели " + tClass.getName() + " второе айди помечено неверно. ");
            }
        }

        return fieldsId;
    }

    private static List<Field> getColumnFieldsWithoutIdFields(Class tClass) throws SQLGeneratorException {

        List<Field> fields = Arrays.stream(tClass.getDeclaredFields())
                .filter(field -> field.getAnnotation(Column.class) != null)
                .collect(Collectors.toList());

        if (fields.isEmpty()) {
            throw new SQLGeneratorException("У модели " + tClass.getName() + " не заполнены столбцы. ");
        }

        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                fields.remove(field);
                if (!id.secondIdField().equals("")) {
                    try {
                        fields.remove(tClass.getDeclaredField(id.secondIdField()));
                    } catch (NoSuchFieldException e) {
                        throw new SQLGeneratorException("У модели " + tClass.getName() + " неправильно заполнен второй айди. ");
                    }
                }
                break;
            }
        }

        return fields;
    }

    private static List<Column> getColumns(List<Field> fields) {
        return fields.stream().map(f -> f.getAnnotation(Column.class)).collect(Collectors.toList());
    }

    private static List<Field> getUniqueColumnFields(Class tClass) throws SQLGeneratorException {
        return Arrays.stream(tClass.getDeclaredFields())
                .filter(field -> field.getAnnotation(Column.class) != null && field.getAnnotation(Unique.class) != null)
                .collect(Collectors.toList());
    }


}
