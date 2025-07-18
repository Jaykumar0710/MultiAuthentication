package com.SecureVault.MultiAuthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MultiAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiAuthenticationApplication.class, args);
	}

}
