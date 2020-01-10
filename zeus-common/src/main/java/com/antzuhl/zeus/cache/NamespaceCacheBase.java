package com.antzuhl.zeus.cache;

/**
 *  集群信息缓存。
 *
 *  有哪些集群
 * */
public class NamespaceCacheBase {
    private static String NamespaceCacheFormat = "%s_%s";
    private String namespaceName;

    public NamespaceCacheBase(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void addCache(String key, String value ) {
        PropertiesCache.instance().put(String.format(NamespaceCacheFormat, key), value);
    }

}
