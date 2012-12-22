package cakeexample.web;

public interface WebHandlerModule {

    interface WebHandler {
        void start();
        void stop();
        int getPort();
        void join();
    }

    WebHandler getWebHandler();

}
