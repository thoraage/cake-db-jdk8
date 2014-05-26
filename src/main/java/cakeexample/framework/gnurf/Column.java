package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;

public class Column<C, V> {
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

    public AbstractField<C, V> field() {
        return field;
    }

    public String name() {
        return name;
    }

}
