package cakeexample.web;

import cakeexample.ConfigModule;
import cakeexample.SingletonModuleImpl;
import cakeexample.framework.web.HttpResponse;
import cakeexample.util.HttpUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static cakeexample.framework.web.HttpResponse.response;
import static org.junit.Assert.assertEquals;

public class JettyWebHandlerTest {

    public static final String MESSAGE = "<html><body>Message in a bottle</body></html>";

    static private class TestJettyStack extends SingletonModuleImpl implements JettyWebHandlerModule, ConfigModule {
        @Override
        public PageHandler getPageHandler() {
            return (method, parameterMap) -> response().body(MESSAGE);
        }

        @Override
        public void initialize() {
            JettyWebHandlerModule.super.initialize();
        }

        @Override
        public Map<String, String> getConfiguration() {
            return new HashMap<>();
        }
    }

    final private TestJettyStack stack = new TestJettyStack();

    @Before
    public void setUpWebServer() {
        stack.getWebHandler().start();
    }

    @After
    public void shutDownServer() {
        stack.getWebHandler().stop();
    }

    @Test
    public void getPage() {
        assertEquals(MESSAGE, HttpUtil.get("http://localhost:" + stack.getWebHandler().getPort()));
    }

}
