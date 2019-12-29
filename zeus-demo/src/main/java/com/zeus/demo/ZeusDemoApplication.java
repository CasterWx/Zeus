package com.zeus.demo;

import com.antzuhl.zeus.server.ZeusRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ZeusRegistry(registryName = "user-center1", zkAddr = "192.168.124.16:2181",
        serverName = "server-2", serverAddr = "48.89.13.33:8081")
public class ZeusDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZeusDemoApplication.class, args);
    }
}
