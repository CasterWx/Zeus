package com.antzuhl.zeus.core.filters;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class FilterRegistry {

    private static final FilterRegistry instance = new FilterRegistry();

    public static final FilterRegistry instance() {
        return instance;
    }

    private final ConcurrentHashMap<String, ZeusFilter> filters = new ConcurrentHashMap<String, ZeusFilter>();

    private FilterRegistry() {
    }

    public ZeusFilter remove(String key) {
        return this.filters.remove(key);
    }

    public ZeusFilter get(String key) {
        return this.filters.get(key);
    }

    public void put(String key, ZeusFilter filter) {
        this.filters.putIfAbsent(key, filter);
    }

    public int size() {
        return this.filters.size();
    }

    public Collection<ZeusFilter> getAllFilters() {
        return this.filters.values();
    }

}
