package com.antzuhl.zeus.cache;

import com.antzuhl.zeus.bean.FlowApiData;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class FlowApiCache {

    private static Set<String> FLOW_MONITOR = new HashSet<String>();

    private static LoadingCache<String, FlowApiData> cache;

    public FlowApiCache(){
        cache = CacheBuilder.newBuilder().maximumSize(3000).expireAfterWrite(86400, TimeUnit.SECONDS)
                .build(new CacheLoader<String, FlowApiData>() {
                    @Override
                    public FlowApiData load(String name) throws Exception {
                        //在这里可以初始化加载数据的缓存信息，读取数据库中信息或者是加载文件中的某些数据信息
                        return null;
                    }
                });
    }

    public void addFlowKey(String key) {
        FLOW_MONITOR.add(key);
    }
    public boolean findFlowKey(String key) {
        return FLOW_MONITOR.contains(key);
    }

    /**
     * 新增緩存
     * */
    public void addCache(FlowApiData key) {
        cache.put(key.getClassPath(), key);
    }

    /*
    * 查詢緩存
    * */
    public FlowApiData findCache(String key) {
        FlowApiData flowApiData = null;
        try {
            flowApiData = cache.get(key);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return flowApiData;
    }

    /**
     * 更新所有缓存/先删除 再新增
     */
    public void updateCache(List<FlowApiData> currentChilds) {
        cache.invalidateAll();
        for (FlowApiData currentChild : currentChilds) {
            addCache(currentChild);
        }
    }

    public List<FlowApiData> getAllCache() {
        List<FlowApiData> flowApiDatas = new ArrayList<>();
        FLOW_MONITOR.stream().forEach(
                flow -> {
                    flowApiDatas.add(findCache(flow));
                }
        );
        return flowApiDatas;
    }
}