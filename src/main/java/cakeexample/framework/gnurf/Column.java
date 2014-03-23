package cakeexample.framework.gnurf;

import cakeexample.framework.domain.Field;

public class Column<C, F> extends Field<C, F> {
    public final String name;
    public final Field<C, ?> field;

    public Column(String name, Field<C, ?> field) {
        this.name = name;
        this.field = field;
    }

    public static <C> Column<C, ?> column(String name, Field<C, ?> field) {
        return new Column<>(name, field);
    }
}
