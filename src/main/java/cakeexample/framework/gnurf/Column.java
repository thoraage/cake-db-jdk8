package cakeexample.framework.gnurf;

import cakeexample.framework.domain.Field;

public class Column<C, V> {
    public final String name;
    public final Field<C, V> field;

    public Column(String name, Field<C, V> field) {
        this.name = name;
        this.field = field;
    }

    public static <C, V> Column<C, V> column(String name, Field<C, V> field) {
        return new Column<>(name, field);
    }

    public Column<C, V> withField(Field<C, V> field) {
        return new Column<>(name, field);
    }
}
