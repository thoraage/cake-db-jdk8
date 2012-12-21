package cakeexample;

import java.util.function.Function;

public interface Singleton {
    <M, T> T get(Function<M, T> constructor, M module);
}
