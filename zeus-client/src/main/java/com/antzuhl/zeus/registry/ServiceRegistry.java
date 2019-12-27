package com.antzuhl.zeus.registry;

import com.antzuhl.zeus.ContextRefreshedListener;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Component
@EnableAutoConfiguration
@Import(ContextRefreshedListener.class)
public class ServiceRegistry  {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public ServiceRegistry() {
    }
    /*
     * Funciation : 注册节点
     *
     * @param data 节点信息
     * */
    public void register(String namespace, String zkAddr, String serverName, String data) {
        if (data != null) {
            ZooKeeper zk = connectServer(zkAddr);
            if (zk != null) {
                createRootNode(zk);
                createNamespaceNode(zk, namespace);
                createNode(zk, namespace, serverName, data);
            }
        }
    }

    private ZooKeeper connectServer(String zkAddr) {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(zkAddr, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
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


    private void createRootNode(ZooKeeper zk){
        try {
            Stat s = zk.exists(Constant.ZK_REGISTRY_PATH, false);
            if (s == null) {
                zk.create(Constant.ZK_REGISTRY_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            logger.error("KeeperException error:", e);
        } catch (InterruptedException ex) {
            logger.error("InterruptedException error:", ex);
        }
    }

    private void createNamespaceNode(ZooKeeper zk, String namespace){
        try {
            Stat s = zk.exists(Constant.ZK_REGISTRY_PATH + "/" +namespace, false);
            if (s == null) {
                zk.create(Constant.ZK_REGISTRY_PATH + "/" + namespace, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            logger.error("KeeperException error:", e);
        } catch (InterruptedException ex) {
            logger.error("InterruptedException error:", ex);
        }
    }


    private void createNode(ZooKeeper zk, String nameSpace, String serverName, String data) {
        try {
            byte[] bytes = data.getBytes();
            String path = zk.create(Constant.ZK_REGISTRY_PATH + "/" + nameSpace + "/" + serverName, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.info("create zookeeper node ({} => {})", path, data);
        } catch (KeeperException e) {
            logger.error("create error:", e);
        } catch (InterruptedException ex){
            logger.error("interrupted error:", ex);
        }
    }
}