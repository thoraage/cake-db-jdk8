package cakeexample.web;

public interface PageHandlerModule {

    interface PageHandler {
        String handle(String method);
    }

    PageHandler getPageHandler();

}
