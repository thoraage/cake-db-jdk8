package cakeexample.framework.gnurf;

import fj.data.hlist.HList;

import java.util.function.Supplier;

import static fj.data.hlist.HList.cons;
import static fj.data.hlist.HList.nil;

public class GnurfDbSession {

    private final DbUtil dbUtil;

    public GnurfDbSession(String databaseDriverClass, String databaseUrl, Supplier<Boolean> showSql) {
        this.dbUtil = new DbUtil(databaseDriverClass, databaseUrl, showSql);
    }

    public <T> Expression<T> from(Table<T> table) {
        return new Expression<>(dbUtil, table);
    }

    public <T> Expression<T> into(Table<T> table) {
        return new Expression<>(dbUtil, table);
    }

    public final void create(Table<?>... tables) {
        for (Table<?> table : tables) {
            // TODO need abstraction for different field types
            dbUtil.createTableIfNotExists(table.name, table.columns);
        }
    }
}
