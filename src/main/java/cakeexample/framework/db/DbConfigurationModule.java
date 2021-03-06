package cakeexample.framework.db;

import cakeexample.framework.ConfigurationModule;

public interface DbConfigurationModule extends ConfigurationModule {

    interface DbConfiguration extends Configuration {
        String getDatabaseDriverClass();
        String getDatabaseUrl();
    }

    @Override
    DbConfiguration getConfiguration();

}
