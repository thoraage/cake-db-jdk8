package cakeexample;

public interface MockDbModule extends DbModule {
    class MockDb implements Db {
        @Override
        public String[] getNames() {
            return new String[] { "*Yuck* plastic fantastic" };
        }
    }
    @Override
    default Db getDb() {
        return new MockDb();
    }
}
