package com.qr.function;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author qr
 * 2018/3/10
 */
public class ProxyFunctionFactory {

    public static ProxyFunction getProxyFunction(Field field, String wantName) {
        final Map<Method, ProxyFunction> functionMap;
        if ((functionMap = FunctionWorkshop.getFUNCTIONS().get(field.getGenericType())) == null) {
            throw new RuntimeException("no type of this field : " + field.toString());
        }
        for (Method method : functionMap.keySet()) {
            if (method.getName().equals(wantName)) {
                return functionMap.get(method);
            }
        }
        return functionMap.values().iterator().next();
    }

}
