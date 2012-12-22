package cakeexample.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtil {
    @SuppressWarnings("unchecked")
    static public <T> Set<Class<? extends T>> findInterfacesOf(Class<T> discriminatorInterface, Class<?> clazz) {
        Set<Class<? extends T>> set = new HashSet<>();
        if (!clazz.isInterface()) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null)
                return Collections.emptySet();
            set.addAll(findInterfacesOf(discriminatorInterface, superClass));
        }
        for (Class<?> subInterface : clazz.getInterfaces()) {
            if (discriminatorInterface.isAssignableFrom(subInterface)) {
                set.add((Class<? extends T>) subInterface);
            }
        }
        return set;
    }
}
