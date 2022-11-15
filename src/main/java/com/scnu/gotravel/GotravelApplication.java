package com.scnu.gotravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class GotravelApplication {
	public static void main(String[] args) {
		SpringApplication.run(GotravelApplication.class, args);
	}

}
