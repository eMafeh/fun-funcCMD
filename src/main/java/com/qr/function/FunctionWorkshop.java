package com.qr.function;

import com.qr.order.MouseOutOrderImpl;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import util.*;

import java.lang.reflect.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;

/**
 * @author qr
 * 2018/3/10
 */
class FunctionWorkshop {
    /**
     * todo 暂定，该容器并不合适
     */
    private static final Map<Type, Map<Method, ProxyFunction>> FUNCTIONS = new ConcurrentHashMap<>();

    private static ProxyFunction toFunction(Method method) {
        //说明是java编译器生成的$的类或方法，不是真正想采集的方法
        if (method.isSynthetic()) {
            return null;
        }
        //不是静态方法暂时不搞事，要对象才能调用的方法不配做function
        if (!Modifier.isStatic(method.getModifiers())) {
            return null;
        }
        //私有方法就不搞了,私有的功能可能不完备
        if (Modifier.isPrivate(method.getModifiers())) {
            return null;
        }
        method.setAccessible(true);
        final Type[] types = method.getGenericParameterTypes();
        final Type returnType = method.getGenericReturnType();
        return void.class.equals(returnType) ? voidReturnFunction(method, types) : typeReturnFunction(method, types, returnType);

    }

    private static ProxyFunction voidReturnFunction(Method method, Type[] types) {
        Type type;
        final ProxyFunction proxyFunction;
        switch (types.length) {
            case 0:
                //入参也为空，出参也为空就不搞了
                return null;
            case 1:
                type = getType(Consumer.class, types[0]);
                proxyFunction = new ProxyFunction<>(getConsumer(method));
                break;
            case 2:
                type = getType(BiConsumer.class, types[0], types[1]);
                proxyFunction = new ProxyFunction<>(getBiConsumer(method));
                break;
            //暂不支持更多入参的方法变成函数
            default:
                return null;
        }
        cacheFunction(type, method, proxyFunction);
        return proxyFunction;
    }


    private static ProxyFunction typeReturnFunction(Method method, Type[] types, Type returnType) {
        Type type;
        final ProxyFunction proxyFunction;
        switch (types.length) {
            case 0:
                type = getType(Supplier.class, returnType);
                proxyFunction = new ProxyFunction<>(getSupplier(method));
                break;
            case 1:
                type = getType(Function.class, types[0], returnType);
                proxyFunction = new ProxyFunction<>(getFunction(method));
                break;
            case 2:
                type = getType(BiFunction.class, types[0], types[1], returnType);
                proxyFunction = new ProxyFunction<>(getBiFunction(method));
                break;
            //暂不支持更多入参的方法变成函数
            default:
                return null;
        }
        cacheFunction(type, method, proxyFunction);
        return proxyFunction;
    }

    private static void cacheFunction(Type type, Method method, ProxyFunction proxyFunction) {
        final Map<Method, ProxyFunction> functionMap = FUNCTIONS.computeIfAbsent(type, a -> new ConcurrentHashMap<>(1));
        functionMap.put(method, proxyFunction);
    }

    private static Consumer<?> getConsumer(Method method) {
        return o -> {
            try {
                method.invoke(null, o);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static BiConsumer<?, ?> getBiConsumer(Method method) {
        return (o1, o2) -> {
            try {
                method.invoke(null, o1, o2);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Function<?, ?> getFunction(Method method) {
        return o -> {
            try {
                return method.invoke(null, o);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static BiFunction<?, ?, ?> getBiFunction(Method method) {
        return (o1, o2) -> {
            try {
                return method.invoke(null, o1, o2);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Supplier<?> getSupplier(Method method) {
        return () -> {
            try {
                return method.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Type getType(Class<?> functionClass, Type... t) {
        final Type[] types = new Type[t.length];
        for (int i = 0; i < t.length; i++) {
            types[i] = getType(t[i]);
        }
        return ParameterizedTypeImpl.make(functionClass, types, null);
    }

    private static Type getType(Type type) {
        switch (type.getTypeName()) {
            case "int":
                return Integer.class;
            case "boolean":
                return Boolean.class;
            case "byte":
                return Byte.class;
            case "short":
                return Short.class;
            case "long":
                return Long.class;
            case "float":
                return Float.class;
            case "double":
                return Double.class;
            case "char":
                return Character.class;
            default:
                return type;
        }

    }

    private static void addFunction(Class<?>... classes) {
        for (Class<?> aClass : classes) {
            final Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                toFunction(method);
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        addFunction(AllThreadUtil.class, BeanToMap.class, ClassUtils.class, DateUtils.class, FileUtils.class, FindClassUtils.class, LocalIp.class, LoopThread.class, PathBuilder.class, StringSplitUtil.class, StringValueUtil.class, UnUsePort.class);
        addFunction(AllThreadUtil.class, BeanToMap.class, ClassUtils.class, DateUtils.class, FileUtils.class, FindClassUtils.class, LocalIp.class, LoopThread.class, PathBuilder.class, StringSplitUtil.class, StringValueUtil.class, UnUsePort.class);
        FUNCTIONS.forEach((a, b) -> System.out.println(b));
        System.err.println(FUNCTIONS);

        final Field trueFalse = MouseOutOrderImpl.INSTANCE.getClass().getDeclaredField("caseTrueFalse");
        trueFalse.setAccessible(true);
        trueFalse.set(MouseOutOrderImpl.INSTANCE, FUNCTIONS.get(trueFalse.getGenericType()).values().toArray()[0]);
        try {
            MouseOutOrderImpl.INSTANCE.useOrder("move fsf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Field test = FunctionWorkshop.class.getDeclaredField("test");
        test.setAccessible(true);
        final FunctionWorkshop workshop = new FunctionWorkshop();
        test.set(workshop, FUNCTIONS.get(test.getGenericType()).values().toArray()[0]);
        final String apply = workshop.test.apply("nihao", 20);
        System.out.println(apply);
    }

    private BiFunction<String, Integer, String> test;
}
