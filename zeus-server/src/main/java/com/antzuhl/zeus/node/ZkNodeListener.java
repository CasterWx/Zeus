package com.antzuhl.zeus.node;

import com.antzuhl.zeus.zkutils.Constant;
import com.antzuhl.zeus.zkutils.ServiceRegistry;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;

import java.util.List;

public class ZkNodeListener {

    /**
     * 监控新增的命名空间
     * */
    public static void addNamespaceListener() {
        System.out.println("Listener addNamespaceListener:" + Constant.ZK_REGISTRY_PATH);
        ServiceRegistry.zkClient.subscribeChildChanges(Constant.ZK_REGISTRY_PATH, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                for (String np : currentChilds){
                    addServerListener(np);
                }
            }
        });
    }


    public static void addServerListener(String namespace) {
        System.out.println("Listener addServerListener:" + Constant.ZK_REGISTRY_PATH+"/"+namespace);
        ServiceRegistry.zkClient.subscribeChildChanges(Constant.ZK_REGISTRY_PATH+"/"+namespace, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                // 监控server数据变化
                for (String server : currentChilds) {
                    addServerDataListener(parentPath, server);
                }
            }
        });
    }


    public static void addServerDataListener(String namespace, String serverName) {
        System.out.println("Listener addServerDataListener:" + namespace+"/"+serverName);
        //对父节点添加监听子节点变化。
        ServiceRegistry.zkClient.subscribeDataChanges(namespace+"/"+serverName, new IZkDataListener() {

            @Override
            public void handleDataChange(String path, Object data) throws Exception {
                System.out.println("变更的节点为:" + path + ", 变更内容为:" + data);
            }

            @Override
            public void handleDataDeleted(String path) throws Exception {
                System.out.println("删除的节点为:" + path);
            }
        });
    }
}
