package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class InstanceUtil {
    private static final String VALUES = "values";

    public static Enum[] getEnumInstance(Class<?> aclass) {
        if (aclass != null && aclass.isEnum()) {
            try {
                final Method values1 = aclass.getMethod(VALUES);
                @SuppressWarnings("unchecked") final Enum[] values = (Enum[]) values1.invoke(null);
                if (values != null) {
                    return values;
                }
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return new Enum[0];
    }

    public static <T> T getSingLetonInstance(Class<?> aclass) {
        final Method[] methods = aclass.getMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) && method.getReturnType() == aclass && method.getParameterCount() == 0) {
                try {
                    @SuppressWarnings("unchecked")
                    T t = (T) method.invoke(null);
                    return t;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Object[] getInstance(Class<?> aclass) {
        if (aclass.isEnum()) {
            return getEnumInstance(aclass);
        }
        final Object instance = getSingLetonInstance(aclass);
        if (instance == null) {
            return new Object[0];
        }
        return new Object[]{instance};
    }
}
