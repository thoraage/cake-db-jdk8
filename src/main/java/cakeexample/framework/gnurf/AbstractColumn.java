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

    AbstractColumn<C, V> withName(String name);

    Optional<?> columnValue(C entity);

    AbstractColumn<C, V> withResult(ResultSet resultSet);

    default AbstractColumn<C, V> retrieveEntity(DatabaseSession session) {
        return this;
    }
}
