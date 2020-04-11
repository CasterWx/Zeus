package com.antzuhl.zeus.event;

import com.antzuhl.zeus.entity.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceDiscovery {

    private static ConcurrentHashMap<String, Service> discovery = new ConcurrentHashMap<>();

    private ServiceDiscovery() {
    }

    public static ServiceDiscovery getInstance() {
        return IMPORTCLASS.serviceDiscovery;
    }

    public void put(String name, Service service) {
        discovery.put(name, service);
    }

    public Service get(String name) {
        return discovery.get(name);
    }

    public boolean contains(String name) {
        return discovery.contains(name);
    }

    public List<Service> getAll() {
        List<Service> result = new ArrayList<>();
        discovery.forEach((key, value)-> {
            result.add(value);
        });
        return result;
    }

    private static class IMPORTCLASS {
        public static ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
    }
}
