package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;
import fj.F;
import fj.P2;

import java.sql.ResultSet;

public interface ColumnResultMapper<V> extends F<P2<ResultSet, Column<?, V>>, AbstractField<?, V>> {

}
