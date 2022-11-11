package SQL.queries;

import java.util.List;
import java.lang.reflect.Field;
import java.util.Optional;

public class InsertQuery extends Query {

    private List<Field> fields;
    private Optional<Field> PKField;

    public List<Field> getFields() {
        return fields;
    }

    public Optional<Field> getPKField() {
        return PKField;
    }

    public InsertQuery(String query, List<Field> fields, Optional<Field> PKField) {
        super(query);
        this.fields = fields;
        this.PKField = PKField;
    }
}
