package com.zeus.demo;

import com.antzuhl.zeus.server.ZeusRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ZeusRegistry(registryName = "user-center1", zkAddr = "192.168.124.16:2181",
        serverName = "server-1", serverAddr = "48.89.13.53:8080")
public class ZeusDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZeusDemoApplication.class, args);
    }
}
