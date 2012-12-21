package cakeexample;

import java.util.List;

import static cakeexample.util.ListUtil.list;

public interface MockDbModule extends DbModule {
    class MockDb implements Db {
        @Override
        public void create(String name) {
        }

        @Override
        public List<String> getCakes() {
            return list("*Yuck* plastic fantastic");
        }
    }
    @Override
    default Db getDb() {
        return new MockDb();
    }
}
