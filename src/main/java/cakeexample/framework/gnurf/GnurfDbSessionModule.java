package cakeexample.framework.gnurf;

import cakeexample.framework.db.DbConfigurationModule;

public interface GnurfDbSessionModule extends DbConfigurationModule{

    default DatabaseSession getGnurfDbSession() {
        DbConfiguration configuration = getConfiguration();
        return new NewConnectionPerRequestSession(configuration.getDatabaseDriverClass(), configuration.getDatabaseUrl(), configuration::getShowSql);
    }

}
