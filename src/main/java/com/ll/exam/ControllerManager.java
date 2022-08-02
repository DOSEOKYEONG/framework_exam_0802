package com.ll.exam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

public class ControllerManager {

    private static Map<String, RouteInfo> routeInfoMap;

    static{
        routeInfoMap = new HashMap<>();

        scanMappings();
    }

    private static void scanMappings() {

    }

    public static void runAction(HttpServletRequest req, HttpServletResponse resp) {

    }
}
