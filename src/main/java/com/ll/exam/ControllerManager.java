package com.ll.exam;

import com.ll.exam.Util.Util;
import com.ll.exam.annotation.Controller;
import com.ll.exam.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ControllerManager {

    private static Map<String, RouteInfo> routeInfoMap;

    static {
        routeInfoMap = new HashMap<>();

        scanMappings();
    }

    private static void scanMappings() {
        Reflections ref = new Reflections("com.ll.exam");
        for (Class<?> c : ref.getTypesAnnotatedWith(Controller.class)) {
            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
//                PostMapping postMapping = method.getAnnotation(PostMapping.class);

                String httpMethod = null;
                String path = null;

                if (getMapping != null) {
                    path = getMapping.value();
                    httpMethod = "GET";
                }

                if (path != null && httpMethod != null) {
                    String key = httpMethod + "___" + path;

                    routeInfoMap.put(key, new RouteInfo(path, method));
                }
            }
        }
    }

    public static void runAction(HttpServletRequest req, HttpServletResponse resp) {

    }

    public static Map<String, RouteInfo> getRouteInfoMap() {
        return routeInfoMap;
    }
}
