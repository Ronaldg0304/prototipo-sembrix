package com.sena.sembrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SembrixApplication {

	public static void main(String[] args) {
		SpringApplication.run(SembrixApplication.class, args);
	}

}
