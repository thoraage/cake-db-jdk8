package cakeexample;

import cakeexample.db.H2MemConfigModule;
import cakeexample.db.JdbcDbModule;
import cakeexample.web.JettyWebHandlerModule;
import cakeexample.web.PageHandler;

public class CakeExample {
    static public void main(String[] args) {
        CakeStack stack = new CakeStack();
        for (String name : stack.getDb().getCakes()) {
            System.out.println(name);
        }
        stack.getWebHandler().start();
        stack.getWebHandler().join();
        stack.getWebHandler().stop();
    }
}

class CakeStack extends SingletonModuleImpl implements H2MemConfigModule, JdbcDbModule, JettyWebHandlerModule {
    @Override
    public void initialize() {
        JdbcDbModule.super.initialize();
        JettyWebHandlerModule.super.initialize();
    }

    @Override
    public PageHandler getPageHandler() {
        return null;
    }
}

