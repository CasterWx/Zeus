package com.antzuhl.zeus;

import com.antzuhl.zeus.annotation.ZeusRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ContextRefreshedListener.class);

    private static Map<String, Object> beanMap = new HashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        beanMap = event.getApplicationContext().getBeansWithAnnotation(ZeusRegistry.class);
        logger.info("ZeusRegistry[Annotation] Get Size ==> {}, {}",beanMap.size(), beanMap);
        Class<?> clazz = null;
        for(String key : beanMap.keySet()){
            clazz = beanMap.get(key).getClass();
        }

        ZeusRegistry zeusRegistry = clazz.getAnnotation(ZeusRegistry.class);

        if (zeusRegistry != null) {
            String registryName = zeusRegistry.registryName();
            String serverName = zeusRegistry.serverName();
            String serverAddr = zeusRegistry.serverAddr();

            logger.info("ZeusRegistry[Annotation] Address ==> registryName:{}, zkAddr:{}, serverName:{}, serverAddr:{}",
                    registryName, serverName, serverAddr);
            /*
            * namespace , data[ipAddress]
            * */
//            注册服务
//            serviceRegistry.register(registryName, zkAddr, serverName, serverAddr);
        }
    }
}