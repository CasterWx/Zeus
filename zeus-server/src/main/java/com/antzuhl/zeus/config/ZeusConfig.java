package com.antzuhl.zeus.config;

import com.antzuhl.zeus.node.ZkNodeSerializer;
import com.antzuhl.zeus.zkutils.Constant;
import com.antzuhl.zeus.zkutils.ServiceRegistry;
import lombok.Getter;
import lombok.Setter;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
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
                ServiceRegistry.zkClient = new ZkClient(zkAddr, Constant.ZK_SESSION_TIMEOUT);
                ServiceRegistry.zkClient.setZkSerializer(new ZkNodeSerializer());
            }
        }
    }
}
