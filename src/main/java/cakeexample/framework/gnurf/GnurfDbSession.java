package cakeexample.framework.gnurf;

import java.util.function.Supplier;

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

    public <T> void create(Table<T> table) {
        dbUtil.createTableIfNotExists(table.name, table.columns.map(column -> column.name).array(String[].class));
    }
}
