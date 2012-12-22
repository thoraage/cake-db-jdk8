package cakeexample.util;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import static cakeexample.util.Throwables.propagate;

public class HttpUtil {
    static public String get(String url) {
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            return propagate(() -> client.execute(new HttpGet(url), new BasicResponseHandler()));
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
}
