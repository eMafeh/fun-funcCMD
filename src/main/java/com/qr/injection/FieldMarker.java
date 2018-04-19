package com.qr.injection;

import javax.annotation.Resource;
import java.lang.reflect.Field;

 class FieldMarker {

     static String getTargetName(Field field) {
        final Resource resource = field.getAnnotation(Resource.class);
        if (resource == null && field.getDeclaringClass().getAnnotation(Resource.class) == null) {
            return null;
        }
        return resource == null || "".equals(resource.name()) ? field.getName() : resource.name();
    }
}
