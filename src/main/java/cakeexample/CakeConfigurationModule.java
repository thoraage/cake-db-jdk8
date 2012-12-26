package cakeexample;

import cakeexample.framework.db.H2DbConfigurationModule;
import cakeexample.framework.web.WebConfigurationModule;

import java.util.Optional;

public interface CakeConfigurationModule extends H2DbConfigurationModule, WebConfigurationModule {
    interface CakeConfiguration extends H2DbConfiguration, WebConfiguration {
        @Override
        default Optional<Integer> getLocalPort() {
            return Optional.of(8080);
        }
    }
    @Override
    default CakeConfiguration getConfiguration() {
        return new CakeConfiguration() {};
    }
}
