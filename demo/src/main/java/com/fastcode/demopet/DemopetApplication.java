package com.fastcode.demopet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DemopetApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemopetApplication.class, args);
	}

}
