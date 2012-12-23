package cakeexample.framework.web;

import cakeexample.framework.util.MapBuilder;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Map;

import static cakeexample.framework.util.MapBuilder.with;

public class HttpResponse {

    public static final String STATUS_CODE = "statusCode";
    public static final String LOCATION_SAME = "locationSame";
    public static final String BODY = "body";
    private final Map<String, Object> map;

    public HttpResponse(Map<String, Object> map) {
        this.map = map;
    }

    public static HttpResponse response() {
        return new HttpResponse(MapBuilder.<String, Object>map(STATUS_CODE, HttpStatus.OK_200).build());
    }

    public HttpResponse statusCode(int statusCode) {
        return new HttpResponse(with(map).add(STATUS_CODE, statusCode).build());
    }

    public HttpResponse locationSame() {
        return new HttpResponse(with(map).add(LOCATION_SAME, true).build());
    }

    public HttpResponse body(String body) {
        return new HttpResponse(with(map).add(BODY, body).build());
    }

    public int statusCode() {
        return (int) map.get(STATUS_CODE);
    }

    public String body() {
        return (String) map.get(BODY);
    }

    public boolean isLocationSame() {
        return map.get(LOCATION_SAME) == Boolean.TRUE;
    }
}
