package cakeexample.framework.db;

/*
import cakeexample.CakeConfigurationModule;
import cakeexample.JdbcDbModule;
import cakeexample.framework.SingletonModuleImpl;
import org.junit.Test;

import static cakeexample.framework.util.SetUtil.asSet;
import static cakeexample.framework.util.SetUtil.set;
import static org.junit.Assert.assertEquals;

public class JdbcDbTest {
    static class TestDbModule extends SingletonModuleImpl implements CakeConfigurationModule, JdbcDbModule {
        @Override
        public void initialize() {
            JdbcDbModule.super.initialize();
        }
    }

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

  */