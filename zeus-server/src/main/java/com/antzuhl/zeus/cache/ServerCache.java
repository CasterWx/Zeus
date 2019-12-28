package com.antzuhl.zeus.cache;

import com.antzuhl.zeus.config.ZeusConfig;
import com.antzuhl.zeus.node.ServerNode;
import com.antzuhl.zeus.zkutils.ServiceRegistry;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ServerCache {

    @Autowired
    ServiceRegistry serviceRegistry;

    @Autowired
    private LoadingCache<String, String> cache;

    private AtomicLong index = new AtomicLong();


    public void addCache(String key) {
        cache.put(key, key);
    }


    /**
     * 更新所有缓存/先删除 再新增
     *
     * @param currentChilds
     */
    public void updateCache(List<String> currentChilds) {
        cache.invalidateAll();
        for (String currentChild : currentChilds) {
            String key = currentChild.split("-")[1];
            addCache(key);
        }
    }


    /**
     * 获取所有的服务列表
     *
     * @return
     */
    public List<String> getAll() {

        List<String> list = new ArrayList<String>();

        if (cache.size() == 0) {
            List<ServerNode> allNode = serviceRegistry.getAllServerNode(ZeusConfig.getZkAddr());
            for (ServerNode node : allNode) {
                String key = node.toString().split("-")[1];
                addCache(key);
            }
        }
        for (Map.Entry<String, String> entry : cache.asMap().entrySet()) {
            list.add(entry.getKey());
        }
        return list;

    }

    /**
     * 选取服务器
     *
     * @return
     */
    public String selectServer() {
        List<String> all = getAll();
        if (all.size() == 0) {
            throw new RuntimeException("CIM 服务器可用服务列表为空");
        }
        Long position = index.incrementAndGet() % all.size();
        if (position < 0) {
            position = 0L;
        }

        return all.get(position.intValue());
    }
}