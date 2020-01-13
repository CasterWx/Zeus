package com.zeus.demo;

import com.antzuhl.zeus.annotation.ZeusRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 2019-12-29 13:30:38.032
 * 2019-12-29 13:30:44.278
 *
 * 2019-12-29 13:35:58.886
 * 2019-12-29 13:36:02.159
 *
 * 2019-12-29 13:36:57.605
 * 2019-12-29 13:37:03.752
 * */
@SpringBootApplication
@ZeusRegistry(registryName = "user-center1", zkAddr = "192.168.124.16:2181",
        serverName = "server-3", serverAddr = "48.89.13.33:8081")
public class ZeusDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZeusDemoApplication.class, args);
    }
}
