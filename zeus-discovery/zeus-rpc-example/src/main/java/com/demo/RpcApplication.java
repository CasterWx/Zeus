package com.demo;

import com.antzuhl.zeus.annotation.RateLimit;
import com.antzuhl.zeus.annotation.ZeusProperty;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@ZeusProperty(serverName = "RpcDemo", serverAddr = "127.0.0.1", comment = "远程服务")
@RateLimit(num = 200)
public class RpcApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(RpcApplication.class, args);
    }

    public void run(ApplicationArguments args) throws Exception {
        System.out.println("1");
    }
}
