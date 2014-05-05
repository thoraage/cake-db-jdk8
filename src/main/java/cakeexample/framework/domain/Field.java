package cakeexample.framework.domain;


import fj.F;
import fj.data.HashMap;

import javax.swing.*;
import java.util.Optional;
import java.util.UUID;

import static fj.data.HashMap.hashMap;

public class Field<C, V> implements AbstractField<C, V> {

    public final UUID identity;
    private final Optional<V> value;
    public final Optional<Class<?>> clazz;
    private final Optional<F<C, V>> getter;
    public final Optional<String> name;

    protected Field(UUID identity, Optional<V> value, Optional<Class<?>> clazz, Optional<F<C, V>> getter, Optional<String> name) {
        this.identity = identity;
        this.value = value;
        this.clazz = clazz;
        this.getter = getter;
        this.name = name;
    }

//    protected Field(UUID identity, Optional<V> value, Optional<Class<?>> clazz, Optional<F<C, V>> getter, Optional<String> name) {
//        this(identity, value, clazz, getter, name);
//    }

//    protected F5<UUID, Optional<V>, Optional<Class<V>>, Optional<F<C, V>>, Optional<String>, Field<C, V>> getMainConstructor() {
//        return (a, b, c, d, e) -> new Field(a, b, c, d, e);
//    }

//    private Field(UUID identity, Optional<V> value) {
//        this(identity, value, Optional.<F<C, V>>empty());
//    }

    public static <C, V> Field<C, V> field(Class<V> clazz, F<C, V> getter) {
        return new Field<>(UUID.randomUUID(), Optional.<V>empty(), Optional.of(clazz), Optional.of(getter), Optional.empty());
    }

    public static <C, V> Field<C, V> field(Class<V> clazz) {
        return new Field<>(UUID.randomUUID(), Optional.<V>empty(), Optional.of(clazz), Optional.empty(), Optional.empty());
    }

//    public Field<C, V> getter(F<C, V> getter) {
//        return new Field<>(identity, value, baseClazz, Optional.of(getter), name);
//    }

    public V get(Iterable<Field<C, ?>> fields, V currentValue) {
        return get(fields, Optional.of(currentValue));
    }

    public V from(Iterable<Field<C, ?>> fields) {
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

    public boolean isSameAs(Field<?, ?> fieldReference) {
        return identity.equals(fieldReference.identity);
    }

    public Field<C, V> as(V value) {
        Optional<V> optional = value == null ? Optional.empty() : Optional.of(value);
        return new Field<>(identity, optional, clazz, getter, name);
    }

    @Override
    public String toString() {
        return "identity: " + identity + ", value: " + value;
    }

//    public Field<C, List<V>> list() {
//        throw new RuntimeException("Not implemented");
//    }

//    public Field<C, V> name(String name) {
//        return new Field<>(identity, value, baseClazz, getter, optional, Optional.of(name));
//    }
}