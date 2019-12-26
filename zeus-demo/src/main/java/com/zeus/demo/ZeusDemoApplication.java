package com.zeus.demo;

import com.antzuhl.zeus.server.ZeusRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.*")
@ZeusRegistry(registryName = "user-center", ipAddress = "127.0.0.1")
public class ZeusDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeusDemoApplication.class, args);
    }

}
