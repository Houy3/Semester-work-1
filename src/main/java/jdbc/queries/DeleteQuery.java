package jdbc.queries;

import java.lang.reflect.Field;
import java.util.List;

public class DeleteQuery extends Query{

    private Field fieldId;

    public Field getFieldId() {
        return fieldId;
    }

    public DeleteQuery(String query, Field fieldId) {
        super(query);
        this.fieldId = fieldId;
    }
}
