package cakeexample.framework.gnurf;

import cakeexample.framework.util.DbUtil;
import cakeexample.model.CakeModelModule;

public class GnurfDbSession {

    private final DbUtil dbUtil;

    public GnurfDbSession(String databaseDriverClass, String databaseUrl) {
        this.dbUtil = new DbUtil(databaseDriverClass, databaseUrl);
    }

    public <T> Expression<T> from(Table<T> table) {
        return new Expression<>(dbUtil, table);
    }

    public <T> Expression<T> into(Table<T> table) {
        return new Expression<>(dbUtil, table);
    }
}
