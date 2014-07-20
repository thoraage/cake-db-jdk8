package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;
import cakeexample.framework.domain.Field;

import java.util.Optional;

public class OneToOneColumn<C, V> implements AbstractColumn<C, V> {

    private final String name;
    private final AbstractField<C, V> field;

    private OneToOneColumn(String name, AbstractField<C, V> field) {
        this.name = name;
        this.field = field;
    }

    public static <OT, T, V> OneToOneColumn<T, V> oneToOne(String name, AbstractField<T, V> field, Table<OT> foreignTable, AbstractColumn<OT, ?> foreignPrimaryKey) {
        return new OneToOneColumn<T, V>(name, Field.field(field.clazz()));
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
        return new OneToOneColumn<>(name, field);
    }
}
