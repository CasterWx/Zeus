package com.antzuhl.zeus.properties;

import org.springframework.context.ApplicationContext;

public class BeanUtils {
    private static ApplicationContext context;

    public static void setContext(ApplicationContext context) {
        BeanUtils.context = context;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
