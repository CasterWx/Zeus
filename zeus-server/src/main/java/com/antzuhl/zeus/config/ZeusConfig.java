package com.antzuhl.zeus.config;

import com.antzuhl.zeus.node.ServerNode;
import com.antzuhl.zeus.node.ZkNodeListener;
import com.antzuhl.zeus.node.ZkNodeSerializer;
import com.antzuhl.zeus.zkutils.Constant;
import com.antzuhl.zeus.zkutils.ServiceRegistry;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import lombok.Setter;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Component
@Configuration
public class ZeusConfig  implements ApplicationListener<ContextRefreshedEvent> {

    @Getter
    @Setter
    private static String zkAddr;

    public static String getZkAddr() {
        return zkAddr;
    }

    public static void setZkAddr(String zkAddr) {
        ZeusConfig.zkAddr = zkAddr;
    }

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
                if (ServiceRegistry.zkClient != null) {
                    ZkNodeListener.addNamespaceListener();
                }
            }
        }
    }

    /**
     * 定义token缓存, 默认最大数量为3000
     */
    @Bean
    public LoadingCache<String, ServerNode> getLoadingCache() {
        return CacheBuilder.newBuilder().maximumSize(3000).expireAfterWrite(Constant.EXPIRE_SECONDS, TimeUnit.SECONDS)
                .build(new CacheLoader<String, ServerNode>() {
                    @Override
                    public ServerNode load(String name) throws Exception {
                        //在这里可以初始化加载数据的缓存信息，读取数据库中信息或者是加载文件中的某些数据信息
                        return null;
                    }
                });
    }
}
