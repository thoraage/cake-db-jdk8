package cakeexample;

import cakeexample.db.JdbcDbModule;
import cakeexample.web.CakePageHandlerModule;
import cakeexample.web.JettyWebHandlerModule;

class CakeStack extends SingletonModuleImpl implements CakeConfigModule, JdbcDbModule, CakePageHandlerModule, JettyWebHandlerModule {
    @Override
    public void initialize() {
        JdbcDbModule.super.initialize();
        JettyWebHandlerModule.super.initialize();
    }
}
