package cakeexample;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SingletonModuleImpl implements SingletonModule {
    private Map<Object, Object> map = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <M, T> T get(Function<M, T> constructor, M module) {
        T t = (T) map.get(constructor);
        if (t == null) {
            t = constructor.apply(module);
            map.put(constructor, t);
        }
        return t;
    }
}
