package cakeexample.web;

import java.util.Map;

public interface PageHandlerModule {

    interface PageHandler {
        String handle(String method, Map<String, String[]> parameterMap);
    }

    PageHandler getPageHandler();

}
