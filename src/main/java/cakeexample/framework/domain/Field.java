package cakeexample.framework.domain;


import fj.F;

import java.util.Optional;
import java.util.UUID;

public class Field<C, V> {

    public final UUID identity;
    public final Optional<V> value;
    public final Optional<F<C, V>> getter;

    private Field(UUID identity, Optional<V> value, Optional<F<C, V>> getter) {
        this.identity = identity;
        this.value = value;
        this.getter = getter;
    }

    private Field(UUID identity, Optional<V> value) {
        this(identity, value, Optional.<F<C, V>>empty());
    }

    public Field(F<C, V> getter) {
        this(UUID.randomUUID(), Optional.<V>empty(), Optional.of(getter));
    }

    public Field<C, V> getter(F<C, V> getter) {
        return new Field<>(identity, value, Optional.of(getter));
    }

    public V get(Iterable<Field<C, ?>> fields, V currentValue) {
        return get(fields, Optional.of(currentValue));
    }

    public V get(Iterable<Field<C, ?>> fields) {
        return get(fields, Optional.<V>empty());
    }

    public V get(Iterable<Field<C, ?>> fields, Optional<V> currentValue) {
        for (Field<?, ?> field : fields) {
            if (field.isSameAs(this)) {
                //noinspection unchecked
                return (V) field.get();
            }
        }
        return currentValue.orElseGet(() -> {
            throw new RuntimeException("No value");
        });
    }

    public V get() {
        return value.get();
    }

    public boolean isSameAs(Field<?, ?> fieldReference) {
        return identity.equals(fieldReference.identity);
    }

    public Field<C, V> as(V value) {
        return new Field<>(identity, Optional.of(value), getter);
    }

    @Override
    public String toString() {
        return "identity: " + identity + ", value: " + value;
    }
}