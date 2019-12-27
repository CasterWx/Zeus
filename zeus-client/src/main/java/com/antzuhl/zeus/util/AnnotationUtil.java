package com.antzuhl.zeus.util;

import com.antzuhl.zeus.server.ZeusRegistry;

import java.lang.reflect.Field;

@Deprecated
public class AnnotationUtil {
    public static boolean getFruitInfo(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ZeusRegistry.class)) {
                return true;
            }
        }
        return false;
    }
}