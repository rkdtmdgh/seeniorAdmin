package com.see_nior.seeniorAdmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SeeniorAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeeniorAdminApplication.class, args);
	}

}
