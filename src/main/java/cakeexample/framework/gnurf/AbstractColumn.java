package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;

import java.sql.ResultSet;
import java.util.Optional;

public interface AbstractColumn<C, V> {
    AbstractField<C, V> field();

    String name();

    boolean primaryKey();

    boolean autoIncrement();

    AbstractColumn<C, V> withField(AbstractField<C, V> field);

    AbstractColumn<C, V> fieldValue(V value);

    AbstractColumn<C, V> withName(String name);

    AbstractColumn<C, V> withResult(ResultSet resultSet);

    default Optional<V> extractFieldValue(C entity) {
        return field().getter().map(getter -> getter.f(entity));
    }

    default AbstractColumn<C, V> retrieveEntity(DatabaseSession session) {
        return this;
    }

    default AbstractColumn<C, V> preInsert(DatabaseSession session) {
        return this;
    }

    default Optional<?> columnValue() {
        return field().value();
    }
}
