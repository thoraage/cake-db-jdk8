package cakeexample.framework.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ListUtil {
    @SafeVarargs
    static public <T> List<T> list(T... ts) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, ts);
        return list;
    }

    static public <T> List<T> list(Stream<T> stream) {
        List<T> list = new ArrayList<>();
        stream.forEach(list::add);
        return list;
    }
}
