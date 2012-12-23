package cakeexample.web;

import cakeexample.framework.web.HttpResponse;

import java.util.Map;

public interface PageHandlerModule {

    interface PageHandler {
        HttpResponse handle(String method, Map<String, String[]> parameterMap);
    }

    PageHandler getPageHandler();

}
