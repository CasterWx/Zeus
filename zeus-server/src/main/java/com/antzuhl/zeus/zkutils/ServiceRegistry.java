package com.antzuhl.zeus.zkutils;

import com.alibaba.fastjson.JSON;
import com.antzuhl.zeus.node.ServerNode;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ServiceRegistry {

    public static ZkClient zkClient = null;

    public ServiceRegistry() {
    }

    private ZkClient connectServer(String zkAddr) {
        if (zkClient != null){
            return zkClient;
        }
        zkClient = new ZkClient(zkAddr, Constant.ZK_SESSION_TIMEOUT);
        return zkClient;
    }

    public List<ServerNode> getAllServerNode(String zkAddr){
        ZkClient zk = connectServer(zkAddr);
        List<ServerNode> zkServerList = null;
        List<String> zkList = zk.getChildren(Constant.ZK_REGISTRY_PATH);
        System.out.println(JSON.toJSONString(zkList));
        // 使用cache，如果node存在，就不去getData，不存在就getData
        return zkServerList;
    }

}