package com.qr.function;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author qr
 * 2018/3/10
 */
public class ProxyFunctionFactory {

    public static ProxyFunction getProxyFunction(Type type, String wantName) {
        final Map<Method, ProxyFunction> functionMap;
        if ((functionMap = FunctionWorkshop.getFUNCTIONS().get(type)) == null) {
            return null;
        }
        for (Method method : functionMap.keySet()) {
            if (method.getName().equals(wantName)) {
                return functionMap.get(method);
            }
        }
        return functionMap.values().iterator().next();
    }

}
