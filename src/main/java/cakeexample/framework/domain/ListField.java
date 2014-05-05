package cakeexample.framework.domain;

import fj.F;
import fj.data.List;

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
}
