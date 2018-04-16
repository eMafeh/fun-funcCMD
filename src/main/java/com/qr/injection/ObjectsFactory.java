package com.qr.injection;

import com.qr.function.ProxyFunction;
import com.qr.function.ProxyFunctionFactory;

import java.lang.reflect.Field;

class ObjectsFactory {
    static Object getFieldValue(Field field, String wantName) {
        if (field.getType().isAssignableFrom(ProxyFunction.class)) {
            return ProxyFunctionFactory.getProxyFunction(field, wantName);
        } else {
            throw new RuntimeException("just support function, can not support type :" + field);
        }
    }
}
