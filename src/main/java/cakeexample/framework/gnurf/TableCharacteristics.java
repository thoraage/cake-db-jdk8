package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;
import fj.data.List;

import java.util.function.Function;

public interface TableCharacteristics<C> {

    String name();

    List<AbstractColumn<C, ?>> columns();

    Function<Iterable<AbstractField<C, ?>>, C> entityConstructor();

}
