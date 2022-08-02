package com.ll.exam;

import java.lang.reflect.Method;

public class RouteInfo {
    private String path;
    private Method method;

    public RouteInfo(String path, Method method) {
        this.path = path;
        this.method = method;
    }
}
