package cakeexample.web;

import cakeexample.db.DbModule;
import org.eclipse.jetty.http.HttpMethods;

import java.util.stream.Stream;

public interface CakePageHandlerModule extends PageHandlerModule, DbModule {

    class CakePageHandler implements PageHandler {
        private CakePageHandlerModule module;

        public CakePageHandler(CakePageHandlerModule module) {
            this.module = module;
        }

        @Override
        public String handle(String method) {
            switch (method) {
                case HttpMethods.GET:
                    String sql = "<html><body><form action='POST'><input type='text'></form><br/><ul>";
                    Stream<String> stream = module.getDb().getCakes().stream();
                    sql += stream.<String>map(s -> "<li>" + s + "</li>").reduce("", (s1, s2) -> s1 + s2);
                    return sql + "</ul>";
                case HttpMethods.POST:
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
