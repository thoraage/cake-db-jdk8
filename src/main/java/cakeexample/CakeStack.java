package cakeexample;

import cakeexample.framework.SingletonModuleImpl;
import cakeexample.framework.web.JettyWebHandlerModule;

class CakeStack extends SingletonModuleImpl implements CakeConfigModule, JdbcDbModule, CakePageHandlerModule, JettyWebHandlerModule {
    @Override
    public void initialize() {
        JdbcDbModule.super.initialize();
        JettyWebHandlerModule.super.initialize();
    }
}
