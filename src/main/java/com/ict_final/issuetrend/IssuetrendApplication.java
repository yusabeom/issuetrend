package com.ict_final.issuetrend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class IssuetrendApplication {

	public static void main(String[] args) {
		SpringApplication.run(IssuetrendApplication.class, args);
	}

}
