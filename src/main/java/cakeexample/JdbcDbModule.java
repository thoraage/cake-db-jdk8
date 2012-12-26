package cakeexample;

import cakeexample.framework.SingletonModule;
import cakeexample.framework.db.DbConfigurationModule;
import cakeexample.framework.db.DbModule;
import cakeexample.framework.util.DbUtil;

import java.util.List;

import static cakeexample.framework.util.Throwables.propagate;

public interface JdbcDbModule extends DbModule, DbConfigurationModule, SingletonModule {
    class JdbcDb implements Db {
        final private DbUtil dbUtil;

        public JdbcDb(JdbcDbModule module) {
            DbConfiguration configuration = module.getConfiguration();
            dbUtil = new DbUtil(configuration.getDatabaseDriverClass(), configuration.getDatabaseUrl());
            dbUtil.createTableIfNotExists("cakes", "name");
        }

        @Override
        public void create(String name) {
            dbUtil.insertSingleColumn("cakes", name);
        }

        @Override
        public List<String> getCakes() {
            return dbUtil.select("cakes", s -> propagate(() -> s.getString("name")));
        }
    }

    @Override
    default void initialize() {
        getSingleton().put(JdbcDbModule.class, () -> new JdbcDb(this));
    }

    @Override
    default Db getDb() {
        return getSingleton().get(JdbcDbModule.class);
    }
}
