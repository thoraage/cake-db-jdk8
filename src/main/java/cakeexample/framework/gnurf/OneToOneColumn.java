package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;

import java.sql.ResultSet;
import java.util.Optional;

public class OneToOneColumn<T, V> implements AbstractColumn<T, V> {

    private final String name;
    private final AbstractField<T, V> field;
    private final AbstractColumn<V, ?> foreignPrimaryKey;

    private OneToOneColumn(String name, AbstractField<T, V> field, AbstractColumn<V, ?> foreignPrimaryKey) {
        this.name = name;
        this.field = field;
        this.foreignPrimaryKey = foreignPrimaryKey;
    }

    public static <T, V> OneToOneColumn<T, V> oneToOne(String name, AbstractField<T, V> field, Table<V> foreignTable, AbstractColumn<V, ?> foreignPrimaryKey) {
        return new OneToOneColumn<T, V>(name, field, foreignPrimaryKey);
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

    @Override
    public Optional<?> columnValue(T entity) {
        return field().getter().flatMap(getter -> {
            V innerEntity = getter.f(entity);
            return foreignPrimaryKey.columnValue(innerEntity);
        });
    }

}
