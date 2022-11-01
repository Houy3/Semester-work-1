package jdbc.queries;

import java.util.List;
import java.lang.reflect.Field;

public class InsertQuery extends Query {

    private List<Field> fields;
    private Field fieldId;

    public List<Field> getFields() {
        return fields;
    }

    public Field getFieldId() {
        return fieldId;
    }

    public InsertQuery(String query, List<Field> fields, Field fieldId) {
        super(query);
        this.fields = fields;
        this.fieldId = fieldId;
    }
}
