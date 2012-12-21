package cakeexample;

import java.util.function.Function;

public interface SingletonModule {
    <M, T> T get(Function<M, T> constructor, M module);
}
