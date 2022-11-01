package jdbc.queries;

import java.util.List;
import java.lang.reflect.Field;

public class InsertQuery extends Query {

    private List<Field> fields;
    private List<Field> fieldsId;

    public List<Field> getFields() {
        return fields;
    }

    public List<Field> getFieldsId() {
        return fieldsId;
    }

    public InsertQuery(String query, List<Field> fields, List<Field> fieldsId) {
        super(query);
        this.fields = fields;
        this.fieldsId = fieldsId;
    }
}
