package com.antzuhl.zeus.zkutils;

import com.antzuhl.zeus.config.ZeusConfig;
import com.antzuhl.zeus.node.ServerNode;
import com.antzuhl.zeus.node.ZkNodeListener;
import com.antzuhl.zeus.node.ZkNodeSerializer;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
        ZkNodeListener.addNamespaceListener();
        return zkClient;
    }

    public ZResult<List<ServerNode>> getAllServerNode(String namespace){
        ZkClient zk = connectServer(ZeusConfig.getZkAddr());
        ZResult<List<ServerNode>> result = new ZResult<List<ServerNode>>(-1);
        List<ServerNode> zkServerList = new ArrayList<ServerNode>();
        List<String> zkList = null;
        try {
            zkList = zk.getChildren(Constant.ZK_REGISTRY_PATH + "/" + namespace);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String sNode : zkList) {
            String data = zk.readData(Constant.ZK_REGISTRY_PATH + "/" +namespace +"/"+sNode);
            zkServerList.add(new ServerNode(namespace,sNode,data));
        }
        if (!CollectionUtils.isEmpty(zkServerList)){
            result.setData(zkServerList);
            result.setMessage("OK");
            result.setCode(200);
        }
        return result;
    }

    public ZResult<List<String>> getAllNamespace(){
        ZResult<List<String>> result = new ZResult<List<String>>();
        ZkClient zk = connectServer(ZeusConfig.getZkAddr());
        // 获得集群namespace名称
        List<String> zkList = zk.getChildren(Constant.ZK_REGISTRY_PATH);
        if (!CollectionUtils.isEmpty(zkList)){
            result.setCode(200);
            result.setData(zkList);
        }
        return result;
    }
}