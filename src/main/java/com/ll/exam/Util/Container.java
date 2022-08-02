package com.ll.exam.Util;

import com.ll.exam.annotation.Controller;
import com.ll.exam.annotation.Service;
import com.ll.exam.article.controller.ArticleController;
import com.ll.exam.home.controller.HomeController;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Container {
    private static Map<Class, Object> objectMap;

    static {
        objectMap = new HashMap<>();

        scanComponents();
    }

    private static void scanComponents() {
        scanController();
        scanService();
    }

    private static void scanService() {
        Reflections ref = new Reflections("com.ll.exam");
        for (Class<?> c : ref.getTypesAnnotatedWith(Service.class)) {
            objectMap.put(c, Ut.cls.newObj(c, null));
        }
    }

    private static void scanController() {
        Reflections ref = new Reflections("com.ll.exam");
        for (Class<?> c : ref.getTypesAnnotatedWith(Controller.class)) {
            objectMap.put(c, Ut.cls.newObj(c, null));
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

}
