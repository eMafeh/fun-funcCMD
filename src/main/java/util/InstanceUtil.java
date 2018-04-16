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

    public static Object getSingLetonInstance(Class<?> aclass) {
        final Method[] methods = aclass.getMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) && method.getReturnType() == aclass && method.getParameterCount() == 0) {
                try {
                    return method.invoke(null);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
