package cakeexample.framework.web;

import cakeexample.framework.ConfigurationModule;

import java.util.Optional;

public interface WebConfigurationModule extends ConfigurationModule {

    interface WebConfiguration extends Configuration {
        default Optional<Integer> getLocalPort() {
            return Optional.empty();
        }
    }

    default WebConfiguration getConfiguration() {
        return new WebConfiguration() {};
    }

}
