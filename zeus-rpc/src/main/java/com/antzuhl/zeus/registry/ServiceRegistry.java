package com.antzuhl.zeus.registry;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.antzuhl.zeus.cache.ServerCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    public ServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }

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
            logger.error("", e);
        } catch (InterruptedException ex){
            logger.error("", ex);
        }
        return zk;
    }

    private void createNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();
            String path = zk.create(Constant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.info("create zookeeper node ({} => {})", path, data);
        } catch (KeeperException e) {
            logger.error("", e);
        }
        catch (InterruptedException ex){
            logger.error("", ex);
        }
    }

    public List<String> getAllNode(){
        ZooKeeper zk = connectServer();
        if (zk == null){
            return null;
        }
        List<String> nodes = null;
        try {
            nodes = zk.getChildren(Constant.ZK_DATA_PATH, false);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return nodes;
    }

    public static void main(String[] args) {
        ServiceRegistry serviceRegistry = new ServiceRegistry("127.0.0.1:2181");
        serviceRegistry.register("coa");
        serviceRegistry.register("sos");
        while (true) {

        }
    }
}