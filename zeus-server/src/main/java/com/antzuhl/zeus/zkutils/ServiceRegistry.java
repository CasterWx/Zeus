package com.antzuhl.zeus.zkutils;

import com.alibaba.fastjson.JSON;
import com.antzuhl.zeus.node.ServerNode;
import com.antzuhl.zeus.node.ZkNodeSerializer;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        ServiceRegistry.zkClient.setZkSerializer(new ZkNodeSerializer());
        return zkClient;
    }

    public List<ServerNode> getAllServerNode(String zkAddr){
        ZkClient zk = connectServer(zkAddr);
        List<ServerNode> zkServerList = new ArrayList<ServerNode>();
        // 获得集群namespace名称
        List<String> zkList = zk.getChildren(Constant.ZK_REGISTRY_PATH);
        System.out.println(JSON.toJSONString(zkList));
        // 根据集群名称获取子下服务节点
        for (String zkName : zkList) {
            List<String> serverNode = zk.getChildren(Constant.ZK_REGISTRY_PATH+"/"+zkName);
            for (String sNode : serverNode) {
                String data = zk.readData(Constant.ZK_REGISTRY_PATH + "/" +zkName +"/"+sNode);
                zkServerList.add(new ServerNode(zkName,sNode,data));
            }
        }
        // 使用cache，如果node存在，就不去getData，不存在就getData
        return zkServerList;
    }

}