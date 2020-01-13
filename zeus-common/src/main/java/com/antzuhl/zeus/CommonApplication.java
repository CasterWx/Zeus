package com.antzuhl.zeus;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CommonApplication implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(CommonApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CommonApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

	}
}

