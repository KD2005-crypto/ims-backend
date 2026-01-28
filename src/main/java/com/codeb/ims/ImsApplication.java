package com.codeb.ims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// ✅ 1. ADD THIS IMPORT
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// ✅ 2. ADD THIS ANNOTATION
@EnableScheduling
public class ImsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImsApplication.class, args);
	}

}