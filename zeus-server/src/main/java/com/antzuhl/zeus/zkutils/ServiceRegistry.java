package com.antzuhl.zeus.zkutils;

import com.antzuhl.zeus.node.ServerNode;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
public class ServiceRegistry {

    private static ZooKeeper zk = null;

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public ServiceRegistry() {
    }

    public ZooKeeper connectServer(String zkAddr) {
        if (zk != null){
            return zk;
        }
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


    public List<ServerNode> getAllServerNode(String zkAddr){
        ZooKeeper zooKeeper = connectServer(zkAddr);
        List<ServerNode> zkServerList = null;
        try {
            List<String> zkList = zooKeeper.getChildren(Constant.ZK_REGISTRY_PATH, false);
            System.out.println(zkList);
            // 使用cache，如果node存在，就不去getData，不存在就getData

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zkServerList;
    }

}