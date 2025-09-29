package com.testlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.testlab")
public class BankingMvcMiniProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingMvcMiniProjectApplication.class, args);
		System.out.println("heheheh - naya hai yahaha");
	}

}
