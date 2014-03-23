package cakeexample.framework.gnurf;

import cakeexample.framework.domain.Field;
import cakeexample.framework.util.DbUtil;
import cakeexample.model.CakeModelModule;
import fj.F;
import fj.data.List;

import java.sql.ResultSet;

import static cakeexample.framework.util.Throwables.propagate;

public class Expression<T> {
    private final DbUtil dbUtil;
    private final Table<T> table;

    public Expression(DbUtil dbUtil, Table<T> table) {
        this.dbUtil = dbUtil;
        this.table = table;
    }

    public List<T> selectAll() {
        return dbUtil.select(table.name, (ResultSet r) -> {
            F f = new FColumnToField<>(r);
            //noinspection unchecked
            return table.entityConstructor.apply(table.columns.map(f));
        });
    }

    private <T> T getValue(ResultSet r, Column<?, T> c) {
        //noinspection unchecked
        return (T) propagate(() -> r.getString(c.name));
    }

    public T update(T t) {
        throw new RuntimeException("Not implemented");
    }

    public T insert(T t) {
        throw new RuntimeException("Not implemented");
    }

    class FColumnToField<T> implements F<Column<?, T>, Field<?, T>> {
        private final ResultSet r;

        public FColumnToField(ResultSet r) {
            this.r = r;
        }

        @Override
        public Field<?, T> f(Column<?, T> c) {
            return c.as(getValue(r, c));
        }
    }
}
