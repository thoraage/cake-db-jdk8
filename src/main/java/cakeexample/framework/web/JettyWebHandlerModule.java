package cakeexample.framework.web;

import cakeexample.framework.SingletonModule;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface JettyWebHandlerModule extends WebHandlerModule, WebConfigurationModule, PageHandlerModule, SingletonModule {

    public class JettyWebHandler implements WebHandler {
        final static private Logger logger = LoggerFactory.getLogger(JettyWebHandler.class);
        final private JettyWebHandlerModule module;
        private Server server;

        public JettyWebHandler(JettyWebHandlerModule module) {
            this.module = module;
        }

        @Override
        public void start() {
            assertRunning(false);
            Integer port = module.getConfiguration().getLocalPort().orElse(0);
            server = new Server(port);
            server.setHandler(new AbstractHandler() {
                public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                    try {
                        response.setContentType("text/html;charset=utf-8");
                        baseRequest.setHandled(true);
                        HttpResponse httpResponse = module.getPageHandler().handle(request.getMethod(), request.getParameterMap());
                        response.setStatus(httpResponse.statusCode());
                        if (httpResponse.isLocationSame()) {
                            response.setHeader(HttpHeaders.LOCATION, request.getRequestURI());
                        }
                        response.getWriter().print(httpResponse.body());
                    } catch (Exception e) {
                        logger.error("Unhandled error", e);
                        response.setStatus(500);
                        response.getWriter().print("DOH");
                    }
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
        getSingleton().put(JettyWebHandlerModule.class, new JettyWebHandler(this));
    }

    @Override
    default WebHandler getWebHandler() {
        return getSingleton().get(JettyWebHandlerModule.class);
    }
}
