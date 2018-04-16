package com.qr.injection;

import util.InstanceUtil;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiFunction;

public class FieldInjection {
    private static BiFunction<Field, String, Object> objectsFactory = ObjectsFactory::getFieldValue;

    public static void insertField(Class<?> aclass) {
        final Field[] fields = aclass.getDeclaredFields();
        for (Field field : fields) {
            insertField(field);
        }
    }

    private static void insertField(Field field) {
        final String wantName = getTargetName(field);
        if (wantName == null) {
            return;
        }
        checkFinal(field);
        insertField(field, wantName);
    }

    private static void checkFinal(Field field) {
        if (Modifier.isFinal(field.getModifiers())) {
            throw new RuntimeException("final field cannot injection : " + field);
        }
    }

    private static void insertField(Field field, String wantName) {
        if (Modifier.isStatic(field.getModifiers())) {
            insertField(field, wantName, null);
            return;
        }
        final Class<?> declaringClass = field.getDeclaringClass();
        if (declaringClass.isEnum()) {
            final Enum[] instances = InstanceUtil.getEnumInstance(declaringClass);
            for (Enum instance : instances) {
                insertField(field, wantName, instance);
            }
            return;
        }
        final Object instance = InstanceUtil.getSingLetonInstance(declaringClass);
        if (instance != null) {
            insertField(field, wantName, instance);
        }
    }

    private static void insertField(Field field, String wantName, Object instance) {
        final Object value = objectsFactory.apply(field, wantName);
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(instance, value);
            System.out.println(field.getDeclaringClass().getTypeName() + "." + field.getName() + "     ||| injection |||     " + value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static String getTargetName(Field field) {
        final Resource resource = field.getAnnotation(Resource.class);
        if (resource == null && field.getDeclaringClass().getAnnotation(Resource.class) == null) {
            return null;
        }
        return resource == null || "".equals(resource.name()) ? field.getName() : resource.name();
    }
}
