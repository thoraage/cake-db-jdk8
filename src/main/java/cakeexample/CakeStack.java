package cakeexample;

import cakeexample.framework.SingletonModuleImpl;
import cakeexample.framework.web.JettyWebHandlerModule;
import cakeexample.model.GnurfCakeModelModule;

class CakeStack extends SingletonModuleImpl
        implements CakeConfigurationModule,
        GnurfCakeModelModule,
        CakePageHandlerModule,
        JettyWebHandlerModule {

    @Override
    public void initialize() {
        JettyWebHandlerModule.super.initialize();
    }
}
