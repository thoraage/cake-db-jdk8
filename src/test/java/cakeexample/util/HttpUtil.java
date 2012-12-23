package cakeexample.util;

import cakeexample.framework.util.ListUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Stream;

import static cakeexample.framework.util.Throwables.propagate;

public class HttpUtil {
    static public String get(String url) {
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            return propagate(() -> client.execute(new HttpGet(url), new BasicResponseHandler()));
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    public static void put(String url, HashMap<String, String> values) {
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(url);
            Stream<NameValuePair> nameValuePairStream = values.entrySet().stream().<NameValuePair>map(e -> new BasicNameValuePair(e.getKey(), e.getValue()));
            post.setEntity(new UrlEncodedFormEntity(ListUtil.list(nameValuePairStream)));
            client.execute(post, new BasicResponseHandler());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
}
