package cakeexample;

import cakeexample.db.H2MemConfigModule;
import cakeexample.db.JdbcDbModule;

public class CakeExample {
    static public void main(String[] args) {
        for (String name : new CakeModuleImpl().getDb().getCakes()) {
            System.out.println(name);
        }
    }
}

class CakeModuleImpl extends SingletonModuleImpl implements H2MemConfigModule, JdbcDbModule {
    @Override
    public void initialize() {
        JdbcDbModule.super.initialize();
    }
}

