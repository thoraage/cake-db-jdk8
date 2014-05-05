package cakeexample.framework.domain;

import fj.F;

import java.util.Optional;

public interface AbstractField<C, V> {

    default V from(Iterable<Field<C, ?>> fields) {
        throw new RuntimeException("Not implemented");
    }

    default Optional<V> value() {
        throw new RuntimeException("Not implemented");
    }

    default Optional<F<C, V>> getter() {
        throw new RuntimeException("Not implemented");
    }

    default AbstractField<C, V> as(V value) {
        throw new RuntimeException("Not implemented");
    }

}
