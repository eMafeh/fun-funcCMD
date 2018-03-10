package com.qr.function;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * @author qr
 * 2018/3/10
 */
public class FunctionLibrary {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        final Method[] methods = FunctionLibrary.class.getDeclaredMethods();
        for (Method method : methods) {

            System.err.println(method.toGenericString());
            addFunction(method, "");
        }
    }

    public static void test(Function<Function<String, Object>, Function<Object, String>> function) throws Exception {

    }

    static void addFunction(Method method, String area) throws IllegalAccessException, InstantiationException {
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        for (Type genericParameterType : genericParameterTypes) {
            System.out.println(genericParameterType.getClass());
        }
    }

    static void changeFunction(Function function, String area) {

    }

    static Function getFunction(Field field, String area) {
        return null;
    }
}
