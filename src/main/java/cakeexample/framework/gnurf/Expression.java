package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;
import fj.F;
import fj.data.List;

import java.sql.ResultSet;
import java.util.Optional;

public class Expression<C> {
    private final DbUtil dbUtil;
    private final Table<C> table;

    public Expression(DbUtil dbUtil, Table<C> table) {
        this.dbUtil = dbUtil;
        this.table = table;
    }

    public List<C> selectAll() {
        return dbUtil.select(table.name, (ResultSet r) -> table.entityConstructor.apply(table.columns.map(c -> c.withResult(r))));
    }

    public C update(C c) {
        throw new RuntimeException("Not implemented");
    }

    public InsertContinuation insert(C entity) {
        return new InsertContinuation(dbUtil.insert(table.name, table.columns.map(c -> harmoniseTypes(c, entity))));
    }

    private static <C, V> AbstractColumn<C, V> harmoniseTypes(AbstractColumn<C, V> c, C entity) {
        // TODO more information in error (which table for example or get position from stack trace during creation)
        return c.field().getter()
                .map(f -> c.withField(c.field().as(f.f(entity))))
                .orElseThrow(() -> new RuntimeException("Field of column " + c.name() + " missing getter function"));
    }

    public class InsertContinuation {
        public final Optional<Long> autogeneratedKey;

        public InsertContinuation(Optional<Long> autogeneratedKey) {
            this.autogeneratedKey = autogeneratedKey;
        }

        public C retrieve() {
            throw new RuntimeException("Not implemented");
        }

        public <V extends Number> Optional<V> id() {
            return dbUtil.getLastGeneratedValue();
        }
    }

}
