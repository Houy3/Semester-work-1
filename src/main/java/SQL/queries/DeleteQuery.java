package SQL.queries;

import java.lang.reflect.Field;
import java.util.List;

public class DeleteQuery extends Query{

    private List<Field> uniqueFields;

    public List<Field> getUniqueFields() {
        return uniqueFields;
    }

    public DeleteQuery(String query, List<Field> uniqueFields) {
        super(query);
        this.uniqueFields = uniqueFields;
    }
}
