package cakeexample.web;

import cakeexample.db.DbModule;
import cakeexample.framework.web.HttpResponse;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Map;
import java.util.stream.Stream;

import static cakeexample.framework.web.HttpResponse.response;

public interface CakePageHandlerModule extends PageHandlerModule, DbModule {

    class CakePageHandler implements PageHandler {
        private CakePageHandlerModule module;

        public CakePageHandler(CakePageHandlerModule module) {
            this.module = module;
        }

        @Override
        public HttpResponse handle(String method, Map<String, String[]> parameterMap) {
            switch (method) {
                case HttpMethods.GET:
                    String sql = "<html><body><form method='post'><input type='text' name='name'><input type='submit' value='Add'></form><br/><ul>";
                    Stream<String> stream = module.getDb().getCakes().stream();
                    sql += stream.<String>map(s -> "<li>" + s + "</li>").reduce("", (s1, s2) -> s1 + s2);
                    return response().body(sql + "</ul>");
                case HttpMethods.POST:
                    String[] names = parameterMap.get("name");
                    if (names != null && names.length > 0)
                        module.getDb().create(names[0]);
                    return response().statusCode(HttpStatus.SEE_OTHER_303).locationSame();
                default:
                    throw new RuntimeException("Not implemented");
            }
        }
    }

    @Override
    default PageHandler getPageHandler() {
        return new CakePageHandler(this);
    }

}
