package com.ll.exam;

import com.ll.exam.Util.Container;
import com.ll.exam.Util.Util;
import com.ll.exam.annotation.Controller;
import com.ll.exam.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
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
        for (Class<?> ControllerClass : ref.getTypesAnnotatedWith(Controller.class)) {
            Method[] methods = ControllerClass.getDeclaredMethods();

            for (Method method : methods) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);

                String httpMethod = null;
                String path = null;

                if (getMapping != null) {
                    path = getMapping.value();
                    httpMethod = "GET";
                }

                if (path != null && httpMethod != null) {
                    String actionPath = Util.Str.beforeForm(path, "/", 4);

                    String key = httpMethod + "___" + actionPath;

                    routeInfoMap.put(key, new RouteInfo(path, actionPath, method, ControllerClass));
                }
            }
        }
    }

    public static void runAction(HttpServletRequest req, HttpServletResponse resp) {
        Rq rq = new Rq(req, resp);

        String routeMethod = rq.getRouteMethod();
        String actionPath = rq.getActionPath();

        String mappingKey = routeMethod + "___" + actionPath;

        boolean contains = routeInfoMap.containsKey(mappingKey);

        if (contains) {
            rq.println("있음");
        } else {
            rq.println("없음");
            return;
        }

        
        runAction(rq, routeInfoMap.get(mappingKey));
    }

    private static void runAction(Rq rq, RouteInfo routeInfo) {
        Class controllerCls = routeInfo.getCls();
        Method actionMethod = routeInfo.getMethod();

        Object controllerObj = Container.getObj(controllerCls);

        try {
            actionMethod.invoke(controllerObj, rq);
        } catch (IllegalAccessException e) {
            rq.println("없음");
        } catch (InvocationTargetException e) {
            rq.println("없음");
        }
    }

    public static Map<String, RouteInfo> getRouteInfoMap() {
        return routeInfoMap;
    }
}
