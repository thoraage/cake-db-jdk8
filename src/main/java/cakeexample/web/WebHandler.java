package cakeexample.web;

public interface WebHandler {
    void start();
    void stop();
    int getPort();
    void join();
}
