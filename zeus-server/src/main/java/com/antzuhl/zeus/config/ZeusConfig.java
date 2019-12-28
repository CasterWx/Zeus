package com.antzuhl.zeus.config;

import com.antzuhl.zeus.zkutils.ServiceRegistry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ZeusConfig  implements ApplicationListener<ContextRefreshedEvent> {

    @Getter
    @Setter
    private static String zkAddr;

    @Autowired
    private Environment env;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (env!=null){
            String zkAddr = env.getProperty("zk.address");
            if (!StringUtils.isEmpty(zkAddr)){
                ZeusConfig.setZkAddr(zkAddr);
            }
        }
    }
}
