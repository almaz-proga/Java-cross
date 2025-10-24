package com.example.cross_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CrossProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrossProjectApplication.class, args);
		
	}

}
