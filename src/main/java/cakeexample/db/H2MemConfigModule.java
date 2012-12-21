package cakeexample.db;

import cakeexample.ConfigModule;

import java.util.HashMap;
import java.util.Map;

public interface H2MemConfigModule extends ConfigModule {
    @Override
    default Map<String, String> getConfiguration() {
        Map<String, String> map = new HashMap<>();
        map.put("databaseDriverClass", "org.h2.Driver");
        map.put("databaseUrl", "jdbc:h2:mem:");
        return map;
    }
}
