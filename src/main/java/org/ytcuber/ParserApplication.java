package org.ytcuber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.ytcuber.repository")
public class ParserApplication {
	public static void main(String[] args) {
		SpringApplication.run(ParserApplication.class, args);
	}
}