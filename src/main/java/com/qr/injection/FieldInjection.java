package com.qr.injection;

import util.InstanceUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.BiFunction;

public class FieldInjection {
    private static BiFunction<Type, String, Object> objectsFactory = ObjectsFactory::getFieldValue;

    public static void insertField(Class<?> aclass) {
        final Field[] fields = aclass.getDeclaredFields();
        for (Field field : fields) {
            insertField(field);
        }
    }

    private static void insertField(Field field) {
        final String wantName = FieldMarker.getTargetName(field);
        if (wantName == null) {
            return;
        }
        insertField(field, wantName);
    }


    private static void insertField(Field field, String wantName) {
        if (Modifier.isStatic(field.getModifiers())) {
            insertField(field, wantName, null);
            return;
        }
        final Class<?> declaringClass = field.getDeclaringClass();
        Object[] instances = InstanceUtil.getInstance(declaringClass);
        for (Object instance : instances) {
            insertField(field, wantName, instance);
        }
    }


    private static void insertField(Field field, String wantName, Object instance) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Object oldValue = field.get(instance);
            if (oldValue == null) {
                insertNewValue(field, wantName, instance);
            } else {
                insertOldValue(field, wantName, oldValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void insertNewValue(Field field, String wantName, Object instance) throws IllegalAccessException {
        if (Modifier.isFinal(field.getModifiers())) {
            throw new RuntimeException("final field cannot injection : " + field);
        }
        final Object value = objectsFactory.apply(field.getGenericType(), wantName);
        field.set(instance, value);
        System.out.println(field.getDeclaringClass().getTypeName() + "." + field.getName() + "     ||| injection |||     " + value);
    }

    @SuppressWarnings("unchecked")
    private static void insertOldValue(Field field, String wantName, Object oldValue) {
        if (List.class.isAssignableFrom(oldValue.getClass())) {
            List box = (List) oldValue;
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            final Object value = objectsFactory.apply(genericType.getActualTypeArguments()[0], wantName);
            box.add(value);
        }
        //有值又不是可以填充的值,跳过
        System.out.println(field.getDeclaringClass().getTypeName() + "." + field.getName() + "     ||| injection skip |||     " + oldValue);
    }

}
