package cakeexample.framework.util;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {
    final private Map<K,V> map;

    public MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    static public <K, V> MapBuilder<K, V> with(Map<K, V> map) {
        return new MapBuilder<>(map);
    }

    public MapBuilder<K, V> add(K key, V value) {
        Map<K, V> newMap = new HashMap<>(map);
        newMap.put(key, value);
        return new MapBuilder<>(newMap);
    }

    public Map<K, V> build() {
        return map;
    }

    public static <K, V> MapBuilder<K,V> map(K key, V value) {
        return with(new HashMap<K, V>()).add(key, value);
    }
}
