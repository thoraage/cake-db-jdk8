package cakeexample.framework.domain;


import fj.F;

import java.util.Optional;
import java.util.UUID;

public class Field<C, V> implements AbstractField<C, V> {

    private final UUID identity;
    private final Optional<V> value;
    private final Class<?> clazz;
    private final Optional<F<C, V>> getter;
    public final Optional<String> name;

    protected Field(UUID identity, Optional<V> value, Class<?> clazz, Optional<F<C, V>> getter, Optional<String> name) {
        this.identity = identity;
        this.value = value;
        this.clazz = clazz;
        this.getter = getter;
        this.name = name;
    }

    public static <C, V> Field<C, V> field(Class<V> clazz, F<C, V> getter) {
        return new Field<>(UUID.randomUUID(), Optional.<V>empty(), clazz, Optional.of(getter), Optional.empty());
    }

    public static <C, V> Field<C, V> field(Class<?> clazz) {
        return new Field<>(UUID.randomUUID(), Optional.<V>empty(), clazz, Optional.empty(), Optional.empty());
    }

    public V get(Iterable<AbstractField<C, ?>> fields, V currentValue) {
        return from(fields, Optional.of(currentValue));
    }

    public V get() {
        if (value.isPresent()) {
            return value.get();
        }
        throw new NoValueForFieldException("No value for field " + identity, this);
    }

    @Override
    public Optional<F<C, V>> getter() {
        return getter;
    }

    @Override
    public Optional<V> value() {
        return value;
    }

    @Override
    public Class<?> clazz() {
        return clazz;
    }

    @Override
    public UUID identity() {
        return identity;
    }

    public Field<C, V> as(V value) {
        Optional<V> optional = value == null ? Optional.empty() : Optional.of(value);
        return new Field<>(identity, optional, clazz, getter, name);
    }

    @Override
    public String toString() {
        return "identity: " + identity + ", value: " + value;
    }

}