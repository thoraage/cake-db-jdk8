package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;
import cakeexample.framework.domain.Field;
import cakeexample.framework.domain.OptionalField;

import java.sql.ResultSet;
import java.util.Optional;

import static cakeexample.framework.util.Throwables.propagate;

public class Column<C, V> implements AbstractColumn<C, V> {
    private final String name;
    private final AbstractField<C, V> field;
    private final boolean autoIncrement;
    private final boolean primaryKey;

    public Column(String name, AbstractField<C, V> field, boolean primaryKey, boolean autoIncrement) {
        this.name = name;
        this.field = field;
        this.primaryKey = primaryKey;
        this.autoIncrement = autoIncrement;
    }

    public static <C, V> Column<C, V> column(String name, AbstractField<C, V> field) {
        return new Column<>(name, field, false, false);
    }

    public Column<C, V> withField(AbstractField<C, V> field) {
        return new Column<>(name, field, primaryKey, autoIncrement);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Column<C, V> withResult(ResultSet resultSet) {
        V value;
        if (field instanceof Field) {
            //noinspection unchecked
            value = propagate(() -> (V) resultSet.getObject(name()));
        } else if (field instanceof OptionalField) {
            value = (V) Optional.ofNullable(propagate(() -> resultSet.getObject(name())));
        } else {
            throw new NotImplementedException("Unable to handle field type " + field.getClass());
        }
        return fieldValue(value);
    }

    @Override
    public Column<C, V> fieldValue(V value) {
        return new Column<>(name, field.as(value), primaryKey, autoIncrement);
    }

    @Override
    public Column<C, V> withName(String name) {
        return new Column<>(name, field, primaryKey, autoIncrement  );
    }

    public Column<C, V> primaryKey(boolean value) {
        return new Column<>(name, field, value, autoIncrement);
    }

    public Column<C, V> autoIncrement(boolean value) {
        return new Column<>(name, field, primaryKey, value);
    }

    public boolean primaryKey() {
        return primaryKey;
    }

    public boolean autoIncrement() {
        return autoIncrement;
    }

    @Override
    public AbstractField<C, V> field() {
        return field;
    }

    @Override
    public String name() {
        return name;
    }

}
