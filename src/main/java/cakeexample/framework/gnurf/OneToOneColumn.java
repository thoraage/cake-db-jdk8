package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;
import cakeexample.framework.domain.Field;
import cakeexample.framework.domain.OptionalField;

import java.sql.ResultSet;
import java.util.Optional;

import static cakeexample.framework.util.Throwables.propagate;

public class OneToOneColumn<T, V> implements AbstractColumn<T, V> {

    private final String name;
    private final AbstractField<T, V> field;
    private final AbstractColumn<?, ?> foreignPrimaryKey;

    private OneToOneColumn(String name, AbstractField<T, V> field, AbstractColumn<?, ?> foreignPrimaryKey) {
        this.name = name;
        this.field = field;
        this.foreignPrimaryKey = foreignPrimaryKey;
    }

    public static <OT, T, V> OneToOneColumn<T, V> oneToOne(String name, AbstractField<T, V> field, Table<OT> foreignTable, AbstractColumn<OT, ?> foreignPrimaryKey) {
        return new OneToOneColumn<T, V>(name, Field.field(field.clazz()), foreignPrimaryKey);
    }

    public AbstractColumn<?, ?> foreignPrimaryKey() {
        return foreignPrimaryKey;
    }

    @Override
    public AbstractField<T, V> field() {
        return field;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean primaryKey() {
        return false;
    }

    @Override
    public boolean autoIncrement() {
        return false;
    }

    @Override
    public OneToOneColumn<T, V> withField(AbstractField<T, V> field) {
        return new OneToOneColumn<>(name, field, foreignPrimaryKey);
    }

    @Override
    public AbstractField<T, V> withResult(ResultSet resultSet) {
        //noinspection unchecked
//        return propagate(() -> field.as(Optional.ofNullable((T) resultSet.getObject(name))));
        throw new NotImplementedException();
    }
}
