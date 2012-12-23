package cakeexample.framework.web;

import cakeexample.framework.ConfigModule;
import cakeexample.framework.SingletonModule;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface JettyWebHandlerModule extends WebHandlerModule, PageHandlerModule, SingletonModule, ConfigModule {

    public class JettyWebHandler implements WebHandler {
        final private JettyWebHandlerModule module;
        private Server server;

        public JettyWebHandler(JettyWebHandlerModule module) {
            this.module = module;
        }

        @Override
        public void start() {
            assertRunning(false);
            String webPort = module.getConfiguration().get("webPort");
            server = new Server(webPort == null ? 0 : Integer.valueOf(webPort));
            server.setHandler(new AbstractHandler() {
                public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                    response.setContentType("text/html;charset=utf-8");
                    baseRequest.setHandled(true);
                    HttpResponse httpResponse = module.getPageHandler().handle(request.getMethod(), request.getParameterMap());
                    response.setStatus(httpResponse.statusCode());
                    if (httpResponse.isLocationSame()) {
                        response.setHeader(HttpHeaders.LOCATION, request.getRequestURI());
                    }
                    response.getWriter().print(httpResponse.body());
                }
            });
            try {
                server.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void stop() {
            assertRunning(true);
            try {
                server.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                server = null;
            }
        }

        @Override
        public int getPort() {
            assertRunning(true);
            return server.getConnectors()[0].getLocalPort();
        }

        @Override
        public void join() {
            assertRunning(true);
            try {
                server.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void assertRunning(boolean is) {
            if ((server == null) == is)
                throw new RuntimeException("Server" + (is ? " not" : " already") + " started");
        }
    }

    @Override
    default void initialize() {
        getSingleton().put(JettyWebHandlerModule.class, () -> new JettyWebHandler(this));
    }

    @Override
    default WebHandler getWebHandler() {
        return getSingleton().get(JettyWebHandlerModule.class);
    }
}
