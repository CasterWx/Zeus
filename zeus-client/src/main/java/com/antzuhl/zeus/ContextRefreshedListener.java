package com.antzuhl.zeus;

import com.antzuhl.zeus.registry.ServiceRegistry;
import com.antzuhl.zeus.server.ZeusRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    public static Map<String, Object> beanMap = new HashMap<String, Object>();

    @Autowired
    ServiceRegistry serviceRegistry;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        beanMap = event.getApplicationContext().getBeansWithAnnotation(ZeusRegistry.class);
        System.out.println(beanMap.size());
        Class<?> clazz = null;
        for(String key : beanMap.keySet()){
            clazz = beanMap.get(key).getClass();
        }

        ZeusRegistry zeusRegistry = clazz.getAnnotation(ZeusRegistry.class);

        if (zeusRegistry != null) {
            String registryName = zeusRegistry.registryName();
            String zkAddr = zeusRegistry.zkAddr();
            String serverName = zeusRegistry.serverName();
            String serverAddr = zeusRegistry.serverAddr();
            /*
            * namespace , data[ipAddress]
            * */
            serviceRegistry.register(registryName, zkAddr, serverName, serverAddr);
        }


    }
}