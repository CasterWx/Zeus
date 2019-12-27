package com.antzuhl.zeus.registry;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Component
public class ServiceRegistry  {

    @Autowired
    private Environment env; //不需要getter setter方法

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    public ServiceRegistry() {
        if (env==null){
        }
        env.getProperty("zookeeper.address");
        System.out.println("1");
    }

    static {
        System.out.println("1");
    }


    /*
     * Funciation : 注册节点
     *
     * @param data 节点信息
     * */
    public void register(String data) {
        if (data != null) {
            ZooKeeper zk = connectServer();
            if (zk != null) {
                createNode(zk, data);
            }
        }
    }

    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (IOException e) {
            logger.error("io error :", e);
        } catch (InterruptedException ex){
            logger.error("interrupted error :", ex);
        }
        return zk;
    }

    private void createNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();
            String path = zk.create(Constant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.info("create zookeeper node ({} => {})", path, data);
        } catch (KeeperException e) {
            logger.error("create error:", e);
        } catch (InterruptedException ex){
            logger.error("interrupted error:", ex);
        }
    }
}