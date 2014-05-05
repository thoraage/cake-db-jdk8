package cakeexample.framework.domain;

import fj.data.List;

public class FieldComparator<E> {

    private final List<AbstractField<E, ?>> fields;

    public FieldComparator(List<AbstractField<E, ?>> fields) {
        this.fields = fields;
    }

    public static <C> FieldComparator<C> create(List<AbstractField<C, ?>> fields) {
        return new FieldComparator<>(fields);
    }

    public boolean equals(E e1, E e2) {
        if (e1 != e2 && (e1 == null || e2 == null)) {
            return false;
        }
        return e1 == e2 || fields.forall(getter -> {
            Object v1 = getFieldValue(e1, getter);
            Object v2 = getFieldValue(e2, getter);
            return v1 == v2 || (v1 != null && v1.equals(v2)) || v2.equals(v1);
        });
    }

    private Object getFieldValue(E e1, AbstractField<E, ?> f) {
        return f.getter().orElseThrow(() -> new RuntimeException("Value missing for field " + f)).f(e1);
    }

}