package cakeexample.db;

import cakeexample.ConfigModule;
import cakeexample.SingletonModule;
import cakeexample.util.DbUtil;

import java.util.List;
import java.util.Map;

import static cakeexample.util.Throwables.propagate;

public interface JdbcDbModule extends DbModule, ConfigModule, SingletonModule {
    class JdbcDb implements Db {
        final private DbUtil dbUtil;

        public JdbcDb(JdbcDbModule module) {
            Map<String,String> configuration = module.getConfiguration();
            dbUtil = new DbUtil(configuration.get("databaseDriverClass"), configuration.get("databaseUrl"));
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
