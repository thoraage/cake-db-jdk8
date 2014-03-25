package cakeexample;

import cakeexample.framework.db.DbConfigurationModule;

public interface H2DbConfigurationModule extends DbConfigurationModule {

    interface H2DbConfiguration extends DbConfiguration {
        @Override
        default String getDatabaseDriverClass() {
            return "org.h2.Driver";
        }

        @Override
        default String getDatabaseUrl() {
            return "jdbc:h2:mem:mycake";
        }

        @Override
        default Boolean getShowSql() {
            return true;
        }
    }

    @Override
    default H2DbConfiguration getConfiguration() {
        return new H2DbConfiguration() {};
    }

}
