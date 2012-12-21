package cakeexample.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SetUtil {
    @SafeVarargs
    static public <T> Set<T> set(T... ts) {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, ts);
        return set;
    }

    static public <T> Set<T> asSet(Collection<T> ts) {
        return new HashSet<>(ts);
    }
}
