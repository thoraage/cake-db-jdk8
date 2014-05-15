package cakeexample.framework.domain;

import fj.F;

import java.util.Optional;
import java.util.UUID;

public class OptionalField<C, V> implements AbstractField<C, Optional<V>> {

    private final Field<C, V> field;
    private final Optional<Optional<V>> value;
    private final Optional<F<C, Optional<V>>> getter;

    public OptionalField(Field<C, V> field, Optional<Optional<V>> value, Optional<F<C, Optional<V>>> getter) {
        this.field = field;
        this.value = value;
        this.getter = getter;
    }

    public static <C, V> OptionalField<C, V> optional(Field<C, V> field, F<C, Optional<V>> getter) {
        return new OptionalField<>(field, Optional.empty(), Optional.of(getter));
    }

    @Override
    public Optional<F<C, Optional<V>>> getter() {
        return getter;
    }

    @Override
    public OptionalField<C, V> as(Optional<V> value) {
        return new OptionalField<>(field, Optional.of(value), getter);
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
    public Optional<V> get() {
        if (value.isPresent()) {
            return value.get();
        }
        throw new NoValueForFieldException("No value for field " + field.identity(), this);
    }

    @Override
    public Optional<Optional<V>> value() {
        return value;
    }

    public Field<C, V> field() { return field; }

}
