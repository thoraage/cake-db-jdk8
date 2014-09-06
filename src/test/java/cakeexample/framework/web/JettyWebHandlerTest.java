package cakeexample.framework.web;

import cakeexample.H2DbConfigurationModule;
import cakeexample.framework.SingletonModuleImpl;
import cakeexample.framework.util.HttpUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static cakeexample.framework.web.HttpResponse.response;
import static org.junit.Assert.assertEquals;

public class JettyWebHandlerTest {

    public static final String MESSAGE = "<html><body>Message in a bottle</body></html>";

    static private class TestJettyStack extends SingletonModuleImpl implements JettyWebHandlerModule, H2DbConfigurationModule {
        interface LocalConfiguration extends WebConfiguration, H2DbConfiguration {}

        @Override
        public LocalConfiguration getConfiguration() {
            return new LocalConfiguration() {};
        }
        @Override
        public PageHandler getPageHandler() {
            return (method, parameterMap) -> response().body(MESSAGE);
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
