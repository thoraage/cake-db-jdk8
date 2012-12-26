package cakeexample.framework;

import java.util.HashMap;
import java.util.Map;

public abstract class SingletonModuleImpl implements SingletonModule {
    private final Singleton singleton = new MapBasedSingleton();

    {
        initialize();
    }

    class MapBasedSingleton implements Singleton {
        private Map<Object, Object> map = new HashMap<>();

        @SuppressWarnings("unchecked")
        @Override
        public <M extends SingletonModule, T> T get(Class<M> clazz) {
            T t = (T) map.get(clazz);
            if (t == null) {
                throw new RuntimeException("Uninitialized module");
            }
            return t;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <M extends SingletonModule, T> void put(Class<M> clazz, T t) {
            if (map.get(clazz) != null) {
                throw new RuntimeException("Tried to initialize module twice");
            }
            //noinspection Convert2MethodRef
            map.put(clazz, t);
        }
    }

    @Override
    public Singleton getSingleton() {
        return singleton;
    }

}
