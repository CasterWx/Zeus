package com.antzuhl.zeus.annotation;

import com.antzuhl.zeus.properties.BeanUtils;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


import java.util.HashMap;
import java.util.Map;

public class AnnotationImport implements ApplicationListener<ContextRefreshedEvent> {

    public static String applicationServerName = null;
    public static String applicationServerAddr = null;
    public static String applicationServerComment = null;
    public static RateLimiter rateLimiter = null;
    private static Logger logger = LoggerFactory.getLogger(AnnotationImport.class);
    private static Map<String, Object> beanMap = new HashMap<>();


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        beanMap = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(RateLimit.class);
        BeanUtils.setContext(contextRefreshedEvent.getApplicationContext());
        Class<?> clazz = null;
        for (String key : beanMap.keySet()) {
            clazz = beanMap.get(key).getClass();
        }
        if (clazz == null) {
            return;
        }
        ZeusProperty zeusProperty = clazz.getAnnotation(ZeusProperty.class);
        RateLimit rateLimit = clazz.getAnnotation(RateLimit.class);
        if (zeusProperty != null) {
            applicationServerName = zeusProperty.serverName();
            applicationServerAddr = zeusProperty.serverAddr();
            applicationServerComment = zeusProperty.comment();
        }

        if (rateLimit != null) {
            int num = rateLimit.num();
            rateLimiter = RateLimiter.create(num);
        }
    }

    public static String getApplicationServerName() {
        return applicationServerName;
    }

    public static String getApplicationServerAddr() {
        return applicationServerAddr;
    }

    public static String getApplicationServerComment() {
        return applicationServerComment;
    }
}
