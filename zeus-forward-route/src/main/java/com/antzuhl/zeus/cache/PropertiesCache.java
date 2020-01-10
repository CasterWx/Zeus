package com.antzuhl.zeus.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class PropertiesCache {

    private static LoadingCache<String, String> cache;

    /**
     * 定义缓存, 默认最大数量为3000
     */
    public static LoadingCache<String, String> instance() {
        if (cache != null) {
            return cache;
        } else {
            cache = CacheBuilder.newBuilder().maximumSize(30000).expireAfterWrite(86400, TimeUnit.SECONDS)
                    .build(new CacheLoader<String, String>() {
                        @Override
                        public String load(String name) throws Exception {
                            //在这里可以初始化加载数据的缓存信息，读取数据库中信息或者是加载文件中的某些数据信息
                            return null;
                        }
                    });
        }
        return cache;
    }


    /**
     * 新增緩存
     */
    public void addCache(String key, String value) {
        PropertiesCache.instance().put(key, value);
    }

    /*
     * 查詢緩存
     * */
    public String findCache(String key) {
        String result = null;
        try {
            result = PropertiesCache.instance().get(key);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取所有的缓存Key
     *
     * @return
     */
    public List<String> getAll() {

        List<String> list = new ArrayList<String>();

        for (Map.Entry<String, String> entry : PropertiesCache.instance().asMap().entrySet()) {
            list.add(entry.getKey());
        }
        return list;

    }


}