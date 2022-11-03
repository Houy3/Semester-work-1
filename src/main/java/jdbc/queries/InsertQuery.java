package jdbc.queries;

import java.util.List;
import java.lang.reflect.Field;

public class InsertQuery extends Query {

    private List<Field> fields;
    private List<Field> uniqueFields;

    public List<Field> getFields() {
        return fields;
    }

    public List<Field> getUniqueFields() {
        return uniqueFields;
    }

    public InsertQuery(String query, List<Field> fields, List<Field> uniqueFields) {
        super(query);
        this.fields = fields;
        this.uniqueFields = uniqueFields;
    }
}
