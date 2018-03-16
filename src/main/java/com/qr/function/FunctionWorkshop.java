package com.qr.function;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;

/**
 * @author qr
 * 2018/3/10
 */
public class FunctionWorkshop {

    /**
     * todo 暂定，该容器并不合适
     */
    private static final Map<Type, Map<Method, ProxyFunction>> FUNCTIONS = new ConcurrentHashMap<>();

    public static long getCount() {
        return count.get();
    }

    private static AtomicLong count = new AtomicLong();
    private static final Set<Class<?>> USE_CLASS_MARK = new ConcurrentSkipListSet<>(Comparator.comparing(Class::toString));
    private static final String MAIN_NAME = "main";

    private static void toFunction(Method method) {
        //说明是java编译器生成的$的类或方法，不是真正想采集的方法
        if (method.isSynthetic()) {
            return;
        }
        //不是静态方法暂时不搞事，要对象才能调用的方法不配做function
        if (!Modifier.isStatic(method.getModifiers())) {
            return;
        }
//        //私有方法就不搞了,私有的功能可能不完备
//        if (Modifier.isPrivate(method.getModifiers())) {
//            return;
//        }
        method.setAccessible(true);
        final Type[] types = method.getGenericParameterTypes();
        final Type returnType = method.getGenericReturnType();
        if (void.class.equals(returnType)) {
            voidReturnFunction(method, types);
        } else {
            typeReturnFunction(method, types, returnType);
        }

    }

    private static void voidReturnFunction(Method method, Type[] types) {
        Type type;
        final ProxyFunction proxyFunction;
        switch (types.length) {
            case 0:
                //入参也为空，出参也为空就不搞了
                return;
            case 1:
                //main 方法就不搞了,不管是不是public的，main方法不搞
                if (types[0].equals(String[].class) && MAIN_NAME.equals(method.getName())) {
                    return;
                }
                type = getType(Consumer.class, types[0]);
                proxyFunction = new ProxyFunction<>(getConsumer(method), method);
                break;
            case 2:
                type = getType(BiConsumer.class, types[0], types[1]);
                proxyFunction = new ProxyFunction<>(getBiConsumer(method), method);
                break;
            //暂不支持更多入参的方法变成函数
            default:
                return;
        }
        cacheFunction(type, method, proxyFunction);
    }


    private static ProxyFunction typeReturnFunction(Method method, Type[] types, Type returnType) {
        Type type;
        final ProxyFunction proxyFunction;
        switch (types.length) {
            case 0:
                type = getType(Supplier.class, returnType);
                proxyFunction = new ProxyFunction<>(getSupplier(method), method);
                break;
            case 1:
                type = getType(Function.class, types[0], returnType);
                proxyFunction = new ProxyFunction<>(getFunction(method), method);
                break;
            case 2:
                type = getType(BiFunction.class, types[0], types[1], returnType);
                proxyFunction = new ProxyFunction<>(getBiFunction(method), method);
                break;
            //暂不支持更多入参的方法变成函数
            default:
                return null;
        }
        cacheFunction(type, method, proxyFunction);
        return proxyFunction;
    }

    private static void cacheFunction(Type type, Method method, ProxyFunction proxyFunction) {
        count.incrementAndGet();
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
                throw new RuntimeException(e.getCause());
            }
        };
    }

    private static Function<?, ?> getFunction(Method method) {
        return o -> {
            try {
                return method.invoke(null, o);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        };
    }

    private static BiFunction<?, ?, ?> getBiFunction(Method method) {
        return (o1, o2) -> {
            try {
                return method.invoke(null, o1, o2);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        };
    }

    private static Supplier<?> getSupplier(Method method) {
        return () -> {
            try {
                return method.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
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

    public static void addFunction(Class<?>... classes) {
        for (Class<?> aClass : classes) {
            if (USE_CLASS_MARK.contains(aClass)) {
                continue;
            }
            USE_CLASS_MARK.add(aClass);
            final Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                toFunction(method);
            }
        }
    }

    public static Map<Type, Map<Method, ProxyFunction>> getFUNCTIONS() {
        return FUNCTIONS;
    }
}
