package com.antzuhl.zeus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.antzuhl.zeus")
@ServletComponentScan
@EnableScheduling
public class Zeus {

    public static void main(String[] args) {
        SpringApplication.run(Zeus.class, args);
    }
}
