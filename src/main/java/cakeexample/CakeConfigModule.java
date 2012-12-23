package cakeexample;

import cakeexample.framework.util.MapBuilder;

import java.util.Map;

public interface CakeConfigModule extends ConfigModule {
    @Override
    default Map<String, String> getConfiguration() {
        return MapBuilder.map("databaseDriverClass", "org.h2.Driver")
                .add("databaseUrl", "jdbc:h2:mem:")
                .add("webPort", "8080").build();
    }
}
