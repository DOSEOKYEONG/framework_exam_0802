package com.ll.exam.Util;

import com.ll.exam.annotation.Autowired;
import com.ll.exam.annotation.Controller;
import com.ll.exam.annotation.Repository;
import com.ll.exam.annotation.Service;
import org.reflections.Reflections;

import java.util.*;

public class Container {
    private static Map<Class, Object> objectMap;

    static {
        objectMap = new HashMap<>();

        scanComponents();
    }

    private static void scanComponents() {
        scanController();
        scanService();
        scanRepository();

        // 조립 의존성 해결
        resolveDependenciesAllComponents();

    }

    private static void scanRepository() {
        Reflections ref = new Reflections("com.ll.exam");
        for (Class<?> c : ref.getTypesAnnotatedWith(Repository.class)) {
            objectMap.put(c, Util.cls.newObj(c, null));
        }
    }

    private static void resolveDependenciesAllComponents() {
        for (Class cls : objectMap.keySet()) {
            Object o = objectMap.get(cls);

            resolveDependencies(o);
        }
    }

    private static void resolveDependencies(Object o) {
        Arrays.asList(o.getClass().getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(Autowired.class))
                .map(field -> {
                    field.setAccessible(true);
                    return field;
                })
                .forEach(field -> {
                    Class cls = field.getType();
                    Object dependency = objectMap.get(cls);

                    try {
                        field.set(o, dependency);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static void scanService() {
        Reflections ref = new Reflections("com.ll.exam");
        for (Class<?> c : ref.getTypesAnnotatedWith(Service.class)) {
            objectMap.put(c, Util.cls.newObj(c, null));
        }
    }

    private static void scanController() {
        Reflections ref = new Reflections("com.ll.exam");
        for (Class<?> c : ref.getTypesAnnotatedWith(Controller.class)) {
            objectMap.put(c, Util.cls.newObj(c, null));
        }
    }

    public static <T> T getObj(Class<T> cls) {
        return (T) objectMap.get(cls);
    }

    public static List<String> getAllControllerNames() {
        List<String> AllControllerNames = new ArrayList<>();

        Reflections ref = new Reflections("com.ll.exam");
        for (Class<?> c : ref.getTypesAnnotatedWith(Controller.class)) {
            AllControllerNames.add(c.getSimpleName().replace("Controller", "").toLowerCase());
        }

        return AllControllerNames;
    }

    private static void initObject(Object obj) {
        Arrays.asList(obj.getClass().getDeclaredFields())
                .stream()
                .sequential()
                .filter(f -> f.isAnnotationPresent(Autowired.class))
                .map(field -> {
                    field.setAccessible(true);
                    return field;
                })
                .forEach(field -> {
                    Class clazz = field.getType();

                    try {
                        field.set(obj, objectMap.get(clazz));
                    } catch (IllegalAccessException e) {

                    }
                });
    }

}
