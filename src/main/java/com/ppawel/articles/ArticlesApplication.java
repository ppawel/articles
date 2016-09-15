package com.ppawel.articles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class ArticlesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArticlesApplication.class, args);
	}
}
