package cakeexample;

import cakeexample.util.DbUtil;

import java.util.List;
import java.util.Map;

import static cakeexample.util.Throwables.propagate;

public interface JdbcDbModule extends DbModule, ConfigModule, SingletonModule {
    class JdbcDb implements cakeexample.Db {
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
    default Db getDb() {
        return get(JdbcDb::new, this);
    }
}
