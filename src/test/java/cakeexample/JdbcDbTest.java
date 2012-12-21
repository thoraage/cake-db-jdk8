package cakeexample;

import org.junit.Test;

import static cakeexample.util.SetUtil.asSet;
import static cakeexample.util.SetUtil.set;
import static org.junit.Assert.assertEquals;

public class JdbcDbTest {
    static class TestDbModule extends SingletonModuleImpl implements H2MemConfigModule, JdbcDbModule {}

    final private TestDbModule dbModule = new TestDbModule();

    @Test
    public void createCake() {
        dbModule.getDb().create("Pancake");
    }

    @Test
    public void getCakes() {
        dbModule.getDb().create("Pancake");
        dbModule.getDb().create("Cup cake");
        assertEquals(set("Pancake", "Cup cake"), asSet(dbModule.getDb().getCakes()));
    }

}

