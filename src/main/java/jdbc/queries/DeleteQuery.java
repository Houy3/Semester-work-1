package jdbc.queries;

import java.lang.reflect.Field;
import java.util.List;

public class DeleteQuery extends Query{

    private List<Field> fieldsId;

    public List<Field> getFieldsId() {
        return fieldsId;
    }

    public DeleteQuery(String query, List<Field> fieldsId) {
        super(query);
        this.fieldsId = fieldsId;
    }
}
