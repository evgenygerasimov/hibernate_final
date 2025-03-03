package org.example.utils;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DIContainer {

    private static final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

    public static <T> T get(Class<T> clazz) {
        if (instances.containsKey(clazz)) {
            return (T) instances.get(clazz);
        }

        return createInstance(clazz);
    }

    private static <T> T createInstance(Class<T> clazz) {
        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            if (constructors.length == 0) {
                throw new IllegalStateException("No public constructor found for " + clazz.getName());
            }

            Constructor<?> constructor = constructors[0];
            Class<?>[] paramTypes = constructor.getParameterTypes();
            Object[] dependencies = new Object[paramTypes.length];

            for (int i = 0; i < paramTypes.length; i++) {
                dependencies[i] = get(paramTypes[i]);
            }

            T instance = (T) constructor.newInstance(dependencies);
            instances.put(clazz, instance);
            return instance;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance for " + clazz.getName(), e);
        }
    }
}
