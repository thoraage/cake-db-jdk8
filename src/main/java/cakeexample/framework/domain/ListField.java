package cakeexample.framework.domain;

import fj.F;
import fj.data.List;

import java.util.UUID;

public class ListField<C, V> implements AbstractField<C, List<V>> {

    private final Field<C, V> field;
    private final F<C, List<V>> getter;

    public ListField(Field<C, V> field, F<C, List<V>> getter) {
        this.field = field;
        this.getter = getter;
    }

    public static <C, V> ListField<C, V> list(Field<C, V> field, F<C, List<V>> getter) {
        return new ListField<>(field, getter);
    }

    @Override
    public List<V> from(Iterable<AbstractField<C, ?>> abstractFields) {
        return null;
    }

    @Override
    public boolean isSameAs(AbstractField<?, ?> field) {
        return field.identity().equals(field.identity());
    }

    @Override
    public UUID identity() {
        return field.identity();
    }

    @Override
    public List<V> get() {
        throw new RuntimeException("Not implemented");
    }
}
