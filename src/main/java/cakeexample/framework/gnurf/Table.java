package cakeexample.framework.gnurf;

import cakeexample.framework.domain.Field;
import fj.data.List;

import java.util.function.Function;

public class Table<C> {
    public final String name;
    public final List<Column<C, ?>> columns;
    public final Function<Iterable<Field<C, ?>>, C> entityConstructor;

    public Table(String name, List<Column<C, ?>> columns, Function<Iterable<Field<C, ?>>, C> entityConstructor) {
        this.name = name;
        this.columns = columns;
        this.entityConstructor = entityConstructor;
    }

}
