package cn.qr.cryptozoic.injection;


import cn.qr.instance.InstanceUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class FieldInjection {

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
        try {
            insertField(field, wantName);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private static void insertField(Field field, String wantName) throws IllegalAccessException {
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


    private static void insertField(Field field, String wantName, Object instance) throws IllegalAccessException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        Object oldValue = field.get(instance);
        if (oldValue == null) {
            insertNewValue(field, wantName, instance);
        } else {
            insertOldValue(field, wantName, oldValue);
        }
    }

    // if object of this field value is null,give a new value
    private static void insertNewValue(Field field, String wantName, Object instance) throws IllegalAccessException {
        if (Modifier.isFinal(field.getModifiers())) {
            throw new RuntimeException("||| final field with null value cannot injection |||: " + field);
        }
        final Object value = ObjectsFactory.getFieldValue(field.getGenericType(), wantName);
        field.set(instance, value);
        System.out.println(field.getDeclaringClass().getTypeName() + "." + field.getName() + "     ||| injection value |||     " + value);
    }

    //if is have a old value ,try append of this value
    @SuppressWarnings("unchecked")
    private static void insertOldValue(Field field, String wantName, Object oldValue) {
        if (List.class.isAssignableFrom(oldValue.getClass())) {
            List box = (List) oldValue;
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Type[] types = genericType.getActualTypeArguments();
            if (types.length == 0)
                throw new RuntimeException(field.getDeclaringClass().getTypeName() + "." + field.getName() + "     ||| injection List un know type |||     " + oldValue);

            //default use first type
            final Object value = ObjectsFactory.getFieldValue(types[0], wantName);
            box.add(value);
            System.out.println(field.getDeclaringClass().getTypeName() + "." + field.getName() + "     ||| injection List |||     " + value);
            return;
        }
        //有值又不是可以填充的值,跳过
        throw new RuntimeException(field.getDeclaringClass().getTypeName() + "." + field.getName() + "     ||| cannot injection |||     " + oldValue);
    }

}
