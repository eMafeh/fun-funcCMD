package develop.show;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author kelaite
 * 2018/2/6
 */
public class ShowObject {
    public static void everyNoParamPublicMethod(Object obj) {
        final Class<?> aClass = obj.getClass();
        everyNoParamPublicMethod(obj, aClass);
    }

    private static <T> void everyNoParamPublicMethod(T t, Class<?> clazz) {
        noParamPublicMethod(t, clazz);
        if (Object.class == clazz) {
            return;
        }
        everyNoParamPublicMethod(t, clazz.getSuperclass());
    }

    public static <T> void noParamPublicMethod(T t, Class<?> clazz) {
        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getParameterCount() != 0) {
                continue;
            }
            method(method, t);
        }
    }

    public static <T> void noParamPublicStaticMethod(Class<?> clazz) {
        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getParameterCount() != 0) {
                continue;
            }
            if (Modifier.isStatic(method.getModifiers())) {
                method(method, null);
            }
        }
    }

    public static void method(Method method, Object obj, Object... args) {
        System.out.println(method.getName());
        try {
            System.out.println(method.invoke(obj, args));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
