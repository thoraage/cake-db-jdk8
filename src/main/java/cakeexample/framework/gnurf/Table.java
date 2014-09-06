package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;
import fj.data.List;

import java.util.function.Function;

public class Table<C> implements TableOperations<C> {
    private final String name;
    private final List<AbstractColumn<C, ?>> columns;
    private final Function<Iterable<AbstractField<C, ?>>, C> entityConstructor;

    public Table(String name, List<AbstractColumn<C, ?>> columns, Function<Iterable<AbstractField<C, ?>>, C> entityConstructor) {
        this.name = name;
        this.columns = columns;
        this.entityConstructor = entityConstructor;
    }

    public String name() {
        return name;
    }

    public List<AbstractColumn<C, ?>> columns() {
        return columns;
    }

    public Function<Iterable<AbstractField<C, ?>>, C> entityConstructor() {
        return entityConstructor;
    }

}
