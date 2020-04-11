package com.demo;

import com.antzuhl.zeus.annotation.RateLimit;
import com.antzuhl.zeus.annotation.ZeusProperty;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@ZeusProperty(serverName = "domain", serverAddr = "127.0.0.1", comment = "管理服务")
@RateLimit(num = 1)
public class DemoApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    public void run(ApplicationArguments args) throws Exception {
        System.out.println("1");
    }
}
