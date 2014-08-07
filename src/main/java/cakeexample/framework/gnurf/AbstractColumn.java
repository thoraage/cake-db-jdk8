package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;

import java.sql.ResultSet;

public interface AbstractColumn<C, V> {
    AbstractField<C, V> field();

    String name();

    boolean primaryKey();

    boolean autoIncrement();

    AbstractColumn<C,V> withField(AbstractField<C, V> field);

    AbstractField<C,V> withResult(ResultSet resultSet);
}
