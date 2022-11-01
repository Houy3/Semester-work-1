package jdbc.queries;

import java.lang.reflect.Field;
import java.util.List;

public class SelectByIdQuery extends Query {

    private List<Field> fields;
    private List<Field> fieldsId;

    public List<Field> getFields() {
        return fields;
    }

    public List<Field> getFieldsId() {
        return fieldsId;
    }

    public SelectByIdQuery(String query, List<Field> fields, List<Field> fieldsId) {
        super(query);
        this.fields = fields;
        this.fieldsId = fieldsId;
    }
}
