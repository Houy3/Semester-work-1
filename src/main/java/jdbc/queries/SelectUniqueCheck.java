package jdbc.queries;

import java.lang.reflect.Field;
import java.util.List;

public class SelectUniqueCheck extends Query {

    private List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public SelectUniqueCheck(String query, List<Field> fields) {
        super(query);
        this.fields = fields;
    }
}
