package util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class UnsafeUtil {
    private static final Unsafe UNSAFE;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object get(Object target, String fieldName) {
        try {
            UnsafeHelp help = UnsafeHelp.getUnsafeHelp(target, fieldName);
            return UNSAFE.getObjectVolatile(help.obj, help.addressA);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeFinal(Object target, String fieldName, Object value) {
        try {
            UnsafeHelp help = UnsafeHelp.getUnsafeHelp(target, fieldName);
            UNSAFE.putObjectVolatile(help.obj, help.addressA, value);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeFinal(Object target, String fieldName, Boolean value) {
        try {
            UnsafeHelp help = UnsafeHelp.getUnsafeHelp(target, fieldName);
            UNSAFE.putBooleanVolatile(help.obj, help.addressA, value);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static class UnsafeHelp {
        long addressA;
        Object obj;
        Field field;

        private static UnsafeHelp getUnsafeHelp(Object target, String fieldName) throws NoSuchFieldException {
            Class<?> clazz;
            if (target instanceof Class) {
                clazz = (Class<?>) target;
            } else {
                clazz = target.getClass();
            }
            UnsafeHelp help = new UnsafeHelp();
            help.field = clazz.getDeclaredField(fieldName);
            if (Modifier.isStatic(help.field.getModifiers())) {
                help.addressA = UNSAFE.staticFieldOffset(help.field);
                help.obj = clazz;
            } else {
                help.addressA = UNSAFE.objectFieldOffset(help.field);
                help.obj = target;
            }
            return help;
        }
    }
}
