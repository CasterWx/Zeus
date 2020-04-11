package com.antzuhl.zeus.framework;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;
    private static String applicationName;

    @Override
    @SuppressWarnings("static-access")
    public void setApplicationContext(ApplicationContext contex) throws BeansException {
        this.context = contex;
        applicationName = contex.getApplicationName();
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public static String getApplicationName() {
        return applicationName;
    }
}
