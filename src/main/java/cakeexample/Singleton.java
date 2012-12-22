package cakeexample;

import java.util.concurrent.Callable;

public interface Singleton {
    <M extends SingletonModule, T> T get(Class<M> clazz);
    <M extends SingletonModule, T> void put(Class<M> clazz, Callable<T> constructor);
}
