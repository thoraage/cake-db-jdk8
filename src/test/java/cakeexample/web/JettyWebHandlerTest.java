package cakeexample.web;

import cakeexample.SingletonModuleImpl;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static cakeexample.util.Throwables.propagate;
import static org.junit.Assert.assertEquals;

public class JettyWebHandlerTest {

    public static final String MESSAGE = "<html><body>Message in a bottle</body></html>";

    static private class TestJettyStack extends SingletonModuleImpl implements JettyWebHandlerModule {
        @Override
        public PageHandler getPageHandler() {
            return new PageHandler() {
                @Override
                public String handle(String method) {
                    return MESSAGE;
                }
            };
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
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            String uri = "http://localhost:" + stack.getWebHandler().getPort();
            System.out.println("URI: " + uri);
            HttpGet get = new HttpGet(uri);
            String body = propagate(() -> client.execute(get, new BasicResponseHandler()));
            assertEquals(MESSAGE, body);
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

}
