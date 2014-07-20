package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;
import fj.data.List;

import java.util.function.Function;

public class Table<C> {
    public final String name;
    public final List<AbstractColumn<C, ?>> columns;
    public final Function<Iterable<AbstractField<C, ?>>, C> entityConstructor;

    public Table(String name, List<AbstractColumn<C, ?>> columns, Function<Iterable<AbstractField<C, ?>>, C> entityConstructor) {
        this.name = name;
        this.columns = columns;
        this.entityConstructor = entityConstructor;
    }

}
