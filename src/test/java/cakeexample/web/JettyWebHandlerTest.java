package cakeexample.web;

import cakeexample.SingletonModuleImpl;
import cakeexample.util.HttpUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JettyWebHandlerTest {

    public static final String MESSAGE = "<html><body>Message in a bottle</body></html>";

    static private class TestJettyStack extends SingletonModuleImpl implements JettyWebHandlerModule {
        @Override
        public PageHandler getPageHandler() {
            return (method, parameterMap) -> MESSAGE;
        }

        @Override
        public void initialize() {
            JettyWebHandlerModule.super.initialize();
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
