package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;
import fj.F2;

import java.sql.ResultSet;

public interface ColumnResultMapper<C, V> extends F2<ResultSet, AbstractColumn<C, V>, AbstractField<C, V>> {

}
