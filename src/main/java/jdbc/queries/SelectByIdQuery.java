package jdbc.queries;

import java.lang.reflect.Field;
import java.util.List;

public class SelectByIdQuery extends Query {

    private List<Field> fields;
    private Field fieldId;

    public List<Field> getFields() {
        return fields;
    }

    public Field getFieldId() {
        return fieldId;
    }

    public SelectByIdQuery(String query, List<Field> fields, Field fieldId) {
        super(query);
        this.fields = fields;
        this.fieldId = fieldId;
    }
}
