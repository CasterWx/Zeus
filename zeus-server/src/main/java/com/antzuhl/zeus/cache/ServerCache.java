package com.antzuhl.zeus.cache;

import com.antzuhl.zeus.node.ServerNode;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class ServerCache {

    @Autowired
    private LoadingCache<String, ServerNode> cache;

    /**
     * 新增緩存
     * */
    public void addCache(ServerNode key) {
        cache.put(key.getPath(), key);
    }

    /*
    * 查詢緩存
    * */
    public ServerNode findCache(String key) {
        ServerNode serverNode = null;
        try {
            serverNode = cache.get(key);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return serverNode;
    }

    /**
     * 更新所有缓存/先删除 再新增
     */
    public void updateCache(List<ServerNode> currentChilds) {
        cache.invalidateAll();
        for (ServerNode currentChild : currentChilds) {
            addCache(currentChild);
        }
    }
}