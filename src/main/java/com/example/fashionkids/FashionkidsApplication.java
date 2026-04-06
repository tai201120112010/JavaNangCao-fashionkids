package com.example.fashionkids;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class FashionkidsApplication {

	public static void main(String[] args) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String hash = encoder.encode("123");

		System.out.println("HASH: " + hash);

		SpringApplication.run(FashionkidsApplication.class, args);
	}
}
