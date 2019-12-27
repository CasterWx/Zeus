package com.zeus.demo;

import com.antzuhl.zeus.server.ZeusRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ZeusRegistry
public class ZeusDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeusDemoApplication.class, args);
    }

}
