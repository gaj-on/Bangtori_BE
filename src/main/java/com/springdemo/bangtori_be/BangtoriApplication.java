package com.springdemo.bangtori_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BangtoriApplication {

	public static void main(String[] args) {
		SpringApplication.run(BangtoriApplication.class, args);
	}

}
