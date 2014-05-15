package cakeexample.framework.domain;

import fj.F;

import java.util.Optional;
import java.util.UUID;

public interface AbstractField<C, V> {

    default V from(Iterable<AbstractField<C, ?>> fields) {
        return from(fields, Optional.<V>empty());
    }

    default V from(Iterable<AbstractField<C, ?>> fields, Optional<V> currentValue) {
        for (AbstractField<?, ?> field : fields) {
            if (field.isSameAs(this)) {
                //noinspection unchecked
                return (V) field.get();
            }
        }
        return currentValue.orElseGet(() -> {
            throw new RuntimeException("No value");
        });
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

    boolean isSameAs(AbstractField<?, ?> field);

    UUID identity();

    V get();

}
