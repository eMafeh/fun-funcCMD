package com.qr.injection;

import com.qr.function.ProxyFunction;
import com.qr.function.ProxyFunctionFactory;

import java.lang.reflect.Type;

class ObjectsFactory {
    static Object getFieldValue(Type type, String wantName) {
        ProxyFunction proxyFunction = ProxyFunctionFactory.getProxyFunction(type, wantName);
        if (proxyFunction != null) {
            return proxyFunction;
        }
        throw new RuntimeException("no value of this type: " + type);
    }

}
