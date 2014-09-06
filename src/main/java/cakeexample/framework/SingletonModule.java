package cakeexample.framework;

public interface SingletonModule {

    interface Singleton {
        <M extends SingletonModule, T> T get(Class<M> clazz);
        <M extends SingletonModule, T> void put(Class<M> clazz, T t);
    }

    void initialize();
    Singleton getSingleton();

}
