package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;

import java.sql.ResultSet;
import java.util.Optional;

public class OneToOneColumn<C, V> implements AbstractColumn<C, V> {

    private final String name;
    private final AbstractField<C, V> field;
    private final AbstractColumn<V, ?> foreignKey;
    private final Table<V> foreignTable;
    private final AbstractColumn<V, ?> foreignPrimaryKey;

    private OneToOneColumn(String name, AbstractField<C, V> field, AbstractColumn<V, ?> foreignKey, Table<V> foreignTable, AbstractColumn<V, ?> foreignPrimaryKey) {
        this.name = name;
        this.field = field;
        this.foreignKey = foreignKey;
        this.foreignTable = foreignTable;
        this.foreignPrimaryKey = foreignPrimaryKey;
    }

    public static <T, V> OneToOneColumn<T, V> oneToOne(String name, AbstractField<T, V> field, Table<V> foreignTable, AbstractColumn<V, ?> foreignPrimaryKey) {
        return new OneToOneColumn<T, V>(name, field, foreignPrimaryKey.withName(name), foreignTable, foreignPrimaryKey);
    }

    public AbstractColumn<?, ?> foreignPrimaryKey() {
        return foreignPrimaryKey;
    }

    @Override
    public AbstractField<C, V> field() {
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
    public OneToOneColumn<C, V> withField(AbstractField<C, V> field) {
        return new OneToOneColumn<>(name, field, foreignPrimaryKey.withName(name), foreignTable, foreignPrimaryKey);
    }

    @Override
    public AbstractColumn<C, V> withResult(ResultSet resultSet) {
        return new OneToOneColumn<>(name, field, foreignKey.withResult(resultSet), foreignTable, foreignPrimaryKey);
    }

    @Override
    public AbstractColumn<C, V> withName(String name) {
        return new OneToOneColumn<>(name, field, foreignKey, foreignTable, foreignPrimaryKey);
    }

    @Override
    public Optional<?> columnValue(C entity) {
        return field().getter().flatMap(getter -> {
            V innerEntity = getter.f(entity);
            return foreignPrimaryKey.columnValue(innerEntity);
        });
    }

    @Override
    public AbstractColumn<C, V> retrieveEntity(DatabaseSession session) {
        // TODO select only the correct row
        V object = foreignTable.selectAll(session).head();
        return new OneToOneColumn<>(name, field.as(object), foreignKey, foreignTable, foreignPrimaryKey);
    }
}
