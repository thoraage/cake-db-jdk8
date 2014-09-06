package cakeexample.framework.web;

import java.util.Map;

public interface PageHandlerModule {

    interface PageHandler {
        HttpResponse handle(String method, Map<String, String[]> parameterMap);
    }

    PageHandler getPageHandler();

}
