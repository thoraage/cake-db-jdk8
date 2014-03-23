package cakeexample.framework.gnurf;

import cakeexample.framework.db.DbConfigurationModule;

public interface GnurfDbSessionModule extends DbConfigurationModule{

    default GnurfDbSession getGnurfDbSession() {
        DbConfiguration configuration = getConfiguration();
        return new GnurfDbSession(configuration.getDatabaseDriverClass(), configuration.getDatabaseUrl());
    }

}
