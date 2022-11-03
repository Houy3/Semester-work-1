package jdbc.queries;

import java.lang.reflect.Field;
import java.util.List;

public class SelectByIdQuery extends Query {

    private List<Field> fields;
    private List<Field> uniqueFields;

    public List<Field> getFields() {
        return fields;
    }

    public List<Field> getUniqueFields() {
        return uniqueFields;
    }

    public SelectByIdQuery(String query, List<Field> fields, List<Field> uniqueFields) {
        super(query);
        this.fields = fields;
        this.uniqueFields = uniqueFields;
    }
}
